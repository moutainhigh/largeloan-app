package com.xianjinxia.cashman.controller;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.common.EventInfo;
import com.xianjinxia.cashman.exceptions.IdempotentException;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.domain.contract.DepositAgreementContract;
import com.xianjinxia.cashman.domain.contract.LargeLoanAgreementContract;
import com.xianjinxia.cashman.domain.contract.PlatformAgreementContract;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.dto.LoanConfigModalDto;
import com.xianjinxia.cashman.dto.LoanUsageDto;
import com.xianjinxia.cashman.request.ContractReq;
import com.xianjinxia.cashman.request.ContractUrlReq;
import com.xianjinxia.cashman.request.GroupRiskResultReq;
import com.xianjinxia.cashman.request.LoanCalculateRequest;
import com.xianjinxia.cashman.request.LoanCheckRequest;
import com.xianjinxia.cashman.response.*;
import com.xianjinxia.cashman.response.mqapp.ResultMsg;
import com.xianjinxia.cashman.service.IContractService;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.service.ILoanService;
import com.xianjinxia.cashman.service.ILoanUsageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author fanmaowen
 *
 */
@Api(tags = "cashman_app loan controller")
@RequestMapping("/service/loan/")
@RestController
public class LoanController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private ILoanService loanService;

    @Autowired
    private IContractService contractService;

    @Autowired
    private ILoanUsageService loanUsageService;

    @Autowired
    private ILoanOrderService loanOrderService;



    @ApiOperation(value = "loan check", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 借款")
    @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "LoanCheckRequest")
    @ApiResponse(response = BaseResponse.class, message = "response", code = 200)
    @PostMapping(value = "biz-check")
    public BaseResponse<LoanCheckResponse> loanBizCheck(@RequestBody LoanCheckRequest loanCheckRequest) {
        BaseResponse<LoanCheckResponse> baseResponse = new BaseResponse<>();
        if (loanCheckRequest.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", loanCheckRequest.getUserId());
            event.setEventType("loan-check");
            CatUtils.info(event);
            try {
                baseResponse = loanService.loanBizCheck(loanCheckRequest);
            } catch (Exception e) {
                logger.error("service/loan/biz-check  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }
    /**
     * 动态计算借款费用明细
     * 
     * @param loanConfigModalDto
     * @return
     */
    @ApiOperation("loan dynamic fee calculate")
    @PostMapping(value = "loan-dynamic-fee-calculate")
    public BaseResponse<LoanFeeDetails> loanDynamicFeeCalculate(@RequestBody LoanConfigModalDto loanConfigModalDto) {
        BaseResponse<LoanFeeDetails> baseResponse = new BaseResponse<>();
        try {
            // cat日志收集
            EventInfo eventInfo = new EventInfo();
            eventInfo.put("productId", loanConfigModalDto.getProductId());
            eventInfo.setEventType("loan-dynamic-fee-calculate");
            CatUtils.info(eventInfo);
            // 参数校验
            if (!loanConfigModalDto.paramCheck(baseResponse)) {
                return baseResponse;
            }
            baseResponse.setData(loanService.loanDynamicFeeCal(loanConfigModalDto));
            logger.info("loanDynamicFeeCalculate response：{}", JSON.toJSONString(baseResponse.getData()));
        } catch (Exception e) {
            baseResponse.systemError();
            logger.error("http error at /service/loan/loan-dynamic-fee-calculate :{}", e);
        }
        return baseResponse;

    }

    /**
     * 动态预计算还款计划
     * 
     * @param loanConfigModalDto
     * @return
     */
    @ApiOperation("loan dynamic repaymentplan calculate")
    @PostMapping(value = "loan-dynamic-repaymentplan-calculate")
    public BaseResponse<List<RepaymentPlanAdvance>> loanDynamicRepaymentPlanCalculate(
            @RequestBody LoanConfigModalDto loanConfigModalDto) {
        BaseResponse<List<RepaymentPlanAdvance>> baseResponse =
                new BaseResponse<>();
        try {
            // 日志收集
            EventInfo eventInfo = new EventInfo();
            eventInfo.put("productId", loanConfigModalDto.getProductId());
            eventInfo.setEventType("loan-dynamic-repaymentplan-calculate");
            CatUtils.info(eventInfo);
            // 参数校验
            if (!loanConfigModalDto.paramCheck(baseResponse)) {
                return baseResponse;
            }

            baseResponse.setData(loanService.loanDynamicRepaymentPlanCal(loanConfigModalDto));
            logger.info("loanDynamicRepaymentPlanCalculate response", baseResponse.getData());
        } catch (Exception e) {
            baseResponse.systemError();
            logger.error("http error at /service/loan/loan-dynamic-repaymentplan-calculate :{}", e);
        }
        return baseResponse;

    }

    @ApiOperation(value = "contract-bigloan_cashman_app", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取大额借款协议数据")
    @ApiImplicitParam(name = "contractReq", value = "contractReq", required = true,
            dataType = "ContractReq")
    @ApiResponse(response = BaseResponse.class, message = "response", code = 200)
    @PostMapping(value = "contract-bigloan")
    public BaseResponse<LargeLoanAgreementContract> getBigAmountContractParams(@RequestBody ContractReq contractReq) {
        BaseResponse<LargeLoanAgreementContract> baseResponse = new BaseResponse<>();
        if (contractReq.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", contractReq.getUserId());
            event.setEventType("contract-bigAmount_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData( contractService.getBigAmountContractParams(contractReq));
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/contract-bigAmount  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "contract-platform_cashman_app", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取平台协议数据")
    @ApiImplicitParam(name = "contractReq", value = "contractReq", required = true,
            dataType = "ContractReq")
    @ApiResponse(response = BaseResponse.class, message = "response", code = 200)
    @PostMapping(value = "contract-platform")
    public BaseResponse<PlatformAgreementContract> getPlatformContractParams(@RequestBody ContractReq contractReq) {
        BaseResponse<PlatformAgreementContract> baseResponse = new BaseResponse<>();
        if (contractReq.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", contractReq.getUserId());
            event.setEventType("contract-platform_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData(contractService.getPlatformContractParams(contractReq));
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/contract-platform  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "contract-bigloan-url_cashman_app", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取大额借款协议数据")
    @ApiImplicitParam(name = "contractReq", value = "contractReq", required = true,
            dataType = "ContractReq")
    @ApiResponse(response = BaseResponse.class, message = "response", code = 200)
    @PostMapping(value = "contract-bigloan-url")
    public BaseResponse<String> getBigAmountContractUrl(@RequestBody ContractReq contractReq) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        if (contractReq.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", contractReq.getUserId());
            event.setEventType("contract-bigAmount-url_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData( contractService.getLoanContractUrl(contractReq.getContractType(),contractReq.getTrdLoanOrderId()));
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/contract-bigAmount-url  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "contract-platform-url_cashman_app", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取平台协议数据")
    @ApiImplicitParam(name = "contractReq", value = "contractReq", required = true,
            dataType = "ContractReq")
    @ApiResponse(response = BaseResponse.class, message = "response", code = 200)
    @PostMapping(value = "contract-platform-url")
    public BaseResponse<String> getPlatformContractUrl(@RequestBody ContractReq contractReq) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        if (contractReq.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", contractReq.getUserId());
            event.setEventType("contract-platform-url_cashman_app");
            CatUtils.info(event);
            try {
                String url =contractService.getPlatformContractUrl(contractReq.getContractType(),contractReq.getTrdLoanOrderId());
                if(StringUtils.isBlank(url)){
                    baseResponse.setCode(ResultMsg.RESULT_FAIL);
                    baseResponse.setMsg("no url");
                }
                baseResponse.setData(url);
            } catch (Exception e) {
                logger.error("service/loan/contract-platform-url  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "contract-list_cashman_app", httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取协议相关信息")
    @GetMapping(value = "contract-list")
    public BaseResponse<List<ContractDto>> getContractList(@RequestParam("trdLoanOrderId") Long trdLoanOrderId) {
        BaseResponse<List<ContractDto>> baseResponse = new BaseResponse<>();
        if (null!=trdLoanOrderId) {
            EventInfo event = new EventInfo();
            event.put("trdLoanOrderId", trdLoanOrderId);
            event.setEventType("contract-list_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData(contractService.getContracts(trdLoanOrderId));
            } catch (Exception e) {
                logger.error("service/loan/contract-list  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "uasge-list_cashman_app", httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "uasge-list 获取借款用途列表信息")
    @GetMapping(value = "uasge-list")
    public BaseResponse<List<LoanUsageDto>> getContractList() {
        BaseResponse<List<LoanUsageDto>> baseResponse = new BaseResponse<>();

            EventInfo event = new EventInfo();
            event.setEventType("uasge-list_cashman_app");
            CatUtils.info(event);
            try {
                List<LoanUsageDto> loanUsageDtoList = loanUsageService.selectAllUsages();
                baseResponse.setData(CollectionUtils.isEmpty(loanUsageDtoList)? Collections.EMPTY_LIST:loanUsageDtoList);
            } catch (Exception e) {
                logger.error("service/loan/contract-list  has  exception :{}", e);
                baseResponse.systemError();
            }

        return baseResponse;
    }

    @ApiOperation(value = "contract-url-list_cashman_app", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取协议相关信息")
    @PostMapping(value = "contract-url-list")
    public BaseResponse<List<ContractDto>> getContracturlList(@RequestBody ContractUrlReq contractReq) {
        BaseResponse<List<ContractDto>> baseResponse = new BaseResponse<>();
        if (contractReq.paramCheck(baseResponse)) {
            EventInfo event = new EventInfo();
            event.put("userId", contractReq.getUserId());
            event.setEventType("contract-platform-url_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData(contractService.getContractUrlForDetail(contractReq));
            } catch (Exception e) {
                logger.error("service/loan/contract-platform-url  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    /**
     * 集团风控结果回调通知
     * @param groupRiskResultReq
     * @return
     */
    @ApiOperation("risk callback")
    @PostMapping("risk-callback")
    public OpenApiBaseResponse<Object> riskCallback(@RequestBody GroupRiskResultReq groupRiskResultReq){
        logger.info("risk-callback入参为:{}",JSON.toJSONString(groupRiskResultReq));
        OpenApiBaseResponse<Object> response = new OpenApiBaseResponse<>();
        try {
            loanOrderService.handleGroupRiskCallbackResult(groupRiskResultReq);
        }catch(IdempotentException ide){
            logger.error("此订单号已经处理过，订单号为:{}",groupRiskResultReq.getOrder_id());
            return response;
        }catch(ServiceException se){
            response = new OpenApiBaseResponse();
            response.setCode(se.getCode());
            response.setMsg(se.getMsg());
        }catch(Exception e){
            response.systemError();
        }
        return response;
    }

    @ApiOperation(value = "contract-deposit_cashman_app", httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取平台协议数据")
    @GetMapping(value = "contract-deposit")
    public BaseResponse<DepositAgreementContract> getDepositContractParams(Long trdLoanOrderId) {
        BaseResponse<DepositAgreementContract> baseResponse = new BaseResponse<>();
        if (null!=trdLoanOrderId) {
            EventInfo event = new EventInfo();
            event.put("trdLoanOrderId",trdLoanOrderId);
            event.setEventType("contract-deposit_cashman_app");
            CatUtils.info(event);
            try {
                baseResponse.setData(contractService.getDepositContractParams(trdLoanOrderId));
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/contract-deposit  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }


    /**
     * 动态计算借款费用明细(批量试算)
     *
     * @param loanCalculateRequest
     * @return
     */
    @ApiOperation("loan dynamic fee calculate")
    @PostMapping(value = "loan-dynamic-fee-batch-calculate")
    public BaseResponse<List<LoanFeeDetails>> loanDynamicFeeBatchCalculate(@RequestBody LoanCalculateRequest loanCalculateRequest) {
        BaseResponse<List<LoanFeeDetails>> baseResponse = new BaseResponse<>();
        try {
            // cat日志收集
            EventInfo eventInfo = new EventInfo();
            eventInfo.put("productId", loanCalculateRequest.getProductId());
            eventInfo.setEventType("loan-dynamic-fee-calculate");
            CatUtils.info(eventInfo);
            // 参数校验
            if (!loanCalculateRequest.paramCheck(baseResponse)) {
                return baseResponse;
            }
            baseResponse.setData(loanService.loanDynamicFeeBatchCal(loanCalculateRequest));
            logger.info("loan dynamic fee batch calculate response：{}", JSON.toJSONString(baseResponse.getData()));
        } catch (Exception e) {
            baseResponse.systemError();
            logger.error("http error at /service/loan/loan-dynamic-fee-batch calculate :{}", e);
        }
        return baseResponse;

    }

    @ApiOperation(value = "speedcard-repay-status", httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取订单提速卡是否需要重新支付")
    @GetMapping(value = "speedcard-repay-status")
    public BaseResponse<SpeedCardRepayRsp> getSpeedRepayStatus(Long loanOrderId) {
        BaseResponse<SpeedCardRepayRsp> baseResponse = new BaseResponse<SpeedCardRepayRsp>();
        if (null!=loanOrderId) {
            EventInfo event = new EventInfo();
            event.put("trdOrderId",loanOrderId);
            event.setEventType("speedcard-repay-status");
            CatUtils.info(event);
            try {
                baseResponse.setData(loanOrderService.getSpeedRepayStatus(loanOrderId));//speedcard-repay-status
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/speedcard-repay-status  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }

    @ApiOperation(value = "get-overdueamt-by-loanid", httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "cashman_app 获取订单逾期费用")
    @GetMapping(value = "get-overdueamt-by-loanid")
    public BaseResponse<Integer> getOverdueAmtByLoanId(Long loanOrderId) {
        BaseResponse<Integer> baseResponse = new BaseResponse<Integer>();
        if (null!=loanOrderId) {
            EventInfo event = new EventInfo();
            event.put("trdOrderId",loanOrderId);
            event.setEventType("get-overdueamt-by-loanid");
            CatUtils.info(event);
            try {
                baseResponse.setData(loanOrderService.getOverdueAmtByLoanId(loanOrderId));
                if(null==baseResponse.getData()){
                    baseResponse.systemError();
                }
            } catch (Exception e) {
                logger.error("service/loan/get-overdueamt-by-loanid  has  exception :{}", e);
                baseResponse.systemError();
            }
        }
        return baseResponse;
    }
}
