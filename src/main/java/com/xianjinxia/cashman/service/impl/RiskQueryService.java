package com.xianjinxia.cashman.service.impl;

import com.google.common.collect.Lists;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.enums.*;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.mapper.RepaymentRecordMapper;
import com.xianjinxia.cashman.mapper.RiskQueryMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;
import com.xianjinxia.cashman.service.IRiskQueryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 风控查询自有信息
 * Created by liquan on 2018/1/4.
 */
@Service public class RiskQueryService implements IRiskQueryService {


    static Logger logger = LoggerFactory.getLogger(RiskQueryService.class);

    @Autowired private RiskQueryMapper riskQueryDao;

    @Autowired @Qualifier("oldCashmanRemoteService") OldCashmanRemoteService remoteService;

    @Autowired private RepaymentPlanMapper repaymentPlanDao;

    @Autowired private ProductsMapper productsDao;

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;


    // 筛选还款状态的订单的状态集合
    final List<String> statusList = Lists
        .newArrayList(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode(),
            TrdLoanOrderStatusEnum.SETTLED.getCode(), TrdLoanOrderStatusEnum.OVERDUE.getCode());


    final String logHeader = "风控查询自有信息-》";

    @Override public OpenApiBaseResponse<RiskQueryDto> query(String orderId, String userId) {
        OpenApiBaseResponse<RiskQueryDto> response = new OpenApiBaseResponse<>();
        RiskQueryDto resultDto = new RiskQueryDto();
        response.setData(resultDto);
        boolean userIsOld = false;
        response.setCode(OpenApiBaseResponse.ResponseCode.SUCCESS.getValue());
        if (StringUtils.isEmpty(orderId) && (StringUtils.isEmpty(userId) || !StringUtils
            .isNumeric(userId))) {
            response.setCode(BaseResponse.ResponseCode.PARAM_CHECK_FAIL.getValue());
            logger.error(logHeader + "参数错误。orderId：{}，userId：{}", orderId, userId);
            return response;
        }
        logger.info(logHeader + "参数：订单id：{},用户id：{}", orderId, userId);
        LoanOrder loanObj = null;
        // 订单集合
        List<LoanOrder> loanListObj = null;
        // current 对象是否为null true 为有值
        boolean current=false;
        LoanOrder currentObj=null;
        Long userIdObj = null;
        // 查询该产品对应的订单集合信息
        if (StringUtils.isNotEmpty(orderId)) {
            current=true;
            loanObj = riskQueryDao.selectBybizSeqNo(orderId,null);
            logger.info("loanObj userId=====>"+loanObj.getUserId());
            if(null!=loanObj){
                loanListObj=riskQueryDao
                    .selectUserLoanOrderListByProductId(loanObj.getUserId(), null);
                userIdObj=loanObj.getUserId();
            }
        }
        logger.info("userId=====>"+userIdObj);
        if(StringUtils.isNotEmpty(userId)){
            userIdObj=Long.valueOf(userId);
            loanListObj = riskQueryDao
                .selectUserLoanOrderListByProductId(userIdObj, null);

        }
        logger.info("判断新用户是否有借款订单开始---");
//        // 判断订单id和用户id是否匹配
//        if (null == loanObj && CollectionUtils.isEmpty(loanListObj)) {
//            response.setCode(BaseResponse.ResponseCode.PARAM_CHECK_FAIL.getValue());
//            // 不匹配直接返回空数据的对象
//            return response;
//        }
        logger.info("判断新用户是否有借款订单结束---");
        //订单列表
        List<OrderInfoDto> loanDtoList = new ArrayList<>();

        List<RepaymentDto> repaymentDtos = new ArrayList<>();
        // 设置订单信息
        resultDto.setOrder(loanDtoList);

        // 还款信息
        resultDto.setRepayment(repaymentDtos);
        if (CollectionUtils.isNotEmpty(loanListObj)) {
            // 遍历订单获取对应的还款计划和还款详情
            for (LoanOrder order : loanListObj) {
                if (null == order.getTermRate()) {
                    throw new ServiceException("订单期利率为空");
                }
                if(current){
                    if(orderId.equals(order.getId().toString()) && StringUtils.isEmpty(userId)){
                        currentObj=order;
//                        continue;
                    }
                }
                OrderInfoDto dto = fillOrderInfoDto(order);
                loanDtoList.add(dto);
                // 设置还款计划
                RepaymentDto repeymentDto = new RepaymentDto();
                // 成功的订单 获取该订单对应的还款计划
                if (statusList.contains(order.getStatus())) {
                    // 有放款成功的为老用户
                    userIsOld=true;
                    // 查询还款计划
                    List<RepaymentPlan> planList =
                        repaymentPlanDao.selectRepaymentPlanByLoanOrderId(order.getTrdLoanOrderId(), order.getProductId());
                    // 分期还款信息
                    List<RepaymentPlanInfoDto> repaymentInfoList = fillRepayInfoDto(planList,order);
                    if(CollectionUtils.isNotEmpty(resultDto.getRepayment_period())){
                        resultDto.getRepayment_period().addAll(repaymentInfoList);
                    }else{
                        resultDto.setRepayment_period(repaymentInfoList);
                    }
                    // 计算还款信息，根据还款计划来计算
                    repeymentDto = calcRepeyment(planList,order.getId());
                    repaymentDtos.add(repeymentDto);
                }
            }
        }
        if(CollectionUtils.isEmpty(resultDto.getRepayment_period())){
            resultDto.setRepayment_period(new ArrayList<>());
        }
        if(null!=currentObj){
            resultDto.setCurrent_order(fillOrderInfoDto(currentObj));
        }
        // 查询用户信息
        UserInfoQueryDto userObj = remoteService.getRiskQueryUserInfo(userIdObj);
        if (null != userObj) {
            // 判断是否是老用户
            if(userIsOld){
                userObj.getUser_info().setCustomerType(RiskQueryCustomerTypeEnum.OLD_USER.getCode());
            }else{
                userObj.getUser_info().setCustomerType(RiskQueryCustomerTypeEnum.NEW_USER.getCode());
            }
            resultDto.setUser_info(userObj.getUser_info());
            resultDto.getUser_info().setUser_id(String.valueOf(userIdObj));
            UserContactDto concat = userObj.getUser_contact();

            if(StringUtils.isNotBlank(concat.getFirst_userId())){
                Long firstId=Long.valueOf(concat.getFirst_userId());
                List<LoanOrder> list = riskQueryDao
                    .selectUserLoanOrderListByProductId(firstId, null);
                List<OrderInfoDto> listObj1= new ArrayList<>();
                for(LoanOrder order:list){
                    listObj1.add(fillOrderInfoDto(order));
                }
                List<RepaymentPlanInfoDto> repayDto1= new ArrayList<>();
                for(LoanOrder loanTmp:list){
                    if (statusList.contains(loanTmp.getStatus())) {
                        List<RepaymentPlan> planList =
                            repaymentPlanDao.selectRepaymentPlanByLoanOrderId(loanTmp.getTrdLoanOrderId(), loanTmp.getProductId());
                        repayDto1.addAll(fillRepayInfoDto(planList,loanTmp));
                        if(CollectionUtils.isNotEmpty(concat.getFirst_repayment())){
                           concat.getFirst_repayment().add(calcRepeyment(planList,loanTmp.getTrdLoanOrderId()));
                        }else{
                            concat.setFirst_repayment(Lists.newArrayList(calcRepeyment(planList,loanTmp.getTrdLoanOrderId())));
                        }
                    }
                }
                concat.setFirst_order(listObj1);
                concat.setFirst_repayment_period(repayDto1);
                if(CollectionUtils.isEmpty(concat.getFirst_repayment())){
                    concat.setFirst_repayment(new ArrayList<>());
                }
                concat.setFirst_userId(null);
            }else{
                concat.setFirst_repayment_period(new ArrayList<>());
                concat.setFirst_order(new ArrayList<>());
                concat.setFirst_repayment(new ArrayList<>());
            }
            if(StringUtils.isNotBlank(concat.getTwo_userId())){
                Long twoId=Long.valueOf(concat.getTwo_userId());
                List<RepaymentPlanInfoDto> repayDto2= new ArrayList<>();
                List<LoanOrder> list  = riskQueryDao
                    .selectUserLoanOrderListByProductId(twoId, null);
                for(LoanOrder loanTmp:list){
                    if (statusList.contains(loanTmp.getStatus())) {
                        List<RepaymentPlan> planList =
                            repaymentPlanDao.selectRepaymentPlanByLoanOrderId(loanTmp.getTrdLoanOrderId(), loanTmp.getProductId());
                        repayDto2.addAll(fillRepayInfoDto(planList,loanTmp));
                        if(CollectionUtils.isNotEmpty(concat.getTwo_repayment())){
                            concat.getTwo_repayment().add(calcRepeyment(planList,loanTmp.getTrdLoanOrderId()));
                        }else{
                            concat.setTwo_repayment(Lists.newArrayList(calcRepeyment(planList,loanTmp.getTrdLoanOrderId())));
                        }
                    }
                }
                List<OrderInfoDto> listObj2= new ArrayList<>();
                for(LoanOrder order:list){
                    listObj2.add(fillOrderInfoDto(order));
                }
                concat.setTwo_repayment_period(repayDto2);
                concat.setTwo_order(listObj2);
                if(CollectionUtils.isEmpty(concat.getTwo_repayment())){
                    concat.setTwo_repayment(new ArrayList<>());
                }
                concat.setTwo_userId(null);
            }else{
                concat.setTwo_repayment_period(new ArrayList<>());
                concat.setTwo_order(new ArrayList<>());
                concat.setTwo_repayment(new ArrayList<>());
            }
            resultDto.setUser_contact(userObj.getUser_contact());
            resultDto.setUser_upload_log(userObj.getUser_upload_log());
        }
        logger.info(logHeader + "返回参数：{}", resultDto);
        return response;
    }

