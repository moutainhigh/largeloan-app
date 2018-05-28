package com.xianjinxia.cashman.service.repay;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 按照金额进行还款的时候进行切分
 */
@Component
public class RepaymentPlanSpliter {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanSpliter.class);
    public static final String RECORD_REPAY_PRINCIPAL_AMT = "repayPrincipalAmt";
    public static final String RECORD_REPAY_INTEREST_AMT = "repayInterestAmt";
    public static final String RECORD_REPAY_OVERDUE_AMT = "repayOverdueAmt";

    public List<RepaymentPlan> splitByRepayAmount(Integer repayAmount, List<RepaymentPlan> repaymentPlans) {
        List<RepaymentPlan> splitRepaymentPlans = new ArrayList<>(repaymentPlans.size());
        repaymentPlans = this.sortedByPeriodAsc(repaymentPlans);
        for (Iterator<RepaymentPlan> iterator = repaymentPlans.iterator(); iterator.hasNext(); ) {



            //当还款金额剩余0的时候，跳出循环
            if (repayAmount.intValue() == 0) {
                break;
            }

            RepaymentPlan repaymentPlan = iterator.next();


            if (repaymentPlan.getStatus().intValue() == RepaymentPlanStatusEnum.Repaymented.getCode()){
                continue;
            }

            if (repayAmount.intValue() >= repaymentPlan.getRepaymentTotalAmount().intValue()) {
                repayAmount = repayAmount - repaymentPlan.getRepaymentTotalAmount().intValue();
                splitRepaymentPlans.add(repaymentPlan);
                logger.info("拆分还款订单：[该期是全额还款]:{}", JSON.toJSONString(repaymentPlan));
                continue;
            }

            Integer repayPrincipalAmt = 0;
            Integer repayInterestAmt = 0;
            Integer repayOverdueAmt = 0;

            // 依次减去罚息,利息，本金
            if (repayAmount.intValue() >= repaymentPlan.getRepaymentPrincipalAmount().intValue()) {
                repayPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
                repayAmount = repayAmount - repayPrincipalAmt;
            } else {
                repayPrincipalAmt = repayAmount;
                repayAmount = 0;
            }

            if (repayAmount.intValue() >= repaymentPlan.getRepaymentInterestAmount().intValue()) {
                repayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
                repayAmount = repayAmount - repayInterestAmt;
            } else {
                repayInterestAmt = repayAmount;
                repayAmount = 0;
            }

            if (repaymentPlan.getIsOverdue()!= null && repaymentPlan.getIsOverdue().booleanValue() == true) {
                if (repayAmount.intValue() >= repaymentPlan.getOverdueFeeAmount().intValue()){
                    repayOverdueAmt = repaymentPlan.getOverdueFeeAmount();
                    repayAmount = repayAmount - repayOverdueAmt;
                }else {
                    repayOverdueAmt = repayAmount;
                    repayAmount = 0;
                }
            }



            Integer splitRepayWaitingAmt = repayPrincipalAmt + repayInterestAmt + repayOverdueAmt;
            Integer splitRepayTotalAmt = repaymentPlan.getRepaymentTotalAmount() - splitRepayWaitingAmt;
            repaymentPlan.setRepaymentTotalAmount(splitRepayWaitingAmt);
            repaymentPlan.setRepaymentWaitingAmount(splitRepayWaitingAmt);
            repaymentPlan.setRepaymentPrincipalAmount(repayPrincipalAmt);
            repaymentPlan.setRepaymentInterestAmount(repayInterestAmt);
            repaymentPlan.setOverdueFeeAmount(repayOverdueAmt);

            logger.info("拆分还款订单：[该期是部分还款]:{}", JSON.toJSONString(repaymentPlan));

            splitRepaymentPlans.add(repaymentPlan);
        }
        return splitRepaymentPlans;
    }

    public RepaymentPlan splitByRepayAmount(Integer repayAmount, RepaymentPlan repaymentPlan) {
        if (repayAmount.intValue() > repaymentPlan.getRepaymentTotalAmount().intValue()) {
            throw new ServiceException("还款金额超出还款计划的待还款总金额");
        }

        if (repayAmount.intValue() == repaymentPlan.getRepaymentTotalAmount().intValue()) {
            return repaymentPlan;
        }


        int repayPrincipalAmt;
        int repayInterestAmt;
        int repayOverdueAmt;

        // 减去罚息
        if (repaymentPlan.getIsOverdue()!= null &&repaymentPlan.getIsOverdue().booleanValue() && repayAmount.intValue() >= repaymentPlan.getOverdueFeeAmount().intValue()) {
            repayOverdueAmt = repaymentPlan.getOverdueFeeAmount();
            repayAmount = repayAmount - repayOverdueAmt;
        } else {
            repayOverdueAmt = repayAmount;
            repayAmount = 0;
        }

        //减去利息
        if (repayAmount.intValue() >= repaymentPlan.getRepaymentInterestAmount().intValue()) {
            repayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
            repayAmount = repayAmount - repayInterestAmt;
        } else {
            repayInterestAmt = repayAmount;
            repayAmount = 0;
        }

        // 剩余的是还款的本金
        repayPrincipalAmt = repayAmount;

        Integer splitRepayWaitingAmt = repayPrincipalAmt + repayInterestAmt + repayOverdueAmt;
        Integer splitRepayTotalAmt = repaymentPlan.getRepaymentTotalAmount() - splitRepayWaitingAmt;
        repaymentPlan.setRepaymentTotalAmount(splitRepayWaitingAmt);
        repaymentPlan.setRepaymentWaitingAmount(splitRepayWaitingAmt);
        repaymentPlan.setRepaymentPrincipalAmount(repayPrincipalAmt);
        repaymentPlan.setRepaymentInterestAmount(repayInterestAmt);
        repaymentPlan.setOverdueFeeAmount(repayOverdueAmt);

        return repaymentPlan;
    }

    /**
     *  催收代扣部分代扣或全额代扣还款计划金额计算, 将代扣金额分解成代扣本、利、罚(用于还款流水)
     *  <p>
     *      代扣顺序: 先扣本金、在扣利息、最后在扣滞纳金
     *  </p>
     *  @param repayAmount 代扣金额
     *  @param repaymentPlan 代扣前的还款计划
     *  @return 代扣后的还款计划
     * */
    public Map<String,Integer> splitWithHoldAmount(Integer repayAmount, RepaymentPlan repaymentPlan) {
        /* 代扣本金   */int repayPrincipalAmt;
        /* 代扣利息   */int repayInterestAmt;
        /* 代扣滞纳金 */int repayOverdueAmt;

        /* 先扣本金 */
        if(repayAmount >= repaymentPlan.getRepaymentPrincipalAmount()){
            repayPrincipalAmt = repaymentPlan.getRepaymentPrincipalAmount();
            repayAmount -= repayPrincipalAmt;
        }else {
            repayPrincipalAmt = repayAmount;
            repayAmount = 0;
        }

        /* 在扣利息 */
        if(repayAmount >= repaymentPlan.getRepaymentInterestAmount()){
            repayInterestAmt = repaymentPlan.getRepaymentInterestAmount();
            repayAmount -= repayInterestAmt;
        }else {
            repayInterestAmt = repayAmount;
            repayAmount = 0;
        }

        /* 在扣罚息 */
        repayOverdueAmt = repayAmount >= repaymentPlan.getOverdueFeeAmount() ? repaymentPlan.getOverdueFeeAmount() : repayAmount;

        return ImmutableMap.of(RECORD_REPAY_PRINCIPAL_AMT,repayPrincipalAmt,RECORD_REPAY_INTEREST_AMT,repayInterestAmt,RECORD_REPAY_OVERDUE_AMT,repayOverdueAmt);
    }

    // 根据period进行升序排列
    private List<RepaymentPlan> sortedByPeriodAsc(List<RepaymentPlan> repaymentPlans) {
        Collections.sort(repaymentPlans, new Comparator<RepaymentPlan>() {

            @Override
            public int compare(RepaymentPlan o1, RepaymentPlan o2) {
                int i = o1.getPeriod().intValue() - o2.getPeriod().intValue();
                if (i == 0) {
                    throw new ServiceException("还款计划期数数据出错");
                }
                return i;
            }
        });

        return repaymentPlans;
    }

}