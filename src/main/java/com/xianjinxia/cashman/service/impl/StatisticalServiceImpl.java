package com.xianjinxia.cashman.service.impl;

import com.xianjinxia.cashman.dto.StatisticRepayOrderDto;
import com.xianjinxia.cashman.dto.StatisticsRepayDto;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.StatisticsMapper;
import com.xianjinxia.cashman.service.IStatisticalService;
import com.xianjinxia.cashman.utils.DateUtil;
import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/4/11.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 11:30 2018/4/11
 * @Modified By:
 */
@Service
public class StatisticalServiceImpl implements IStatisticalService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    private static final Logger logger = LoggerFactory.getLogger(StatisticalServiceImpl.class);

    /**
     * 根据产品ID和商户号查询统计信息
     * @param productId --产品ID
     * @param productCategory --产品类型
     * @param merchantNo --商户号
     * @param repaymentPlanTime --计划还款时间
     * @return
     */
    @Override
    public StatisticsRepayDto getStatisticsInfo(Long productId,Integer productCategory, String merchantNo,String repaymentPlanTime) {
        //订单还款计划有效状态
        List<Integer> validPlaneStatus = new ArrayList<>();
        validPlaneStatus.add(RepaymentPlanStatusEnum.Waiting.getCode());
        validPlaneStatus.add(RepaymentPlanStatusEnum.Part.getCode());
        validPlaneStatus.add(RepaymentPlanStatusEnum.Repaymented.getCode());
        //获取应还人数
        Integer expireNumber= statisticsMapper.countRepaymentNumber(productId,repaymentPlanTime,validPlaneStatus);
        logger.info("应还的人数为：{},时间为：{}",expireNumber,repaymentPlanTime);
        //订单还款计划有效状态--已还款的订单状态
        List<Integer> repaymentPlaneStatus = new ArrayList<>();
        repaymentPlaneStatus.add(RepaymentPlanStatusEnum.Repaymented.getCode());
        //获取应还实还人数
        Integer repaymentNumber =statisticsMapper.countRepaymentNumber(productId,repaymentPlanTime,repaymentPlaneStatus);
        logger.info("实际还款人数为：{},时间为：{}",repaymentNumber,repaymentPlanTime);
        StatisticsRepayDto statisticsRepayDto =new StatisticsRepayDto(ProductCategoryEnum.getStatisticType(productCategory),merchantNo,expireNumber,repaymentNumber);
        logger.info("统计结果对象{}",statisticsRepayDto.toString());
        return  statisticsRepayDto;
    }

    @Override
    public List<StatisticRepayOrderDto> getRepayOrderForDay(String startDay,String endDay) {
        List<StatisticRepayOrderDto> statisticRepayOrderDto =new ArrayList<StatisticRepayOrderDto>();
        //订单状态为21，40,50的订单，还款计划状态为10,20,30的还款计划，大额
        //订单还款计划有效状态
        List<Integer> validPlaneStatus = new ArrayList<>();
        validPlaneStatus.add(RepaymentPlanStatusEnum.Waiting.getCode());
        validPlaneStatus.add(RepaymentPlanStatusEnum.Part.getCode());
        validPlaneStatus.add(RepaymentPlanStatusEnum.Repaymented.getCode());
        //订单状态
        List<String> validOrderStatus= new ArrayList<>();
        validOrderStatus.add(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
        validOrderStatus.add(TrdLoanOrderStatusEnum.OVERDUE.getCode());
        validOrderStatus.add(TrdLoanOrderStatusEnum.SETTLED.getCode());

        List<Integer> productCategoryBig = new ArrayList<>();
        productCategoryBig.add(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        productCategoryBig.add(ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode());
        statisticRepayOrderDto=statisticsMapper.countRepaymentForCollection(startDay,endDay,
                productCategoryBig,validPlaneStatus,validOrderStatus);
        logger.info("查询出来订单数据，开始时间为{},结束时间,查询结果为{}",startDay,endDay,statisticRepayOrderDto.toString());
        return statisticRepayOrderDto;
    }
}
