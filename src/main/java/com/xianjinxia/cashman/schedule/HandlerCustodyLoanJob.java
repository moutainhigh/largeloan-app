package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.SpeedCardPayStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.ICustodyLoanService;
import com.xianjinxia.cashman.service.IMqMessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whb
 * @date 2018/1/4.
 */
@Service("handlerCustodyLoanJob")
public class HandlerCustodyLoanJob extends PagebleScanCollectionJob<LoanOrder,List<LoanOrder>> {
    private static final Logger logger = LoggerFactory
    			.getLogger(HandlerCustodyLoanJob.class);

    @Autowired
    LoanOrderMapper loanOrderMapper;

    @Autowired
    ICustodyLoanService custodyLoanService;

    @Autowired
    IMqMessageService mqMessageService;
    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    /**
     * 注：这里查询出订单并推送给支付中心的产品类型： 非"商城订单"
     * 大额订单：{@link com.xianjinxia.cashman.enums.ProductCategoryEnum#PRODUCT_CATEGORY_BIG}
     * 小额订单：{@link com.xianjinxia.cashman.enums.ProductCategoryEnum#PRODUCT_CATEGORY_SMALL}
     * 商城订单：{@link com.xianjinxia.cashman.enums.ProductCategoryEnum#PRODUCT_CATEGORY_SHOPPING}
     * @return
     */
    @Override
    public Page<LoanOrder> fetch() {
        List<String> statusList = new ArrayList();
        statusList.add(TrdLoanOrderStatusEnum.APPROVED.getCode());
        statusList.add(TrdLoanOrderStatusEnum.MANUAL_APPROVED.getCode());

        List<Integer> productCategoryList = new ArrayList();
        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode());
        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());

        Page<LoanOrder>  page = (Page<LoanOrder>) loanOrderMapper.selectByProductCategoryAndStatus(productCategoryList, statusList);
        return page;
    }

    @Override
    public void process(List<LoanOrder> item) {
        for (LoanOrder order:item) {
            try {
                if(StringUtils.isEmpty(order.getSpeedCardId())||(StringUtils.isNotEmpty(order.getSpeedCardId())&&SpeedCardPayStatusEnum.SPEED_CARD_PAY_SUCCESS.getCode().equals(order.getSpeedCardPayStatus()))){
                    logger.info("speedCardId={},orderId={}",order.getSpeedCardId(),order.getId());
                    custodyLoanService.uploadCustodyLoan(order);
                }
            } catch (Exception e){
                logger.error("存管放款推送支付中心失败  loan_order_id= "+order.getId(),e);
            }
        }

    }


}
