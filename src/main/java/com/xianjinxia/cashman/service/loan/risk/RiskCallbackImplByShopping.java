package com.xianjinxia.cashman.service.loan.risk;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.GroupRiskResultEnum;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xjx.mqclient.pojo.MqMessage;
import com.xjx.mqclient.service.MqClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskCallbackImplByShopping extends RiskCallbackAbstract {

    private static final Logger logger = LoggerFactory.getLogger(RiskCallbackImplByShopping.class);

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private MqClient mqClient;

    @Override
    public RiskCallbackResult callback(LoanOrder loanOrder, GroupRiskResultEnum groupRiskResultEnum) {
        int shoppingProductCategory = ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode().intValue();
        if (loanOrder.getProductCategory().intValue() != shoppingProductCategory){
            return new RiskCallbackResult(false);
        }

        TrdLoanOrderStatusEnum orderStatusEnum = groupRiskResultEnum.getOrderStatus();
        String updatedToStatus = orderStatusEnum.getCode();
        super.updateLoanOrderStatus(loanOrder.getId(), loanOrder.getTrdLoanOrderId(), loanOrder.getStatus(), updatedToStatus);


        // 发送MQ到trade-app， 同步订单的状态
        SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
        syncLoanOrderDto.setLoanOrderId(loanOrder.getTrdLoanOrderId());
        syncLoanOrderDto.setStatus(updatedToStatus);
        syncLoanOrderDto.setProductCategory(loanOrder.getProductCategory());
        MqMessage syncTrdLoanOrderMessage = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);

        mqClient.sendMessage(syncTrdLoanOrderMessage);
        return new RiskCallbackResult(true);
    }
}
