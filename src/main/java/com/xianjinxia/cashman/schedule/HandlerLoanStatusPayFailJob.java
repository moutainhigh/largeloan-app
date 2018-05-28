package com.xianjinxia.cashman.schedule;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.xianjinxia.cashman.constants.QueueConstants;
import com.xianjinxia.cashman.dto.LoanOrderSpeedDto;
import com.xianjinxia.cashman.dto.SyncLoanOrderDto;
import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.SpeedCardPayStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.IMqMessageService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xjx.mqclient.pojo.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/3/30.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 11:33 2018/3/30
 * @Modified By:
 */
@Service
public class HandlerLoanStatusPayFailJob extends PagebleScanCollectionJob<LoanOrderSpeedDto, List<LoanOrderSpeedDto>> {

    private static Logger logger= LoggerFactory.getLogger(HandlerLoanStatusPayFailJob.class);

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;
    @Autowired
    private IMqMessageService mqMessageService;

    @Autowired
    private LoanOrderMapper loanOrderMapper;
    private List<Integer> productCategoryList = new ArrayList<Integer>();
    private List<String> speedCardPayStatus = new ArrayList<String>();
    private List<String> status = new ArrayList<String>();
    //失效时间--更新时间加上72小时--3天
    private Date  deadTime;
    public boolean before(){
        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_SMALL.getCode());
        productCategoryList.add(ProductCategoryEnum.PRODUCT_CATEGORY_BIG.getCode());
        speedCardPayStatus.add(SpeedCardPayStatusEnum.SPEED_CARD_PAY_FAIL.getCode());
        status.add(TrdLoanOrderStatusEnum.APPROVED.getCode());
//        status.add(TrdLoanOrderStatusEnum.MANUAL_APPROVED.getCode());--目前人工审核成功的状态数据不捞
        deadTime = DateUtil.addDay(new Date(),-3);
        return true;
    }
    @Override
    @Transactional
    public void process(List<LoanOrderSpeedDto> item) {
        List<MqMessage> messageList = new ArrayList<>();
        for (LoanOrderSpeedDto loanOrderSpeedDto : item) {
            //更改订单状态为支付失败
            loanOrderMapper.updateStatusForSpeed(loanOrderSpeedDto.getId(),TrdLoanOrderStatusEnum.LOAN_FAIL.getCode(),loanOrderSpeedDto.getStatus());
            //更改还款计划为40状态
            repaymentPlanMapper.updateStatus(loanOrderSpeedDto.getTrdLoanOrderId(), RepaymentPlanStatusEnum.Canceled.getCode());
            // 发送MQ到trade-app， 同步订单的状态
            SyncLoanOrderDto syncLoanOrderDto = new SyncLoanOrderDto();
            syncLoanOrderDto.setLoanOrderId(loanOrderSpeedDto.getTrdLoanOrderId());
            syncLoanOrderDto.setStatus(TrdLoanOrderStatusEnum.LOAN_FAIL.getCode());
            syncLoanOrderDto.setProductCategory(loanOrderSpeedDto.getProductCategory());
            MqMessage syncLoanOrderMessage = new MqMessage(JSON.toJSONString(syncLoanOrderDto), QueueConstants.CASHMANAPP_SYNC_TRD_ORDER_STAUTS_TO_TRADE);
            messageList.add(syncLoanOrderMessage);
            logger.info("synLoanOrder info send mq info:{}",syncLoanOrderDto.toString());
        }
        mqMessageService.sendMessageList(messageList);
    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    public List<Integer> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<Integer> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public List<String> getSpeedCardPayStatus() {
        return speedCardPayStatus;
    }

    public void setSpeedCardPayStatus(List<String> speedCardPayStatus) {
        this.speedCardPayStatus = speedCardPayStatus;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    @Override
    public Page<LoanOrderSpeedDto> fetch() {
      Page<LoanOrderSpeedDto> pages = (Page<LoanOrderSpeedDto>)loanOrderMapper.selectByCategoryAndSpeedStatus(productCategoryList,status,speedCardPayStatus,deadTime);
        return pages;
    }
}
