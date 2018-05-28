package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.enums.MerchantNoEnum;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.response.UnfreezeOrdersResponse;
import com.xianjinxia.cashman.schedule.job.PageInfoScanCollectionJob;
import com.xianjinxia.cashman.service.ILoanService;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liquan on 2018/1/2.
 */
@Service
public class HandlerUnfreezeNoticeJob extends PageInfoScanCollectionJob<UnfreezeOrdersResponse, List<UnfreezeOrdersResponse>> {

    private static Logger logger = LoggerFactory.getLogger(HandlerPaymentRequestTimeoutJob.class);

    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private IProductsService productsService;
    @Autowired
    private ILoanService loanService;
    private int startPageIndex;
    private int currentPageIndex;
    private int endPageIndex;

    private int productSize;
    private int nowIndex;
    private Boolean isIsLastPage;
    private NoticeOrdersReq unfreezeOrdersReq;
    private List<NoticeOrdersReq> unfreezeOrdersReqs=new ArrayList<>();
    @Override
    public boolean before(){
        List<Integer> productCategoryList =productsService.getAllCategoryType();
        //产品id-产品商户号-产品类型（有可能不同产品的短信模板不一致）
        unfreezeOrdersReqs = productsService.getFreezeInfoProductByCategoryList(productCategoryList,true);
        if(CollectionUtils.isEmpty(unfreezeOrdersReqs)){
            logger.info("未查到有解冻时间的产品");
            return false;
        }
        setUnfreezeOrdersReqs(unfreezeOrdersReqs);
        unfreezeOrdersReq = unfreezeOrdersReqs.get(0);
        unfreezeOrdersReq.setPageNum(1);
        unfreezeOrdersReq.setPageSize(pageSize());
        setUnfreezeOrdersReq(unfreezeOrdersReq);
        setStartPageIndex(1);
        setEndPageIndex(2);
        setCurrentPageIndex(1);
        setProductSize(unfreezeOrdersReqs.size());
        setNowIndex(0);
        return true;
    }
    @Override
    public void process(List<UnfreezeOrdersResponse>  item) {
        for (UnfreezeOrdersResponse unfreezeOrdersRsp : item) {
            try {
                logger.info("发送短信的数据为："+unfreezeOrdersRsp.toString());
                Boolean isSendSuccess = false;
                //快借钱包的短信模板--目前和极速现金侠不一致
                if(null!=unfreezeOrdersReq.getMerchantNo()&&(MerchantNoEnum.MERCHANT_KJQB.getMerchantNo().equals(unfreezeOrdersReq.getMerchantNo()))){
                    isSendSuccess= smsService.sendSms(new SmsDto(unfreezeOrdersRsp.getBizSeqNo(), unfreezeOrdersRsp.getUserPhone(), new StringBuilder(MerchantNoEnum.MERCHANT_KJQB.getMerchantNoticePrex()).append(Constant.SMS_UNFREEZE_ID).toString(),"",unfreezeOrdersReq.getMerchantNo()));
                }else{
                    isSendSuccess=smsService.sendSms(new SmsDto(unfreezeOrdersRsp.getBizSeqNo(), unfreezeOrdersRsp.getUserPhone(), Constant.SMS_UNFREEZE_ID,"",unfreezeOrdersReq.getMerchantNo()));
                }
                if (!isSendSuccess) {
                    logger.warn("短信发送失败，bizSeqNo：" + unfreezeOrdersRsp.getBizSeqNo());
                }
            } catch (Exception e) {
                logger.error("短信发送异常: PaymentRequest[{}]",unfreezeOrdersRsp.getBizSeqNo() , e);
            }
        }



    }

    @Override
    public int pageSize() {
        return 500;
    }

    @Override
    public int threshold() {
        return 300000;
    }


    @Override
    public PageInfo<UnfreezeOrdersResponse> fetch() {
        //遍历产品取数据
        PageInfo<UnfreezeOrdersResponse> pages = loanService.getUnfreezeOrderList(unfreezeOrdersReq);
        setStartPageIndex(pages.getNavigateFirstPage());
        setEndPageIndex(pages.getNavigateLastPage());
        setCurrentPageIndex(pages.getPageNum());
        if(pages.isIsLastPage()){//最后一页，而且产品循环未结束
            if((nowIndex<productSize-1)) {
                unfreezeOrdersReq.setPageNum(1);
                setIsLastPage(false);
                setNowIndex(nowIndex + 1);//索引加一
                setUnfreezeOrdersReq(unfreezeOrdersReqs.get(nowIndex));
                unfreezeOrdersReq.setPageNum(1);
                unfreezeOrdersReq.setPageSize(1);
                setStartPageIndex(1);
                setEndPageIndex(2);
                setCurrentPageIndex(1);
            }else if(nowIndex==productSize-1){//索引不动
                unfreezeOrdersReq.setPageNum(pages.getNextPage());
                setIsLastPage(pages.isIsLastPage());
                setNowIndex(nowIndex+1);//索引加一
            }
        }else{//不是最后一页，
            unfreezeOrdersReq.setPageNum(pages.getNextPage());
            setIsLastPage(pages.isIsLastPage());
        }
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

    public NoticeOrdersReq getUnfreezeOrdersReq() {
        return unfreezeOrdersReq;
    }

    public void setUnfreezeOrdersReq(NoticeOrdersReq unfreezeOrdersReq) {
        this.unfreezeOrdersReq = unfreezeOrdersReq;
    }

    public List<NoticeOrdersReq> getUnfreezeOrdersReqs() {
        return unfreezeOrdersReqs;
    }

    public void setUnfreezeOrdersReqs(List<NoticeOrdersReq> unfreezeOrdersReqs) {
        this.unfreezeOrdersReqs = unfreezeOrdersReqs;
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

    @Override
    public Boolean isNotEnd() {
        if(0==productSize&&(null==isIsLastPage)){
            return true;//首次访问时
        }
        return nowIndex<productSize;
    }
}