    /**
     * 计算还款详情
     * 该planList 已经是根据期数拍好序的 不需要再次排序了
     *
     * @param planList
     * @return
     */
    RepaymentDto calcRepeyment(List<RepaymentPlan> planList,Long orderId) {
        RepaymentDto dto = new RepaymentDto();
        // 是否逾期，有一次逾期就为逾期
        // 遍历 还款计划
        Integer repayment = null;
        // 逾期状态
        Integer overdueStatus = RiskQueryRepaymentStatusEnum.OVERDUE.getCode();
        BigDecimal
            // 应还金额
            loan = new BigDecimal("0"),
            // 实际还款金额
            pay = new BigDecimal("0");

        Date payDate = null;

        // 根据还款计划的期数排序
        // TODO:该list查询出来时就是根据期数由小到大排序的
        List<RepaymentPlan> sortedList =
            planList.stream().sorted(Comparator.comparingInt(RepaymentPlan::getPeriod))
                .collect(toList());
        logger.info("sortedList size:"+sortedList.size()+" orderId="+orderId);
        if(sortedList.size()>=1) {
            RepaymentPlan lastRepaymentPlan = sortedList.get(sortedList.size() - 1);
            List<Integer> overDueDayList = new ArrayList<Integer>();
            for (RepaymentPlan repaymentPlan : sortedList) {
                overDueDayList.add(repaymentPlan.getOverdueDayCount() != null ? repaymentPlan.getOverdueDayCount() : 0);
            }
            Collections.sort(overDueDayList);
            dto.setPlan_repayment_time(lastRepaymentPlan.getRepaymentPlanTime()!=null?String.valueOf(lastRepaymentPlan.getRepaymentPlanTime().getTime()/1000):"");
            dto.setOverdue_day(overDueDayList.get(overDueDayList.size()-1));
        }


        for (RepaymentPlan plan : sortedList) {
            // 计算还款金额
            loan = loan.add(new BigDecimal(plan.getRepaymentTotalAmount()));
            // 计算已经还款金额
            if (RepaymentPlanStatusEnum.Repaymented.getCode() == plan.getStatus()) {
                pay = pay.add(new BigDecimal(plan.getRepaymentIncomeAmount()));
            }
            if (plan.getIsOverdue()) {
                repayment = overdueStatus;
                break;
            }
            // 不是逾期
            if (!overdueStatus.equals(repayment)) {
                // 已结清
                if (RepaymentPlanStatusEnum.Repaymented.getCode() == plan.getStatus()) {
                    repayment = RiskQueryRepaymentStatusEnum.REPAYMENT.getCode();
                }
                // 生息中
                else {
                    repayment = RiskQueryRepaymentStatusEnum.INTEREST.getCode();
                    break;
                }
            }
        }
        dto.setStatus(repayment);
        dto.setUpdatedAt(payDate);
        dto.setAmount(pay.intValue());
        dto.setRepayPrincipalAmt(loan.intValue());
        dto.setRepaymentPlanId(orderId);
        dto.setId(orderId);

        return dto;
    }

