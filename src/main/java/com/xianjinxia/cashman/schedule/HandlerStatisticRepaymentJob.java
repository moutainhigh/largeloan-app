package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.google.gson.Gson;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.dto.StatisticsRepayDto;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.mapper.ProductsMapper;
import com.xianjinxia.cashman.response.UnfreezeProductsResponse;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.service.IStatisticalService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xjx.mqclient.pojo.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liujun on 2018/4/12.
 * 每日1点钟统计昨日还款情况发送mq到BE
 * @Author: liujun
 * @Description:
 * @Date: Created in 14:30 2018/4/12
 * @Modified By:
 */
@Service("handlerStatisticRepaymentJob")
public class HandlerStatisticRepaymentJob  extends PagebleScanCollectionJob<UnfreezeProductsResponse,List<UnfreezeProductsResponse>> {

    private static final Logger logger = LoggerFactory.getLogger(HandlerStatisticRepaymentJob.class);
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private IMqMessageService mqMessageService;
    @Autowired
    private IStatisticalService statisticalService;

    private String yesterdayStr;
    @Override
    public boolean before() {
        setYesterdayStr(DateUtil.yyyyMMdd(DateUtil.addDay(new Date(),-1)));
        return true;
    }
    @Override
    public void process(List<UnfreezeProductsResponse> item) {
        Gson gson = new Gson();
        MqMessage mqMessage = new MqMessage();
        mqMessage.setQueueName(QueueConstants.SMS_STATISTICS_QUEUE);
        List<StatisticsRepayDto> statisticsRepayDtos = new ArrayList<>();
        for (UnfreezeProductsResponse unfreezeProductsResponse:item) {
            try {
                StatisticsRepayDto statisticsRepayDto = statisticalService.getStatisticsInfo(unfreezeProductsResponse.getId(),unfreezeProductsResponse.getProductCategory(),unfreezeProductsResponse.getMerchantNo(),getYesterdayStr());
                logger.info("统计数据消息为 {}",statisticsRepayDto.toString());
                if(null!=statisticsRepayDto){
                    statisticsRepayDtos.add(statisticsRepayDto);
                }
            } catch (Exception e){
                logger.error("统计数据通知be失败");
            }
        }
        mqMessage.setMessage(gson.toJson(statisticsRepayDtos));
        logger.info("统计数据消息为 {}",gson.toJson(statisticsRepayDtos));
        mqMessageService.sendMessage(mqMessage);
    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    @Override
    public Page<UnfreezeProductsResponse> fetch() {
        List<Integer> productCategoryList = new ArrayList();
//        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode()); //--暂时不统计小额和商城
        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        Page<UnfreezeProductsResponse>  page = (Page<UnfreezeProductsResponse>) productsMapper.getFreezeInfoProductByCategoryList(productCategoryList);
        return page;
    }

    public String getYesterdayStr() {
        return yesterdayStr;
    }

    public void setYesterdayStr(String yesterdayStr) {
        this.yesterdayStr = yesterdayStr;
    }
}
