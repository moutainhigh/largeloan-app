package com.xianjinxia.cashman.service.repay.jobs;

import com.google.gson.Gson;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.exceptions.SqlUpdateException;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.ScheduleTaskOverdue;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.OverdueCalcLogMapper;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.mapper.ScheduleTaskOverdueMapper;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xjx.mqclient.pojo.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import com.xianjinxia.cashman.enums.LoanOrderStatusEnum;

@Service
public class OverdueJobService {

    private static final Logger logger = LoggerFactory.getLogger(OverdueJobService.class);

    @Autowired
    private IRepaymentPlanService repaymentOrderService;

    @Autowired
    private ScheduleTaskOverdueMapper scheduleTaskOverdueMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private ILoanOrderService loanOrderService;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;

    @Autowired
    private IMqMessageService mqMessageService;


    @Autowired
    private OverdueCalcLogMapper overdueCalcLogMapper;

    private Map<Long, Products> productsCache = new ConcurrentHashMap<>();

    @Transactional( propagation = Propagation.REQUIRES_NEW)
    public void prepareOverdueData(RepaymentPlan repaymentPlan) {
        Long repaymentOrderId = repaymentPlan.getId();

        // 1.验证是否有正在还款的订单
        if (repaymentPlan.getRepaymentTotalAmount() == 0 && repaymentPlan.getRepaymentWaitingAmount() > 0) {
            logger.warn("还款订单[{}]有正在全额还款的订单，逾期任务暂不处理", repaymentOrderId);
            return;
        }

        Products product = productsCache.get(repaymentPlan.getProductId());
        if (ObjectUtils.isEmpty(product)) {
            product = productsMapper.selectById(repaymentPlan.getProductId());
            productsCache.put(product.getId(), product);
        }

        // 2.插入逾期任务表
        ScheduleTaskOverdue overdueOrder = new ScheduleTaskOverdue();
        overdueOrder.setUserId(repaymentPlan.getUserId());
        overdueOrder.setRepaymentOrderId(repaymentPlan.getId());
        overdueOrder.setIsCollection(false);
        overdueOrder.setIsRepaymented(false);
        overdueOrder.setLastCalculateTime(repaymentPlan.getRepaymentPlanTime());
        overdueOrder.setCreatedTime(new Date());
        overdueOrder.setUpdatedTime(new Date());
        overdueOrder.setDataValid(true);

        scheduleTaskOverdueMapper.insert(overdueOrder);
        logger.info("增加还款订单到逾期任务表，订单ID：{}", repaymentOrderId);


        // 3.修改还款计划的OperationFlag字段，避免下个任务重新读取相同的数据
        int repaymentPlanUpdateCount = repaymentPlanMapper.updateOverdueOperationFlagById(repaymentPlan.getId(), RepaymentPlanOperationFlagEnum.OVERDUE.getCode(), repaymentPlan.getVersion(), true);
        if (repaymentPlanUpdateCount != 1) {
            logger.warn("修改还款订单逾期状态，OperationFlag失败，订单ID：[{}]", repaymentOrderId);
            throw new ServiceException(Constant.DB_UPDATE_ERROR);
        }


        // 4.修改cashman-app的LoanOrder的状态为"已逾期"
        long loanOrderId = repaymentPlan.getLoanOrderId();
        LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(loanOrderId);
        int count = loanOrderMapper.updateLoanOrderStatusByTrdLoanOrderId(loanOrderId, TrdLoanOrderStatusEnum.OVERDUE.getCode());
        if (count !=1){
            throw new ServiceException("修改借款订单"+loanOrderId+"为逾期状态失败");
        }

        // 5.修改trade-app的LoanOrder的状态为"已逾期"(发送MQ给Trade-App)
        MqMessage syncLoanOrderMessage = new MqMessage();
        SyncLoanOrderDto SyncLoanOrderDto = new SyncLoanOrderDto();
        SyncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
        SyncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
        SyncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.OVERDUE.getCode());
        syncLoanOrderMessage.setMessage(new Gson().toJson(SyncLoanOrderDto));
        syncLoanOrderMessage.setQueueName(QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
        mqMessageService.sendMessage(syncLoanOrderMessage);
    }

    @Transactional
    public void calculateOverdueFee(ScheduleTaskOverdue scheduleTaskOverdue, Date date) {
        logger.info("ScheduleTaskOverdue:{}",scheduleTaskOverdue);
        RepaymentPlan repaymentPlan = repaymentOrderService.getRepaymentPlanByIdWithoutCheck(scheduleTaskOverdue.getRepaymentOrderId());
        if (repaymentPlan.getRepaymentTotalAmount() == 0){
            logger.info("逾期费用计算任务：还款订单当前待还款金额为0，跳过此订单的逾期计算， 订单信息：{}", repaymentPlan);
        }

        repaymentOrderService.checkAndUpdateOverdue(scheduleTaskOverdue.getRepaymentOrderId());

        int count = scheduleTaskOverdueMapper.updateLastCalculateTimeById(scheduleTaskOverdue.getId(), date);
        if (count != 1){
            throw new SqlUpdateException("修改逾期最后计算日期字段失败, 逾期任务的ID：" + scheduleTaskOverdue.getId());
        }

    }
}
