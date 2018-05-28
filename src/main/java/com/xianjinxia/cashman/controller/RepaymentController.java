package com.xianjinxia.cashman.controller;


import com.xianjinxia.cashman.dto.*;
import com.xianjinxia.cashman.enums.PaymentChannelEnum;
import com.xianjinxia.cashman.enums.RepaymentMethodEnum;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.request.AlipayRepayIncomeReq;
import com.xianjinxia.cashman.request.RepaymentReq;
import com.xianjinxia.cashman.request.VerifyPaymentReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.IRepaymentService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 还款的restful接口
 *
 * @author zyj@xianjinxia.com
 */
@Api(tags = "cashman_app repay controller")
@RestController
@RequestMapping("/service/repayment")
public class RepaymentController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RepaymentController.class);

    @Autowired
    private IRepaymentService repaymentService;

    @Autowired
    private IRepaymentPlanService repaymentPlanService;

    @Autowired
    private IContractService contractService;

    @ApiOperation("repay commit interface, return a payment-request id to client if success")
    @PostMapping("/repay-commit")
    public BaseResponse<Void> repayCommit(@RequestBody RepaymentReq repayRequest) {
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        try {
            EventInfo event = new EventInfo();
            event.put("repayAmount", repayRequest.getAmount());
            event.put("userId", repayRequest.getUserId());
            event.put("trdLoanOrderId", repayRequest.getTrdLoanOrderId());
//            event.put("repaymentOrderIds", Arrays.toString(repayRequest.getRepaymentOrderIds()));
            event.setEventType("repaym-commit");
            CatUtils.info(event);

            //1. 检查参数
            repayRequest.paramCheck();
            //2. 创建还款请求
//            repayRequest.setPaymentBizTypeEnum(PaymentBizTypeEnum.REPAYMENT);
//            PaymentParamDto paymentParamDto = repaymentService.repayCommit(repayRequest, RepaymentMethodEnum.SELECT_REPAY);
//
            repayRequest.setPaymentBizTypeEnum(PaymentBizTypeEnum.REPAYMENT);
//            PaymentParamDto paymentParamDto =
            repaymentService.repayCommit(repayRequest);


        } catch (ServiceException se) {
            logger.error("还款请求提交失败:", se);
            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            baseResponse.setMsg(se.getMsg());
        } catch (Exception e) {
            logger.error("还款请求提交异常", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }

//    @ApiOperation("repay commit interface, return a payment-request id to client if success")
//    @PostMapping("/repay-commit-amt")
//    public BaseResponse<PaymentParamDto> repayCommitByAmt(@RequestBody RepaymentReq repayRequest) {
//        BaseResponse<PaymentParamDto> baseResponse = new BaseResponse<>();
//        try {
//            EventInfo event = new EventInfo();
//            event.put("repayAmount", repayRequest.getAmount());
//            event.put("userId", repayRequest.getUserId());
//            event.put("trdLoanOrderId", repayRequest.getTrdLoanOrderId());
//            event.setEventType("repaym-commit-amt");
//            CatUtils.info(event);
//
//            //1. 检查参数
//            repayRequest.paramCheck();
//            //2. 创建还款请求
//            repayRequest.setPaymentBizTypeEnum(PaymentBizTypeEnum.REPAYMENT);
//            PaymentParamDto paymentParamDto = repaymentService.repayCommit(repayRequest, RepaymentMethodEnum.AMOUNT_REPAY);
//
//            //3. 返回客户端
//            baseResponse.setData(paymentParamDto);
//
//        } catch (ServiceException se) {
//            logger.error("还款请求提交失败:", se);
//            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
//            baseResponse.setMsg(se.getMsg());
//        } catch (Exception e) {
//            logger.error("还款请求提交异常", e);
//            baseResponse.systemError();
//        }
//        return baseResponse;
//    }

    /**
     * 获取还款单
     *
     * @param loanId
     *
     * @return
     */
    @ApiOperation(value = "/repay-order/order", httpMethod = "POST", notes = "换取借款单")
    @ApiImplicitParam(name = "loanId", value = "还款订单", required = true, dataType = "Long")
    @ApiResponse(response = BaseResponse.class, message = "baseResponse", code = 200)
    @GetMapping("/repayment-order/order")
    public BaseResponse<List<RepaymentPlan>> getRepaymentOrderByLoanOrder(Long loanId) {
        logger.info("查询还款单,参数,loanId:{}", loanId);
        BaseResponse<List<RepaymentPlan>> baseResponse = new BaseResponse<>();
        EventInfo event = new EventInfo();
        event.put("loanId", loanId);
        event.setEventType("repay-plan-list");
        CatUtils.info(event);
        try {
            List<RepaymentPlan> repaymentOrderByLoanOrderId = repaymentService.getRepaymentPlanListByTrdLoanOrderId(loanId);
            baseResponse.setData(repaymentOrderByLoanOrderId);
        } catch (Exception e) {
            logger.error("service/loan/biz-check  has  exception :{}", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }

    @GetMapping("/repayment-list")
    @ApiOperation(value = "/repayment-list", httpMethod = "POST", notes = "换取借款单列表")
    @ApiImplicitParam(name = "loanOrderId", value = "还款订单", required = true, dataType = "Long")
    @ApiResponse(response = BaseResponse.class, message = "baseResponse", code = 200)
    public BaseResponse<List<IndexRepaymentPlanDto>> getRepaymentByLoanOrderId(Long loanOrderId) {
        BaseResponse<List<IndexRepaymentPlanDto>> resp = new BaseResponse<>();
        try {
            resp.setData(repaymentService.getRepaymentOrder(loanOrderId));
        } catch (Exception e) {
            logger.error("/service/repay/wait-repay-list", e);
            resp.systemError();
        }
        return resp;
    }

    /**
     * 获取还款单
     *
     * @param loanOrderId
     *
     * @return
     */
    @ApiOperation(value = "/repayment-list/shopping", httpMethod = "GET", notes = "换取借款单")
    @ApiImplicitParam(name = "loanId", value = "还款订单", required = true, dataType = "Long")
    @ApiResponse(response = BaseResponse.class, message = "baseResponse", code = 200)
    @GetMapping("/repayment-plan/shopping")
    public BaseResponse<List<RepaymentPlan>> getRepaymentOrderByShoppingLoanOrder(Long loanOrderId) {
        logger.info("查询还款单,参数,loanOrderId:{}", loanOrderId);
        BaseResponse<List<RepaymentPlan>> baseResponse = new BaseResponse<>();
        EventInfo event = new EventInfo();
        event.put("loanId", loanOrderId);
        event.setEventType("shopping-repay-plan-list");
        CatUtils.info(event);
        try {
            List<RepaymentPlan> repaymentOrderByLoanOrderId = repaymentService.getRepaymentPlanListByTrdLoanOrderId(loanOrderId);
            baseResponse.setData(repaymentOrderByLoanOrderId);
        } catch (Exception e) {
            logger.error("service/loan/biz-check  has  exception :{}", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }


    @GetMapping("/contracts")
    @ApiOperation(value = "/contracts", httpMethod = "GET", notes = "换取合同")
    @ApiImplicitParam(name = "productId", value = "产品id", required = true, dataType = "Long")
    @ApiResponse(response = BaseResponse.class, message = "baseResponse", code = 200)
    public BaseResponse<List<ContractDto>> getContracts(Long productId) {
        logger.info("查询合同,参数productId:{}", productId);
        BaseResponse response = new BaseResponse();
        try {
            List<ContractDto> contractDtoList = contractService.selectByProductId(productId);
            return BaseResponse.ok(contractDtoList);
        } catch (Exception e) {
            logger.info("查询合同出错", e);
            response.systemError();
        }
        return response;
    }

    @ApiOperation("get loan data by trace no")
    @GetMapping("/loan-data/{loanTraceNo}")
    public BaseResponse<RiskDto> getRiskData(@PathVariable("loanTraceNo") String loanTraceNo) {
        logger.info("查询逾期统计数据,参数订单 loanTraceNo:{}", loanTraceNo);
        BaseResponse<RiskDto> response = new BaseResponse();
        try {
            Long userId = repaymentPlanService.getUserIdByOrderBizSeqNo(loanTraceNo);
            RiskDto riskDto = repaymentPlanService.getRiskDto(userId);
            response.setData(riskDto);
        } catch (Exception e) {
            logger.info("查询逾期统计数据", e);
            response.systemError();
        }
        return response;
    }

    @ApiOperation("verify payment center order")
    @PostMapping("/verify")
    public BaseResponse<Void> verifyPaymentRequest(@RequestBody VerifyPaymentReq verifyPaymentReq) {
        logger.info("校验还款订单:{}", verifyPaymentReq);
        BaseResponse<Void> response = new BaseResponse();
        try {
            //1. 检查参数
            verifyPaymentReq.paramCheck();

            //2. 业务系统校验订单
            repaymentService.repayVerify(verifyPaymentReq);
        } catch (ServiceException se) {
            logger.info("校验还款订单失败:{}", verifyPaymentReq);
            response.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            response.setMsg(se.getMsg());
        } catch (Exception e) {
            logger.info("校验还款订单异常:{}", verifyPaymentReq);
            response.systemError();
        }
        return response;
    }


}