    OrderInfoDto fillOrderInfoDto(LoanOrder order){
        Products products = productsDao.selectById(order.getProductId());
        String loanTerm = "";
        if(ProductTermTypeEnum.DAY.getCode().equals(order.getTermUnit())){
            loanTerm = String.valueOf(products.getTerm());
        }else{
            loanTerm = String.valueOf(order.getPeriods());
        }
        logger.info("loanTerm===>"+loanTerm);
        OrderInfoDto dto = new OrderInfoDto(order.getId().toString(),// 订单id
            order.getProductCategory().toString(),  // 业务场景 现金分期 默认 todo:修改这种获取方式不正确
            order.getOrderAmount().toString(),// 借款金额
            order.getTermRate().setScale(2).multiply(new BigDecimal(12*1000)).toString(),//  年化借款利率  风控那边拿到这个值还要乘以0.001
            RiskQueryLoanTermEnum.getRiskStatus(order.getTermUnit()).toString(),
            //  借款方式[0: 天，1：月，2:年]
                loanTerm,//  借款期限[根据loan_method确定，几天、几月、几年]
            len10Date(order.getCreatedTime()),//  下单时间
            RiskQueryTerminalEnum.getRiskCode(order.getTerminal()).toString(),
            //  终端类型[1:android，2iOS，3:h5，4：API ]
            TrdLoanOrderStatusEnum.getRiskStatus(order.getStatus()).toString(),//  订单状态
            TrdLoanSourceEnum.getRiskSouce(order.getSource()).toString(),//  申请来源
            order.getIsDepository(),//  是否存管
            null,// 还款计划集合
            null,// 还款详情
            TrdLoanOrderStatusEnum.getRiskCheckStatus(order.getStatus()).toString()
            // 风控检查状态 订单状态为 RiskQueryOrderEnum.ORDER_FIRSTTRIAL  时有效
        );
        return dto;
    }

