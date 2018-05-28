package com.xianjinxia.cashman.remote;

import com.github.pagehelper.PageInfo;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.dto.LoanOrderContractDto;
import com.xianjinxia.cashman.dto.TradeAppRiskDto;
import com.xianjinxia.cashman.request.NoticeOrdersReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.UnfreezeOrdersResponse;
import com.xianjinxia.cashman.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class TradeAppRemoteService extends BaseRemoteService {

    private static final String GET_LAST_REJECT_LOAN_ORDER = "/service/loan/loan/user-loan-orders";

    private static final String GET_LOAN_ORDER_BY_ORDER_ID = "/service/loan/loan/get-loan-order-by-id";

    private static final String GET_SHOPPING_LOAN_ORDER_BY_ORDER_ID = "/service/shopping/get-loan-order-by-id";

    private static final String GET_USER_ID_BY_BIZ_SEQ_NO = "/service/loan/get-user-id-by-biz-seq-no";

    private static final String GET_UNFREEZE_ORDERS = "/service/loan/get-unfreeze-orders";

    private static final String SYNC_LOAN_ORDER_STATUS = "/service/event/sync-order-status";

    private static final Logger logger = Logger.getLogger(TradeAppRemoteService.class);

    @Override
    protected String getServiceName() {
        return "TRADE-APP";
    }

    //获取最近一次拒绝的借款单
    public TradeAppRiskDto getLastRejectDayCount(Long userId) {

        BaseResponse<TradeAppRiskResponse> response = myRestTemplate.httpGet(buildUrl(GET_LAST_REJECT_LOAN_ORDER) + "?userId=" + userId, new ParameterizedTypeReference<BaseResponse<TradeAppRiskResponse>>() {
        });


        if (!BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())) {
            throw new ServiceException(response.getCode(), response.getMsg());
        }

        TradeAppRiskResponse tradeAppRiskResponse = response.getData();


        if (ObjectUtils.isEmpty(tradeAppRiskResponse)) {
            throw new ServiceException("调用Trade-APP风控数据接口异常");
        }

        TradeAppRiskDto tradeAppRiskDto = new TradeAppRiskDto();


        Date rejectTime = tradeAppRiskResponse.getLastRejectLoanOrder().getUpdatedTime();
        Integer lastRejectDayCount = DateUtil.daysBetween(rejectTime, new Date());
        Integer currentApplyAmount = tradeAppRiskResponse.getCurrentApplyLoanOrder().getOrderAmount();
        Integer currentApplyPeriods = tradeAppRiskResponse.getCurrentApplyLoanOrder().getPeriods();

        tradeAppRiskDto.setCurrentApplyAmount(currentApplyAmount);
        tradeAppRiskDto.setCurrentApplyPeriods(currentApplyPeriods);
        tradeAppRiskDto.setLastRejectDayCount(lastRejectDayCount);
        return tradeAppRiskDto;
    }

    //现金分期合同信息
    public LoanOrderContractDto getLoanOrderByOrderId(Long id) {
        return this.getLoanOrderContractInfo(id, GET_LOAN_ORDER_BY_ORDER_ID);
    }


    //商城订单合同信息
    public LoanOrderContractDto getShoppingLoanOrderByOrderId(Long id) {
       return this.getLoanOrderContractInfo(id, GET_SHOPPING_LOAN_ORDER_BY_ORDER_ID);
    }

    private LoanOrderContractDto getLoanOrderContractInfo(Long id, String url){
        BaseResponse<LoanOrderContractDto> response = myRestTemplate.httpGet(buildUrl(url) + "?id=" + id, new ParameterizedTypeReference<BaseResponse<LoanOrderContractDto>>() {
        });


        if (!BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())) {
            throw new ServiceException(response.getCode(), response.getMsg());
        }

        LoanOrderContractDto loanOrder = response.getData();


        if (ObjectUtils.isEmpty(loanOrder)) {
            throw new ServiceException("调用Trade-APP获取订单信息失败");
        }
        return loanOrder;
    }

    public Long getUserIdByOrderBizSeqNo(String bizSeqNo) {
        BaseResponse<Long> response = myRestTemplate.httpGet(buildUrl(GET_USER_ID_BY_BIZ_SEQ_NO) + "?bizSeqNo=" + bizSeqNo, new ParameterizedTypeReference<BaseResponse<Long>>() {
        });
        if (!BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())) {
            throw new ServiceException(response.getCode(), response.getMsg());
        }
        Long userId = response.getData();
        return userId;
    }


    public PageInfo<UnfreezeOrdersResponse> getUnfreezeOrderList(NoticeOrdersReq unfreezeOrdersReq) {
        BaseResponse<PageInfo<UnfreezeOrdersResponse>> response = myRestTemplate.httpPost(buildUrl(GET_UNFREEZE_ORDERS), unfreezeOrdersReq, new ParameterizedTypeReference<BaseResponse<PageInfo<UnfreezeOrdersResponse>>>() {
        });

        if (!BaseResponse.ResponseCode.SUCCESS.getValue().equals(response.getCode())) {
            throw new ServiceException(response.getCode(), response.getMsg());
        }

        PageInfo<UnfreezeOrdersResponse> unfreezeOrdersResponses = response.getData();

        if (ObjectUtils.isEmpty(unfreezeOrdersResponses)) {
            logger.info("调用Trade-APP没有获取到适合条件的待解冻订单信息");
        }
        return unfreezeOrdersResponses;

    }
}

