package com.xianjinxia.cashman.remote;

import com.xianjinxia.cashman.exceptions.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayCenterRemoteService extends BaseRemoteService {

    private static final String BATCH_QUERY = "/api/payment/repayment/batchQuery";

    private static final Logger logger = Logger.getLogger(PayCenterRemoteService.class);

    @Override
    protected String getServiceName() {
        return "PAYMENTCENTER";
    }

    //查询支付中心是否收到还款请求
    public String queryPaymentRequestStatus(String bizId, String bizType, String requestSource) {
        PayCenterRequest payCenterRequest = new PayCenterRequest(bizId,bizType,requestSource);
        List<PayCenterRequest> req = new ArrayList<>();
        req.add(payCenterRequest);

        PayCenterResponse response = myRestTemplate.httpPost(buildUrl(BATCH_QUERY), req, new ParameterizedTypeReference<PayCenterResponse>() {
        });


        if (!PayCenterResponse.SUCCESS.equals(response.getResponseCode())) {
            throw new ServiceException(response.getResponseCode(), response.getResponseMsg());
        }

        List<PayCenterData> payCenterDataList = response.getData();
        if (CollectionUtils.isEmpty(payCenterDataList)){
            throw new ServiceException("支付中心查询失败，查询的集合返回为空");
        }

        if (payCenterDataList.size()>1){
            throw new ServiceException("支付中心查询失败，查询的集合返回数据超过1条");
        }
        return payCenterDataList.get(0).getStatus();
    }


}

class PayCenterRequest {
    private String bizId;
    private String bizType;
    private String requestSource;

    public PayCenterRequest(String bizId, String bizType, String requestSource) {
        this.bizId = bizId;
        this.bizType = bizType;
        this.requestSource = requestSource;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }
}

class PayCenterResponse {
    static final String SUCCESS = "1000";//处理成功
    static final String FAILURE = "4000";//处理失败

    private String responseCode;
    private String responseMsg;
    private List<PayCenterData> data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public List<PayCenterData> getData() {
        return data;
    }

    public void setData(List<PayCenterData> data) {
        this.data = data;
    }
}

class PayCenterData {
    private String bizId;
    private String requestSource;
    private String bizType;
    private String status;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
