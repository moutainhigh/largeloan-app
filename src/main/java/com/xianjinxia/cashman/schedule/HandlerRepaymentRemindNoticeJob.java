package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.dto.RepaymentNoticeDto;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.enums.MerchantNoEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.TrdLoanOrderStatusEnum;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.schedule.job.PageInfoScanCollectionJob;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.ISmsService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/1/5.
 * 还款提前(2天)提醒短信
 */
@Service
public class HandlerRepaymentRemindNoticeJob extends PageInfoScanCollectionJob<RepaymentNoticeDto, List<RepaymentNoticeDto>> {

    private static Logger logger = LoggerFactory.getLogger(HandlerPaymentRequestTimeoutJob.class);

    @Autowired
    private IRepaymentPlanService repaymentPlanService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IProductsService productsService;
    private Date afterTomorrow;

    private String afterTomorrowString;

    private int startPageIndex;
    private int currentPageIndex;
    private int endPageIndex;

    private int productSize;
    private int nowIndex;
    private Boolean isIsLastPage;
    private NoticeOrdersReq repayRemindNoticeReq;
    private List<NoticeOrdersReq> repayRemindNoticeReqs= new ArrayList<>();
    private String sendTemplateNo;
    @Override
    public boolean before(){
        setAfterTomorrow(DateUtil.daysBefore(DateUtil.yyyyMMdd2Date(DateUtil.yyyyMMdd()),-2));
        setAfterTomorrowString(DateUtil.yyyyMMddForZh(getAfterTomorrow()));
        setStartPageIndex(1);
        setEndPageIndex(2);
        setCurrentPageIndex(1);

        //获取产品类型和
        List<Integer> productCategoryList =productsService.getAllCategoryType();
        //产品id-产品商户号-产品类型（有可能不同产品的短信模板不一致）
        repayRemindNoticeReqs = productsService.getFreezeInfoProductByCategoryList(productCategoryList,false);
        if(CollectionUtils.isEmpty(repayRemindNoticeReqs)){
            logger.info("未查到产品");
            return false;
        }
        setSendTemplateNo();
        //排除掉快借钱包的短信发送
        repayRemindNoticeReqs=productsService.getNoticeProductsRemoveAssigned(repayRemindNoticeReqs,MerchantNoEnum.MERCHANT_KJQB);
        setRepayRemindNoticeReqs(repayRemindNoticeReqs);
        repayRemindNoticeReq = repayRemindNoticeReqs.get(0);
        repayRemindNoticeReq.setPageNum(1);
        repayRemindNoticeReq.setPageSize(pageSize());
        //后天为还款日
        repayRemindNoticeReq.setUseTime(getAfterTomorrow());
        setRepayRemindNoticeReq(repayRemindNoticeReq);
        setProductSize(repayRemindNoticeReqs.size());
        setNowIndex(0);

        repayRemindNoticeReq.setStatus(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
        repayRemindNoticeReq.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        return true;
    }
    @Override
    public void process(List<RepaymentNoticeDto> item) {
        for (RepaymentNoticeDto repaymentNoticeDto : item) {
            try {
                logger.info("短信内容："+repaymentNoticeDto.toString());
                Boolean isSendSuccess = false;
                //快借钱包的短信模板--目前和大额分期不一致
                if(null!=repayRemindNoticeReq.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(repayRemindNoticeReq.getMerchantNo()))){
                    isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(),new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_EXPIRATION_REMINDER_ADVANCE_ID).toString(),
                            new StringBuilder(getAfterTomorrowString()).append(",").append(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(),repayRemindNoticeReq.getMerchantNo()));
                }else {
                        //极速现金侠尾号为1,2,3,4使用新版本
                        if (useNewTemplate(repaymentNoticeDto.getUserPhone())) {
                            isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(),sendTemplateNo ,
                                    new StringBuilder(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(),repayRemindNoticeReq.getMerchantNo()));

                        }else {
                            isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), Constant.SMS_EXPIRATION_REMINDER_ADVANCE_ID,
                                    new StringBuilder(getAfterTomorrowString()).append(",").append(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(),repayRemindNoticeReq.getMerchantNo()));
                        }
                   }
                if(!isSendSuccess){
                    logger.info("还款提前(2天)提醒短信发送失败，bizSeqNo："+repaymentNoticeDto.getBizSeqNo());
                }
            } catch (Exception e) {
                logger.error("发送还款提前(2天)提醒短信超时，订单bizSeqNo: bizSeqNos[{}]", repaymentNoticeDto.getBizSeqNo(), e);
            }
        }
    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 30000;
    }

    @Override
    public PageInfo<RepaymentNoticeDto> fetch() {
        //当天时间的前两天 后天
        PageInfo<RepaymentNoticeDto> pages = repaymentPlanService.getReapymentPlansForNotice(repayRemindNoticeReq);
        logger.info("还款提前(2天)提醒短信请求参数：{当前页数currentPageIndex："+getCurrentPageIndex()+",查询订单状态为："+TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode()+",订单还款计划状态为："+RepaymentPlanStatusEnum.Waiting.getCode()+",计划应还时间为："+getAfterTomorrowString());
        setStartPageIndex(pages.getNavigateFirstPage());
        setEndPageIndex(pages.getNavigateLastPage());
        setCurrentPageIndex(pages.getPageNum());
        if(pages.isIsLastPage()){//最后一页，而且产品循环未结束
            if((nowIndex<productSize-1)) {
                repayRemindNoticeReq.setPageNum(1);
                setIsLastPage(false);
                setNowIndex(nowIndex + 1);//索引加一
                setRepayRemindNoticeReq(repayRemindNoticeReqs.get(nowIndex));
                repayRemindNoticeReq.setPageNum(1);
                repayRemindNoticeReq.setPageSize(1);
                setStartPageIndex(1);
                setEndPageIndex(2);
                setCurrentPageIndex(1);
            }else if(nowIndex==productSize-1){//索引不动
                repayRemindNoticeReq.setPageNum(pages.getNextPage());
                setIsLastPage(pages.isIsLastPage());
                setNowIndex(nowIndex+1);//索引加一
            }
        }else{//不是最后一页，
            setCurrentPageIndex(pages.getNextPage());
            repayRemindNoticeReq.setPageNum(pages.getNextPage());
            setIsLastPage(pages.isIsLastPage());
        }
        repayRemindNoticeReq.setStatus(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
        repayRemindNoticeReq.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        repayRemindNoticeReq.setUseTime(getAfterTomorrow());
        return pages;
    }
    @Override
    public int startPageIndex() {
        return this.getStartPageIndex();
    }

    @Override
    public int currentPageIndex() {
        return this.getCurrentPageIndex();
    }

    @Override
    public int endPageIndex() {
        return this.getEndPageIndex();
    }
    public Date getAfterTomorrow() {
        return afterTomorrow;
    }

    public void setAfterTomorrow(Date afterTomorrow) {
        this.afterTomorrow = afterTomorrow;
    }

    public String getAfterTomorrowString() {
        return afterTomorrowString;
    }

    public void setAfterTomorrowString(String afterTomorrowString) {
        this.afterTomorrowString = afterTomorrowString;
    }

    public int getStartPageIndex() {
        return startPageIndex;
    }

    public void setStartPageIndex(int startPageIndex) {
        this.startPageIndex = startPageIndex;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public NoticeOrdersReq getRepayRemindNoticeReq() {
        return repayRemindNoticeReq;
    }

    public void setRepayRemindNoticeReq(NoticeOrdersReq repayRemindNoticeReq) {
        this.repayRemindNoticeReq = repayRemindNoticeReq;
    }

    public List<NoticeOrdersReq> getRepayRemindNoticeReqs() {
        return repayRemindNoticeReqs;
    }

    public void setRepayRemindNoticeReqs(List<NoticeOrdersReq> repayRemindNoticeReqs) {
        this.repayRemindNoticeReqs = repayRemindNoticeReqs;
    }

    public int getProductSize() {
        return productSize;
    }

    public void setProductSize(int productSize) {
        this.productSize = productSize;
    }

    public int getNowIndex() {
        return nowIndex;
    }

    public void setNowIndex(int nowIndex) {
        this.nowIndex = nowIndex;
    }

    public Boolean getIsLastPage() {
        return isIsLastPage;
    }

    public void setIsLastPage(Boolean isLastPage) {
        isIsLastPage = isLastPage;
    }

    public String getSendTemplateNo() {
        return sendTemplateNo;
    }

    public void setSendTemplateNo(String sendTemplateNo) {
        this.sendTemplateNo = sendTemplateNo;
    }

    /**
     * 根据时间判断短信模板
     */
    public void setSendTemplateNo() {
        Calendar now = Calendar.getInstance();
        Calendar firstTimeMin = Calendar.getInstance();
        firstTimeMin.set(Calendar.HOUR_OF_DAY, Constant.SMS_NOTICE_8_AM);
        firstTimeMin.set(Calendar.MINUTE,Constant.SMS_NOTICE_MINITE_PART);
        Calendar secondTimeMin = Calendar.getInstance();
        secondTimeMin.set(Calendar.HOUR_OF_DAY, Constant.SMS_NOTICE_1_PM);
        secondTimeMin.set(Calendar.MINUTE,Constant.SMS_NOTICE_MINITE_PART);
        Calendar thirdTimeMin = Calendar.getInstance();
        thirdTimeMin.set(Calendar.HOUR_OF_DAY, Constant.SMS_NOTICE_8_PM);
        thirdTimeMin.set(Calendar.MINUTE,Constant.SMS_NOTICE_MINITE_PART);
        if (now.equals(firstTimeMin)||(now.after(firstTimeMin)&&now.before(secondTimeMin))) {
            this.sendTemplateNo= Constant.SMS_EXPIRATION_REMINDER_ADVANCE_ID_FIRST;
        }else if(now.equals(secondTimeMin)||(now.after(secondTimeMin)&&now.before(thirdTimeMin))){
            this.sendTemplateNo= Constant.SMS_EXPIRATION_REMINDER_ADVANCE_ID_SECOND;
        }else if(now.equals(thirdTimeMin)||now.after(thirdTimeMin)){
            this.sendTemplateNo= Constant.SMS_EXPIRATION_REMINDER_ADVANCE_ID_THIRD;
        }
    }

    /**
     * 根据手机号判断是否使用新的模板
     * @param userphone
     * @return
     */
    private Boolean useNewTemplate(String userphone){
        int endNumber = Integer.valueOf(userphone.substring(userphone.length()-1)).intValue();
        if(endNumber>0&&endNumber<5){
            return  true;
        }else {
            return false;
        }
    }
    @Override
    public Boolean isNotEnd() {
        if(0==productSize&&(null==isIsLastPage)){
            return true;//首次访问时
        }
        return nowIndex<productSize;
    }
}