class LastRejectLoanOrderRequest {
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

class TradeAppLoanOrder {
    /**
     * 表id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 业务流水号
     */
    private String bizSeqNo;

    private String ukToken;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 订单金额，单位为分
     */
    private Integer orderAmount;

    /**
     * 借款服务费
     */
    private Integer feeAmount;

    /**
     * 实际到账金额，单位为分
     */
    private Integer paymentAmount;

    /**
     * 应还金额
     */
    private Integer repaymentAmount;

    /**
     * 总期数
     */
    private Integer periods;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品类型  (1:小额   2:大额)
     */
    private Integer productCategory;

    /**
     * 用户银行卡号id
     */
    private Long userBankCardId;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号后四位
     */
    private String lastFourBankCardNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是老用户：0、新用户；1；老用户
     */
    private Boolean userType;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 真实姓名
     */
    private String userName;

    /**
     * 状态：1:审核中 ; 2:审核失败; 3:放款中 ;4:放款失败;5:放款成功；
     */
    private String status;

    /**
     * 订单来源：01、现金侠 02、融360
     */
    private String source;

    /**
     * 终端类型 01 ios ,02 android, 03 h5
     */
    private String terminal;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 数据有效性 0 无效  1 有效
     */
    private Boolean dataValid;

    /**
     * 追踪号
     */
    private String traceNo;

    public enum LoanOrderStatusEnum {

        APPROVALING("0", "审核中"), APPROVAL_FAILURE("-3", "审核失败"), LOANING("22", "放款中"), LOAN_FAILURE("-10", "放款失败"), LOAN_SUCCESS("21", "放款成功"), LOAN_YHQ("11", "已还清");


        /**
         * 下面两个是老系统的属性，老系统没有还款表，新系统将还款抽取出来了，所以目前用不到以下两个字段
         * STATUS_KKZ("12", "扣款中"),
         * STATUS_KKSB("-7", "扣款失败");
         */

        private String code;
        private String value;

        LoanOrderStatusEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return this.code;
        }

        public String getValue() {
            return this.value;
        }

    }

    public enum LoanOrderTypeEnum {

        LOAN("0", "贷款");

        private String code;
        private String value;

        LoanOrderTypeEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return this.code;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ProductCategoryEnum {

        SMALLAMOUNT("1"), BIGAMOUNT("2");

        ProductCategoryEnum(String code) {
            this.code = code;
        }

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public String getUkToken() {
        return ukToken;
    }

    public void setUkToken(String ukToken) {
        this.ukToken = ukToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBizSeqNo() {
        return bizSeqNo;
    }

    public void setBizSeqNo(String bizSeqNo) {
        this.bizSeqNo = bizSeqNo == null ? null : bizSeqNo.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserBankCardId() {
        return userBankCardId;
    }

    public void setUserBankCardId(Long userBankCardId) {
        this.userBankCardId = userBankCardId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Boolean getUserType() {
        return userType;
    }

    public void setUserType(Boolean userType) {
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal == null ? null : terminal.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo == null ? null : traceNo.trim();
    }


    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLastFourBankCardNo() {
        return lastFourBankCardNo;
    }

    public void setLastFourBankCardNo(String lastFourBankCardNo) {
        this.lastFourBankCardNo = lastFourBankCardNo;
    }
}


class TradeAppRiskResponse {
    /**
     * 用户最近被拒的订单信息
     */
    private TradeAppLoanOrder lastRejectLoanOrder;

    /**
     * 用户当前最新带审核的订单信息
     */
    private TradeAppLoanOrder currentApplyLoanOrder;

    public TradeAppLoanOrder getLastRejectLoanOrder() {
        return lastRejectLoanOrder;
    }

    public void setLastRejectLoanOrder(TradeAppLoanOrder lastRejectLoanOrder) {
        this.lastRejectLoanOrder = lastRejectLoanOrder;
    }

    public TradeAppLoanOrder getCurrentApplyLoanOrder() {
        return currentApplyLoanOrder;
    }

    public void setCurrentApplyLoanOrder(TradeAppLoanOrder currentApplyLoanOrder) {
        this.currentApplyLoanOrder = currentApplyLoanOrder;
    }
}

