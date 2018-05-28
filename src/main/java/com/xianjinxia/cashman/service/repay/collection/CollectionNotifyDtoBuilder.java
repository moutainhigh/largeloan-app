package com.xianjinxia.cashman.service.repay.collection;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.controller.EventController;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.ProductsFeeConfig;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import com.xianjinxia.cashman.enums.ProductsFeeConfigEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.mapper.*;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CollectionNotifyDtoBuilder {

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductsFeeConfigMapper productsFeeConfigMapper;

    private static final Logger logger = LoggerFactory.getLogger(CollectionNotifyDtoBuilder.class);

    public CollectionNotifyDto build(Long repaymentPlanId, boolean isRepaymented) {
        RepaymentPlan repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentPlanId);
        CollectionNotifyDto collectionNotifyDto = null;
        if (repaymentPlan != null) {
            LoanOrder loanOrder = loanOrderMapper.selectByTrdOrderId(repaymentPlan.getLoanOrderId());
            ProductsDto products = productsMapper.getById(loanOrder.getProductId());
            ProductsFeeConfig productInterestFeeConfig = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(loanOrder.getProductId(),
                    ProductsFeeConfigEnum.INTEREST_FEE.getCode(), repaymentPlan.getPeriod());

            ProductsFeeConfig productOverdueFeeConfig = productsFeeConfigMapper.getByProductIdAndFeeTypeAndPeriods(loanOrder.getProductId(),
                    ProductsFeeConfigEnum.OVERDUE_FEE.getCode(), repaymentPlan.getPeriod());

            CollectionNotifyLoanDto loanDto = new CollectionNotifyLoanDto();
            loanDto.setId(repaymentPlan.getLoanOrderId());//借款订单ID
            loanDto.setUserId(repaymentPlan.getUserId());//用户ID
            loanDto.setLoanRate(productInterestFeeConfig.getFeeRate().multiply(new BigDecimal(10000)).intValue());//借款利率
            loanDto.setServiceCharge(BigDecimal.ZERO.intValue());//服务费金额，大额的固定是0
            loanDto.setLoanPenaltyRate(productOverdueFeeConfig.getFeeRate().multiply(new BigDecimal(10000)).intValue());//滞纳金费率
            loanDto.setLoanStartTime(loanOrder.getCreatedTime().getTime());//放款时间
            loanDto.setLoanEndTime(repaymentPlan.getRepaymentPlanTime().getTime());//应还时间
            loanDto.setTermNumber(repaymentPlan.getPeriod());//期数
            loanDto.setLateDay(repaymentPlan.getOverdueDayCount());//逾期天数
            loanDto.setAccrual(repaymentPlan.getRepaymentOriginInterestAmount());//利息
            loanDto.setMerchantNo(products.getMerchantNo());//商户号
//            loanDto.setMerchantNo("xianjinxia");//商户号
            loanDto.setBorrowingType(String.valueOf(loanOrder.getProductCategory()));//借款类型 2-大额，3-分期商城

            CollectionNotifyRepaymentDto repaymentDto = new CollectionNotifyRepaymentDto();
            repaymentDto.setId(repaymentPlanId);
            repaymentDto.setLoanId(repaymentPlan.getLoanOrderId());
            repaymentDto.setCreateDate(repaymentPlan.getCreatedTime().getTime());
            repaymentDto.setCreateDate(repaymentPlan.getCreatedTime().getTime());
            repaymentDto.setReceivableDate(repaymentPlan.getRepaymentPlanTime().getTime());
            repaymentDto.setStatus(RepaymentPlanStatusEnum.getText(Integer.parseInt(repaymentPlan.getStatus().toString())));
            repaymentDto.setRealgetServiceCharge(BigDecimal.ZERO.intValue());//实收服务费

            collectionNotifyDto = new CollectionNotifyDto();

            Integer toatalRepayedPrincipalAmt = 0;//已还总本金
            Integer toatalRepayedOverdueAmt = 0;//已还总滞纳金
            Integer toatalRepayedInterestAmt = 0;//已还实收利息

            //还款信息
            List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectRepaymentedRecords(repaymentPlanId);

            ArrayList<CollectionNotifyRepaymentDetailDto> repaymentDetailDtoList = new ArrayList<>();

            if (repaymentRecords != null && repaymentRecords.size() > 0) {
                for (RepaymentRecord repaymentRecord : repaymentRecords) {
                    CollectionNotifyRepaymentDetailDto repaymentDetailDto = new CollectionNotifyRepaymentDetailDto();
                    // 忽略处理中和失败的还款记录状态
                    if (repaymentRecord.getStatus().intValue() != PaymentRequestStatusEnum.SUCCESS.getCode()) {
                        continue;
                    }

                    repaymentDetailDto.setId(repaymentRecord.getId());
                    repaymentDetailDto.setCreateDate(repaymentRecord.getCreatedAt().getTime());
                    repaymentDetailDto.setPayId(repaymentPlan.getId());
                    repaymentDetailDto.setReturnType(repaymentRecord.getPaymentType());
                    repaymentDetailDto.setRealMoney(repaymentRecord.getRepayPrincipalAmt());//本次还款实收本金
                    repaymentDetailDto.setRealPenlty(repaymentRecord.getRepayOverdueAmt()); //本次还款实收滞纳金
                    repaymentDetailDto.setRealgetAccrual(repaymentRecord.getRepayInterestAmt());//本次还款实收利息

                    Integer principalAmt = repaymentRecord.getRepayPrincipalAmt();
                    Integer interestAmt = repaymentRecord.getRepayInterestAmt();
                    Integer overdueAmt = repaymentRecord.getRepayOverdueAmt();

                    toatalRepayedPrincipalAmt += principalAmt;
                    toatalRepayedInterestAmt += interestAmt;
                    toatalRepayedOverdueAmt += overdueAmt;

                    repaymentDetailDto.setRealPrinciple(repaymentPlan.getRepaymentPrincipalAmount().intValue());// 剩余应还本金
                    repaymentDetailDto.setRealInterest(repaymentPlan.getOverdueFeeAmount().intValue());// 剩余应还滞纳金
                    repaymentDetailDto.setRemainAccrual(repaymentPlan.getRepaymentInterestAmount().intValue());//剩余利息

                    repaymentDetailDtoList.add(repaymentDetailDto);
                }
            }

            collectionNotifyDto.setRepaymentDetailList(repaymentDetailDtoList);

            loanDto.setLoanPenalty(repaymentPlan.getOverdueFeeAmount().intValue() + toatalRepayedOverdueAmt.intValue());//滞纳金
            loanDto.setLoanMoney(repaymentPlan.getRepaymentOriginPrincipalAmount().intValue() == 0 ? (toatalRepayedPrincipalAmt.intValue() + repaymentPlan.getRepaymentPrincipalAmount().intValue()) : repaymentPlan.getRepaymentOriginPrincipalAmount().intValue());//借款金额
            loanDto.setPaidMoney(repaymentPlan.getRepaymentOriginPrincipalAmount().intValue() == 0 ? (toatalRepayedPrincipalAmt.intValue() + repaymentPlan.getRepaymentPrincipalAmount().intValue()) : repaymentPlan.getRepaymentOriginPrincipalAmount().intValue());//(本金+服务费)
            repaymentDto.setLoanPenalty(repaymentPlan.getOverdueFeeAmount().intValue() + toatalRepayedOverdueAmt.intValue()); //滞纳金
            repaymentDto.setReceiveMoney(loanDto.getLoanMoney().intValue() + loanDto.getLoanPenalty().intValue() + toatalRepayedInterestAmt.intValue() + repaymentPlan.getRepaymentInterestAmount().intValue());//应还总金额(本金+利息+滞纳金)

            repaymentDto.setRealMoney(toatalRepayedOverdueAmt + toatalRepayedInterestAmt + toatalRepayedPrincipalAmt);//总实收金额
            repaymentDto.setRealgetInterest(toatalRepayedOverdueAmt);//总实收滞纳金
            repaymentDto.setRealgetAccrual(toatalRepayedInterestAmt);//总实收利息
            repaymentDto.setRealgetPrinciple(toatalRepayedPrincipalAmt);//总实收本金
            repaymentDto.setReceivablePrinciple(repaymentPlan.getRepaymentPrincipalAmount().intValue());//剩余应还本金
            repaymentDto.setReceivableInterest(repaymentPlan.getOverdueFeeAmount().intValue());//剩余应还滞纳金
            repaymentDto.setRemainServiceCharge(BigDecimal.ZERO.intValue());//剩余应还服务费
            repaymentDto.setRemainAccrual(repaymentPlan.getRepaymentInterestAmount().intValue());//剩余利息

            collectionNotifyDto.setLoan(loanDto);
            collectionNotifyDto.setRepayment(repaymentDto);
        }

        logger.info("CollectionNotifyDtoBuilder build message length：{}, {}", JSON.toJSONString(collectionNotifyDto).length(), JSON.toJSONString(collectionNotifyDto));

        return collectionNotifyDto;
    }


}