    List<RepaymentPlanInfoDto> fillRepayInfoDto(List<RepaymentPlan> planList,LoanOrder loanObj){
        List<RepaymentPlanInfoDto> repaymentInfoList = new ArrayList<>();
        for (RepaymentPlan plan : planList) {
            Date repayTime = plan.getRepaymentRealTime();
            if(null==repayTime){
                repayTime = plan.getUpdatedTime();
            }
            Integer payedOverdueAmt = 0;
            if (plan.getIsOverdue()) {
                //还款信息
                List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectRepaymentedRecords(plan.getId());
                if (repaymentRecords != null && repaymentRecords.size() > 0) {
                    for (RepaymentRecord repaymentRecord : repaymentRecords) {
                        if(repaymentRecord.getRepayOverdueAmt() >= 0){
                            payedOverdueAmt += repaymentRecord.getRepayOverdueAmt();
                        }
                    }
                }
            }
            Integer overdueFeeAmount = plan.getOverdueFeeAmount() + payedOverdueAmt;

            RepaymentPlanInfoDto planDto =
                new RepaymentPlanInfoDto(plan.getId().toString(),//           分期还款订单号
                    loanObj.getId().toString(),//订单号 把trd id 修改为 id
                    loanObj.getId().toString(),// 总还款订单号 和 orderId 相同
                    plan.getUserId(),//    用户ID
                    plan.getPeriod(),//当前期数
                        plan.getRepaymentTotalAmount(),//     预期剩余还款金额 = 待还款总金额  repaymentTotalAmount
                    plan.getRepaymentOriginAmount(),// 预期还款金额 = 初始还款金额, 本金和利息的总额，不含逾期费用
                    len10Date(plan.getRepaymentPlanTime()),//    预期还款时间  repaymentPlanTime
                    0,// 服务费
                    plan.getRepaymentIncomeAmount(),//    实际还款金额  repaymentIncomeAmount
                    len10Date(repayTime),//实际还款时间   repaymentRealTime
                        ((plan.getRepaymentOriginInterestAmount()!=null && plan.getRepaymentOriginInterestAmount()>0)?plan.getRepaymentOriginInterestAmount():plan.getRepaymentInterestAmount()),//    利息  repaymentInterestAmount
                        overdueFeeAmount,//    overdueFeeAmount 滞纳金
                        ((plan.getRepaymentOriginPrincipalAmount()!=null && plan.getRepaymentOriginPrincipalAmount()>0)?plan.getRepaymentOriginPrincipalAmount():plan.getRepaymentPrincipalAmount()),
                    //    本金  repaymentPrincipalAmount
                    plan.getOverdueDayCount(),//  overdueDayCount 滞纳天数
                    plan.getIsOverdue()?"1":"0",//    是否逾期
                    0,  //券抵扣金额
                    RepaymentPlanStatusEnum.getRiskStatus(plan.getStatus())//    还款订单状态
                );
            // 设置逾期状态
            if (plan.getIsOverdue()) {
                planDto.setStatus(RiskQueryRepaymentPlanStatusEnum.OVERDUE.getCode());
            }
            repaymentInfoList.add(planDto);
        }
        return repaymentInfoList;
    }

    String len10Date(Date time){
        if(null==time){
            return "";
        }
        Long number = time.getTime();
        return ""+number/1000;
    }


}
