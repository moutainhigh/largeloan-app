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
 * 到期前一天通知短信(到期前一日，还款日为明日)
 */
@Service
public class HandlerDayBeforeDueNoticeJob extends PageInfoScanCollectionJob<RepaymentNoticeDto, List<RepaymentNoticeDto>> {

    private static Logger logger = LoggerFactory.getLogger(HandlerPaymentRequestTimeoutJob.class);

    @Autowired
    private IRepaymentPlanService repaymentPlanService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IProductsService productsService;
    private Date tomorrow;

    private String tomorrowStr;

    private int startPageIndex;
    private int currentPageIndex;
    private int endPageIndex;

    private int productSize;
    private int nowIndex;
    private Boolean isIsLastPage;
    private NoticeOrdersReq dayBeforeDueNoticeRep;
    private List<NoticeOrdersReq> dayBeforeDueNoticeReps = new ArrayList<>();
    private String sendTemplateNo;
    @Override
    public boolean before() {
        setTomorrow(DateUtil.daysBefore(DateUtil.yyyyMMdd2Date(DateUtil.yyyyMMdd()),-1));
        setTomorrowStr(DateUtil.yyyyMMddForZh(getTomorrow()));
        setStartPageIndex(1);
        setEndPageIndex(2);
        setCurrentPageIndex(1);
        setSendTemplateNo();

        //获取产品类型和
        List<Integer> productCategoryList =productsService.getAllCategoryType();
        //产品id-产品商户号-产品类型（有可能不同产品的短信模板不一致）
        dayBeforeDueNoticeReps = productsService.getFreezeInfoProductByCategoryList(productCategoryList,false);
        if(CollectionUtils.isEmpty(dayBeforeDueNoticeReps)){
            logger.info("未查到产品");
            return false;
        }
        dayBeforeDueNoticeReps=productsService.getNoticeProductsRemoveAssigned(dayBeforeDueNoticeReps,MerchantNoEnum.MERCHANT_KJQB);

        setDayBeforeDueNoticeReps(dayBeforeDueNoticeReps);
        dayBeforeDueNoticeRep = dayBeforeDueNoticeReps.get(0);
        dayBeforeDueNoticeRep.setPageNum(1);
        dayBeforeDueNoticeRep.setPageSize(pageSize());
        //后天为还款日
        dayBeforeDueNoticeRep.setUseTime(getTomorrow());
        setDayBeforeDueNoticeRep(dayBeforeDueNoticeRep);
        setProductSize(dayBeforeDueNoticeReps.size());
        setNowIndex(0);

        dayBeforeDueNoticeRep.setStatus(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
        dayBeforeDueNoticeRep.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        return true;
    }

    @Override
    public void process(List<RepaymentNoticeDto> item) {
        for (RepaymentNoticeDto repaymentNoticeDto : item) {
            try {
                logger.info("短信内容："+repaymentNoticeDto.toString());
                Boolean isSendSuccess = false;
                //快借钱包的短信模板--目前和大额分期不一致
                if(null!=dayBeforeDueNoticeRep.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(dayBeforeDueNoticeRep.getMerchantNo()))){
                    isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_LATE_REMIND_ID).toString(),
                            new StringBuilder(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(),dayBeforeDueNoticeRep.getMerchantNo()));
                }else {
                    //极速现金侠尾号为1,2,3,4使用新版本
                    if (useNewTemplate(repaymentNoticeDto.getUserPhone())) {
                        isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), sendTemplateNo,
                                new StringBuilder(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(), dayBeforeDueNoticeRep.getMerchantNo()));
                    } else {
                        isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), Constant.SMS_LATE_REMIND_ID,
                                new StringBuilder(MoneyUtil.changeCentToYuan(repaymentNoticeDto.getRepaymentTotalAmount()).toString()).toString(), dayBeforeDueNoticeRep.getMerchantNo()));
                    }
                }

                if (!isSendSuccess) {
                    logger.warn("到期前一天通短信发送失败，bizSeqNo：" + repaymentNoticeDto.getBizSeqNo());
                }
            } catch (Exception e) {
                logger.error("到期前一天通知短信发送超时，订单bizSeqNo: bizSeqNos[{}]", repaymentNoticeDto.getBizSeqNo(), e);
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
        //当天时间的后一天--到期日，订单状态应该为待还款状态
        PageInfo<RepaymentNoticeDto> pages = repaymentPlanService.getReapymentPlansForNotice(dayBeforeDueNoticeRep);
        logger.info("到期前一日通知短信请求参数：{当前页数currentPageIndex："+getCurrentPageIndex()+",查询订单状态为："+TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode()+",订单还款计划状态为："+RepaymentPlanStatusEnum.Waiting.getCode()+",计划应还时间为："+getTomorrowStr());
        setStartPageIndex(pages.getNavigateFirstPage());
        setEndPageIndex(pages.getNavigateLastPage());
        setCurrentPageIndex(pages.getPageNum());
        if(pages.isIsLastPage()){//最后一页，而且产品循环未结束
            if((nowIndex<productSize-1)) {
                dayBeforeDueNoticeRep.setPageNum(1);
                setIsLastPage(false);
                setNowIndex(nowIndex + 1);//索引加一
                setDayBeforeDueNoticeRep(dayBeforeDueNoticeReps.get(nowIndex));
                dayBeforeDueNoticeRep.setPageNum(1);
                dayBeforeDueNoticeRep.setPageSize(1);
                setStartPageIndex(1);
                setEndPageIndex(2);
                setCurrentPageIndex(1);
            }else if(nowIndex==productSize-1){//索引不动
                dayBeforeDueNoticeRep.setPageNum(pages.getNextPage());
                setIsLastPage(pages.isIsLastPage());
                setNowIndex(nowIndex+1);//索引加一
            }
        }else{//不是最后一页，
            setCurrentPageIndex(pages.getNextPage());
            dayBeforeDueNoticeRep.setPageNum(pages.getNextPage());
            setIsLastPage(pages.isIsLastPage());
        }
        dayBeforeDueNoticeRep.setStatus(TrdLoanOrderStatusEnum.LOAN_SUCCESS.getCode());
        dayBeforeDueNoticeRep.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        dayBeforeDueNoticeRep.setUseTime(getTomorrow());
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

    public Date getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(Date tomorrow) {
        this.tomorrow = tomorrow;
    }

    public String getTomorrowStr() {
        return tomorrowStr;
    }

    public void setTomorrowStr(String tomorrowStr) {
        this.tomorrowStr = tomorrowStr;
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

    public NoticeOrdersReq getDayBeforeDueNoticeRep() {
        return dayBeforeDueNoticeRep;
    }

    public void setDayBeforeDueNoticeRep(NoticeOrdersReq dayBeforeDueNoticeRep) {
        this.dayBeforeDueNoticeRep = dayBeforeDueNoticeRep;
    }

    public List<NoticeOrdersReq> getDayBeforeDueNoticeReps() {
        return dayBeforeDueNoticeReps;
    }

    public void setDayBeforeDueNoticeReps(List<NoticeOrdersReq> dayBeforeDueNoticeReps) {
        this.dayBeforeDueNoticeReps = dayBeforeDueNoticeReps;
    }

    @Override
    public Boolean isNotEnd() {
        if(0==productSize&&(null==isIsLastPage)){
            return true;//首次访问时
        }
        return nowIndex<productSize;
    }

    public String getSendTemplateNo() {
        return sendTemplateNo;
    }

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
            this.sendTemplateNo= Constant.SMS_DAY_BEFORE_DUE_REMINDER_ID_FIRST;
        }else if(now.equals(secondTimeMin)||now.after(secondTimeMin)){
            this.sendTemplateNo= Constant.SMS_DAY_BEFORE_DUE_REMINDER_ID_SECOND;
        }else if(now.equals(thirdTimeMin)||now.after(thirdTimeMin)){
            this.sendTemplateNo= Constant.SMS_DAY_BEFORE_DUE_REMINDER_ID_THIRD;
        }
    }

    private Boolean useNewTemplate(String userphone){
        int endNumber = Integer.valueOf(userphone.substring(userphone.length()-1)).intValue();
        if(endNumber>0&&endNumber<5){
            return  true;
        }else {
            return false;
        }
    }
}
