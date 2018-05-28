package com.xianjinxia.cashman.service.repay;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.mapper.OverdueCalcLogMapper;
import com.xianjinxia.cashman.mapper.ProductsFeeConfigMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class RepaymentPlanOverdueOperatorImpl implements RepaymentPlanOverdueOperator {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanOverdueOperatorImpl.class);

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private OverdueCalcLogMapper overdueCalcLogMapper;

    @Autowired
    private ProductsFeeConfigMapper productsFeeConfigMapper;


    /**
     *
     * modify by zhangyongjia at 2018-04-22
     * <p>
     *     1. 首日逾期费用：初始化当期借款本金的5%
     *     2. 首日之后每日按0.6%收取
     *     3. 滞纳金最高金额不会超过本金
     * </p>
     *
     *===========================================================================================
     *
     *
     * modify by ganminghui at 2018-04-12
     * <p>
     * 1. 在原先逻辑的基础上对罚息的算法做修改, 日罚息每日计算一次, 总罚息每日叠加(新罚息 = 还款计划中待偿还本金为基础计算罚息)
     * 2. 添加注释
     * 注意:【还款计划中待偿还本金可能会变动,变动场景: 部分还款】
     * </p>
     * ===================================================================
     * <p>
     * 1. 原罚息 = 逾期利息 + 滞纳金
     * 1.1 逾期利息 = 计划还款本金 * 逾期天数 /30 * 利息费率
     * 1.2 滞纳金 = 计划还款本金 * 逾期天数 * 逾期费率
     * 2. 待还款总额 = 待还款本金 + 待还款利息 - 已入账金额 - 在途金额 + 新罚息
     * </p>
     * ===================================================================
     * <p>
     * 1. 新罚息 = 今天的逾期利息 + 今天的滞纳金 + 昨天的罚息
     * 1.1 今天逾期利息 = 待偿还本金 * 1(逾期天数) * 利息费率
     * 1.2 今天滞纳金 = 待偿还本金 * 1(逾期天数) * 逾期费率
     * 1.3 今天罚息 = 今天逾期利息 + 今天滞纳金
     * 2. 待还款总额 = 原始本金利息 + 今天罚息 + 昨天罚息 - 已入账金额 - 在途金额
     * </p>
     *
     * @param isUpdateOperationFlag 是否更新"operationFlag"标识
     * @param repaymentPlan         当前库中的还款计划
     */
    @Override
    @Transactional
    public RepaymentPlan checkAndUpdateOverdue(RepaymentPlan repaymentPlan, boolean isUpdateOperationFlag) {

        /** 若还款计划状态是"已还款",则不需要更新还款计划 */
        if (repaymentPlan.getStatus().equals(RepaymentPlanStatusEnum.Repaymented.getCode())) {
            return repaymentPlan;
        }

        /** 若在途金额大于0 或者 待还款总金额等于0, 则不需要更新还款计划 */
        if (repaymentPlan.getRepaymentWaitingAmount() > 0 && repaymentPlan.getRepaymentTotalAmount() == 0) {
            return repaymentPlan;
        }

        /** 若还款计划没有逾期,则不用更新还款计划 */
        if (!repaymentPlan.getIsOverdue()) {
            return repaymentPlan;
        }

        /** 若罚息计算日是今天，则不用更新还款计划 */
        if (DateUtil.isToday(repaymentPlan.getOverdueCalTime())) {
            return repaymentPlan;
        }

        Date currentTime = DateUtil.dateFilter(new Date());
        Date planRepaymentTime = DateUtil.dateFilter(repaymentPlan.getRepaymentPlanTime());

        /** 当前时间在计划还款时间之前,即"没逾期" */
        if (currentTime.before(planRepaymentTime)) {
            return repaymentPlan;
        }

        // 逾期天数
        Integer overdueDayCount = new Long((currentTime.getTime() - planRepaymentTime.getTime()) / 3600 / 24 / 1000).intValue();

        // 逾期费用
        Integer overdueFeeAmount = this.calculateCurrentDayOverdueFeeAmount(repaymentPlan);
        logger.info("新产生的逾期费用：{}", overdueFeeAmount);
        logger.info("原始本金：{}", repaymentPlan.getRepaymentPrincipalAmount());
        logger.info("原始本息：{}", repaymentPlan.getRepaymentOriginAmount());
        logger.info("当前逾期费：{}",repaymentPlan.getOverdueFeeAmount());
        logger.info("已入账金额：{}", repaymentPlan.getRepaymentIncomeAmount());
        logger.info("在途金额：{}", repaymentPlan.getRepaymentWaitingAmount());

        
        /** 计算待还款金额 */
        int totalAmount = BigDecimal.ZERO.add(new BigDecimal(repaymentPlan.getRepaymentTotalAmount()))
                            .add(new BigDecimal(overdueFeeAmount))
                            .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

        /** 若计算新的待还款总额和原计划待还款总额一致,则不需要更新操作.(可能定时任务已经更新了最新的罚息、待还款总额) */
        if (repaymentPlan.getRepaymentTotalAmount() == totalAmount) {
            return repaymentPlan;
        }

        if (repaymentPlan.getRepaymentTotalAmount() > totalAmount) {
            logger.info("重新计算待还款总金额，还款计划ID：{}, 当前待还款总金额：{}，计算结果：{}", repaymentPlan.getId(), repaymentPlan.getRepaymentTotalAmount(), totalAmount);
            throw new ServiceException("还款金额计算有误");
        }


        if (isUpdateOperationFlag){
            repaymentPlanMapper.updateOverdueDayAndFeeById(
                    repaymentPlan.getId(),
                    totalAmount,
                    true,
                    overdueDayCount,
                    overdueFeeAmount,
                    RepaymentPlanOperationFlagEnum.OVERDUE.getCode(),
                    repaymentPlan.getVersion());
        }else{
            repaymentPlanMapper.updateOverdueDayAndFeeById(
                    repaymentPlan.getId(),
                    totalAmount,
                    true,
                    overdueDayCount,
                    overdueFeeAmount,
                    null,
                    repaymentPlan.getVersion());
        }


        /** 记录日罚息计算操作日志(存在UK约束,若触发,直接回滚事务) */
        overdueCalcLogMapper.insert(repaymentPlan.getId());


        logger.info("修改还款计划id:{}, 待还款总金额:{}(分), 总罚息:{}(分), 逾期天数：{}(天)", repaymentPlan.getId(), repaymentPlan.getRepaymentTotalAmount(), repaymentPlan.getOverdueFeeAmount(), repaymentPlan.getOverdueDayCount());
        repaymentPlan = repaymentPlanMapper.selectByPrimaryKey(repaymentPlan.getId());
        return repaymentPlan;

    }



    private Integer calculateCurrentDayOverdueFeeAmount(RepaymentPlan repaymentPlan){
        BigDecimal firstDayOverdueRate = new BigDecimal(0.05);
        BigDecimal notFirstDayOverdueRate = new BigDecimal(0.006);

        boolean isOverdueFirstDay = this.isOverdueFirstDay(repaymentPlan.getRepaymentPlanTime());
        Integer repaymentOriginPrincipalAmount = repaymentPlan.getRepaymentOriginPrincipalAmount();

        // 历史生成的初始化本金字段为0， 以剩余待还款本金计算
        if (repaymentOriginPrincipalAmount.intValue() == 0){
            repaymentOriginPrincipalAmount = repaymentPlan.getRepaymentPrincipalAmount();
        }

        Integer overdueFeeAmount;
        // 首日逾期：初始化本金的5%
        if (isOverdueFirstDay){
            overdueFeeAmount = new BigDecimal(repaymentOriginPrincipalAmount).multiply(firstDayOverdueRate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            return overdueFeeAmount;
        }

        // 非首日： 日息0.006%
        overdueFeeAmount = new BigDecimal(repaymentOriginPrincipalAmount).multiply(notFirstDayOverdueRate).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

        // 逾期费用不能大于本金
        if ((repaymentPlan.getOverdueFeeAmount().intValue() + overdueFeeAmount.intValue()) > repaymentOriginPrincipalAmount.intValue()){
            return 0;
        }

        return overdueFeeAmount;

    }

    private boolean isOverdueFirstDay(Date repaymentPlanTime){
        int daysBetween = DateUtil.daysBetween(repaymentPlanTime, new Date());
        if (daysBetween == 1){
            return true;
        }

        return false;
    }

}
