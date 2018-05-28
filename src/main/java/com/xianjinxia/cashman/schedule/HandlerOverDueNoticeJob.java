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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liquan on 2018/1/5.
 * 逾期通知短信(逾期首日，还款日为昨日)
 */
@Service
public class HandlerOverDueNoticeJob  extends PageInfoScanCollectionJob<RepaymentNoticeDto, List<RepaymentNoticeDto>> {

    private static Logger logger = LoggerFactory.getLogger(HandlerPaymentRequestTimeoutJob.class);

    @Autowired
    private IRepaymentPlanService repaymentPlanService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IProductsService productsService;
    private Date yesterday;

    private String yesterdayStr;

    private int startPageIndex;
    private int currentPageIndex;
    private int endPageIndex;

    private int productSize;
    private int nowIndex;
    private Boolean isIsLastPage;
    private NoticeOrdersReq overDueNoticeRep;
    private List<NoticeOrdersReq> overDueNoticeReps = new ArrayList<>();
    @Override
    public boolean before() {
        setYesterday(DateUtil.daysBefore(DateUtil.yyyyMMdd2Date(DateUtil.yyyyMMdd()),1));
        setYesterdayStr(DateUtil.yyyyMMddForZh(getYesterday()));
        setStartPageIndex(1);
        setEndPageIndex(2);
        setCurrentPageIndex(1);


        //获取产品类型和
        List<Integer> productCategoryList =productsService.getAllCategoryType();
        //产品id-产品商户号-产品类型（有可能不同产品的短信模板不一致）
        overDueNoticeReps = productsService.getFreezeInfoProductByCategoryList(productCategoryList,false);
        if(CollectionUtils.isEmpty(overDueNoticeReps)){
            logger.info("未查到产品");
            return false;
        }
        overDueNoticeReps=productsService.getNoticeProductsRemoveAssigned(overDueNoticeReps,MerchantNoEnum.MERCHANT_KJQB);
        setOverDueNoticeReps(overDueNoticeReps);
        overDueNoticeRep = overDueNoticeReps.get(0);
        overDueNoticeRep.setPageNum(1);
        overDueNoticeRep.setPageSize(pageSize());
        //后天为还款日
        overDueNoticeRep.setUseTime(getYesterday());
        setOverDueNoticeRep(overDueNoticeRep);
        setProductSize(overDueNoticeReps.size());
        setNowIndex(0);

        overDueNoticeRep.setStatus(TrdLoanOrderStatusEnum.OVERDUE.getCode());
        overDueNoticeRep.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        return true;
    }

    @Override
    public void process(List<RepaymentNoticeDto> item) {
        for (RepaymentNoticeDto repaymentNoticeDto : item) {
            try {
                logger.info("短信内容："+repaymentNoticeDto.toString());
                Boolean isSendSuccess = false;
                //快借钱包的短信模板--目前和大额分期不一致
                if(null!=overDueNoticeRep.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(overDueNoticeRep.getMerchantNo()))){
                    isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_LATE_REMIND_ID).toString(),
                            repaymentNoticeDto.getTrdLoanOrderId().toString(),overDueNoticeRep.getMerchantNo()));
                }else{
                    isSendSuccess = smsService.sendSms(new SmsDto(repaymentNoticeDto.getBizSeqNo(), repaymentNoticeDto.getUserPhone(), Constant.SMS_LATE_REMIND_ID,
                            repaymentNoticeDto.getTrdLoanOrderId().toString(),overDueNoticeRep.getMerchantNo()));
                }
                if (!isSendSuccess) {
                    logger.warn("逾期短信发送失败，bizSeqNo：" + repaymentNoticeDto.getBizSeqNo());
                }
            } catch (Exception e) {
                logger.error("发送逾期短信超时，订单bizSeqNo: bizSeqNos[{}]", repaymentNoticeDto.getBizSeqNo(), e);
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
        //当天时间的前一天--逾期首日，订单状态应该为逾期状态
        PageInfo<RepaymentNoticeDto> pages = repaymentPlanService.getReapymentPlansForNotice(overDueNoticeRep);
        logger.info("逾期通知短信请求参数：{当前页数currentPageIndex："+getCurrentPageIndex()+",查询订单状态为："+TrdLoanOrderStatusEnum.OVERDUE.getCode()+",订单还款计划时间为："+RepaymentPlanStatusEnum.Waiting.getCode()+",计划应还时间为："+getYesterdayStr());
        setStartPageIndex(pages.getNavigateFirstPage());
        setEndPageIndex(pages.getNavigateLastPage());
        setCurrentPageIndex(pages.getPageNum());
        if(pages.isIsLastPage()){//最后一页，而且产品循环未结束
            if((nowIndex<productSize-1)) {
                overDueNoticeRep.setPageNum(1);
                setIsLastPage(false);
                setNowIndex(nowIndex + 1);//索引加一
                setOverDueNoticeRep(overDueNoticeReps.get(nowIndex));
                overDueNoticeRep.setPageNum(1);
                overDueNoticeRep.setPageSize(1);
                setStartPageIndex(1);
                setEndPageIndex(2);
                setCurrentPageIndex(1);
            }else if(nowIndex==productSize-1){//索引不动
                overDueNoticeRep.setPageNum(pages.getNextPage());
                setIsLastPage(pages.isIsLastPage());
                setNowIndex(nowIndex+1);//索引加一
            }
        }else{//不是最后一页，
            setCurrentPageIndex(pages.getNextPage());
            overDueNoticeRep.setPageNum(pages.getNextPage());
            setIsLastPage(pages.isIsLastPage());
        }
        overDueNoticeRep.setStatus(TrdLoanOrderStatusEnum.OVERDUE.getCode());
        overDueNoticeRep.setRepayStatus(String.valueOf(RepaymentPlanStatusEnum.Waiting.getCode()));
        overDueNoticeRep.setUseTime(getYesterday());
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
    public Date getYesterday() {
        return yesterday;
    }

    public void setYesterday(Date yesterday) {
        this.yesterday = yesterday;
    }

    public String getYesterdayStr() {
        return yesterdayStr;
    }

    public void setYesterdayStr(String yesterdayStr) {
        this.yesterdayStr = yesterdayStr;
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

    public NoticeOrdersReq getOverDueNoticeRep() {
        return overDueNoticeRep;
    }

    public void setOverDueNoticeRep(NoticeOrdersReq overDueNoticeRep) {
        this.overDueNoticeRep = overDueNoticeRep;
    }

    public List<NoticeOrdersReq> getOverDueNoticeReps() {
        return overDueNoticeReps;
    }

    public void setOverDueNoticeReps(List<NoticeOrdersReq> overDueNoticeReps) {
        this.overDueNoticeReps = overDueNoticeReps;
    }

    @Override
    public Boolean isNotEnd() {
        if(0==productSize&&(null==isIsLastPage)){
            return true;//首次访问时
        }
        return nowIndex<productSize;
    }
}
