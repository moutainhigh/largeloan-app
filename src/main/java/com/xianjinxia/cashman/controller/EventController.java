package com.xianjinxia.cashman.controller;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.dto.AlipayRepaymentDto;
import com.xianjinxia.cashman.dto.ManualAlipayRepaymentDto;
import com.xianjinxia.cashman.dto.SpeedCardPayResultDto;
import com.xianjinxia.cashman.enums.IdempotentEventTypeEnum;
import com.xianjinxia.cashman.enums.PaymentChannelEnum;
import com.xianjinxia.cashman.exceptions.IdempotentException;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.idempotent.IdempotentService;
import com.xianjinxia.cashman.request.*;
import com.xianjinxia.cashman.response.AlipayManualIncomeResponse;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.LoanByMqCallbackDto;
import com.xianjinxia.cashman.response.MQResponse;
import com.xianjinxia.cashman.response.mqapp.ResultMsg;
import com.xianjinxia.cashman.service.*;
import com.xianjinxia.cashman.utils.DateUtil;
import com.xianjinxia.cashman.utils.MoneyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(tags = "cashman_app event controller")
@RequestMapping("/service/event/")
@RestController
public class EventController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private IRepaymentService repaymentService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IRenewalService renewalService;

    @Autowired
    private ILoanOrderService loanOrderService;

    @Autowired
    ICustodyLoanService custodyLoanService;

    @Autowired
    private IdempotentService idempotentService;

    @Autowired
    private IMqMessageService mqMessageService;

    /**
     * 业务系统确认借款成功后的回调处理--添加Order数据和还款计划数据
     *
     * @param trdLoanOrder
     */
    @PostMapping("/trd-confirm-loan-success")
    @ApiOperation(value = "cashman-app confirm loan success process", notes = "确认借款成功后cashman-app回调处理函数")
    public ResultMsg processTradeResult(@RequestBody LoanOrder trdLoanOrder) {
        try {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEventType("payment-tradeCallback");
            eventInfo.setMessage("receive trade app loan success message");
            eventInfo.put("trade loan_order_id", trdLoanOrder.getId());
            CatUtils.info(eventInfo);
            if (trdLoanOrder == null) {
                return paramErrorResp();
            }
            loanOrderService.processConfirmSuccess(trdLoanOrder);
            return successResp();
        } catch (DuplicateKeyException e) {
            logger.error("duplicateKeyException", e);
            return successResp();
        } catch (Exception e) {
            logger.error("processConfirmLoanResult error,url:{}", "/service/event/trd-confirm-loan-success", e);
            return innerErrorResp();
        }
    }


    //public ResultMsg repayCallback(@RequestBody  PayCenterCallbackReq paymentCenterCallbackModel) {
    @PostMapping("/repay-callback")
    public ResultMsg repayCallback(@RequestBody PayCenterCallbackReq payCenterCallbackReq) {
        try {
            EventInfo event = new EventInfo();
            event.put("paymentRequestId", payCenterCallbackReq.getOrderDetailId());
            event.setEventType("repay-callback");
            CatUtils.info(event);

            logger.info("repayCallback支付中心回调参数：{}", JSON.toJSONString(payCenterCallbackReq));

            // 判断支付订单的来源
            if (!payCenterCallbackReq.getRequestSource().equals(Constant.APPLICATION_PAYMENT_SOURCE)) {
                logger.info("支付订单的来源不是重构系统提交的，忽略此订单");
                return successResp();
            }

            repaymentService.repayCallback(payCenterCallbackReq);
            return successResp();
        } catch (Exception e) {
            logger.error("repayCallback支付回调出现异常:", e);
            return innerErrorResp();
        }
    }

    /**
     * 续期支付回调
     *
     * @param payCenterCallbackReq
     * @return
     */
    @PostMapping("/renewal-pay-confirm")
    public ResultMsg renewalPayCallback(@RequestBody PayCenterCallbackReq payCenterCallbackReq) {
        try {
            logger.info("续期费用支付后,支付中心回调参数为：{}", payCenterCallbackReq);
            // 判断支付订单的来源
            if (!payCenterCallbackReq.getRequestSource().equals(Constant.APPLICATION_PAYMENT_SOURCE)) {
                logger.info("支付订单的来源不是重构系统提交的，忽略此订单");
                return successResp();
            }

            EventInfo event = new EventInfo();
            event.setEventType("renewal-pay-confirm");
            event.setMessage("开始");
            CatUtils.info(event);

            renewalService.renewalPayCallback(payCenterCallbackReq);

            event.setMessage("结束");
            CatUtils.info(event);
            return successResp();
        } catch (Exception e) {
            logger.error("续期支付回调出现异常:", e);
            return innerErrorResp();
        }
    }


    /**
     * 业务系统放款成功后的回调处理
     *
     * @param paymentMessage
     */
    @PostMapping("/trd-loan-success")
    @ApiOperation(value = "cashman-app loan success process", notes = "放款成功后cashman-app回调处理函数")
    public ResultMsg processTradeResult(@RequestBody PaymentMessage paymentMessage) {
        try {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEventType("payment-tradeCallback");
            eventInfo.setMessage("receive trade app loan success message");
            eventInfo.put("trade loan_order_id", paymentMessage.getTrdLoanOrderId());
            CatUtils.info(eventInfo);
            if (!paymentMessage.paramCheck()) {
                return paramErrorResp();
            }
            paymentService.processLoanSuccess(paymentMessage);
            return successResp();
        } catch (DuplicateKeyException e) {
            logger.warn("duplicateKeyException", e);
            return successResp();
        } catch (Exception e) {
            logger.error("processTradeResult error,url:{}", "/service/event/trd-loan-success", e);
            return innerErrorResp();
        }
    }


    /**
     * 催收减免
     *
     * @param collectionDeductReq 催收系统减免的请求对象
     * <p>
     *    催收减免规则:
     *       1. 用户已经还清了本金和利息.
     *       2. 仅仅可以减免滞纳金
     *       3. 减免金额仅仅等于代还的滞纳金
     * </p>
     * @return {@link com.xianjinxia.cashman.response.mqapp.ResultMsg} mq result
     */
    @PostMapping("/collection-deduct")
    public ResultMsg collectionSubstractAmount(@RequestBody CollectionDeductReq collectionDeductReq) {
        logger.info("催收减免接受到请求,开始操作催收减免!!!");
        try {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEventType("collection-deduct");
            eventInfo.setMessage("collection-system deduct");
            eventInfo.put("deduct_repayment_plan_id", collectionDeductReq.getRepaymentId());
            CatUtils.info(eventInfo);
            if (!collectionDeductReq.paramCheck()) { return paramErrorResp(); }

            /** 请求数据幂等 */
            idempotentService.idempotentCheck(IdempotentEventTypeEnum.COLLECT_DEDUCT, collectionDeductReq);

            /** 触发催收减免 */
            repaymentService.repayDeduct(collectionDeductReq);

            return successResp();
        } catch (IdempotentException e) {
            logger.info("幂等异常,返回成功,保证MQ不在重试!!!");
            return successResp();
        } catch (Exception e) {
            logger.error("deduct error,url:{}", "/service/event/collection-deduct", e);
            return innerErrorResp();
        }
    }

    /**
     * 催收代扣
     *
     * @param collectWithholdReq 催收代扣请求对象
     * <p>
     *    暂时没有做成job, 目前了解到催收代扣由人工一单单触发
     *    催收代扣业务:
     *      1. 催收系统发起代扣请求
     *      2. 创建支付请求,并设置支付类型为催收代扣【用于路由到催收代扣的回调】
     *      3. 发起支付,等待支付中心回调
     *      4. 路由回调,通知催收系统代扣结果
     *</p>
     */
    @PostMapping("/collection-withhold")
    public ResultMsg collectionWithhold(@RequestBody CollectWithholdReq collectWithholdReq) {
        logger.info("催收代扣接受到请求,开始操作催收代扣!!!");
        try {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setEventType("collection-withhold");
            eventInfo.setMessage("collection-system withhold");
            eventInfo.put("withhold_repayment_plan_id", collectWithholdReq.getRepaymentId());
            CatUtils.info(eventInfo);

            /** 请求参数校验 */
            if (!collectWithholdReq.paramCheck()) {
                return paramErrorResp();
            }

            /** 请求数据幂等 */
            idempotentService.idempotentCheck(IdempotentEventTypeEnum.COLLECT_WITH_HOLD, collectWithholdReq);

            /** 触发催收代扣 */
            repaymentService.repayWithhold(collectWithholdReq);

            return successResp();
        } catch (IdempotentException e) {
            logger.info("幂等异常,返回成功,保证MQ不在重试!!!");
            return successResp();
        } catch (Exception e) {
            logger.error("withhold error,url:{}", "/service/event/collection-withhold", e);
            return innerErrorResp();
        }
    }


    @PostMapping("/custodycallback")
    @ApiOperation(value = "custody-confirm", notes = "存管推单回调接口")
    public BaseResponse custodyLoanCallback(@RequestBody LoanByMqCallbackDto result) {
        logger.info("存管推单mq回调信息为 {}", JSON.toJSONString(result));
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (Constant.APPLICATION_PAYMENT_SOURCE.equals(result.getSourceId())) {
                logger.info("A5存管推单mq回调信息为 {}", JSON.toJSONString(result));
                custodyLoanService.resolveCallback(result);
            }
        } catch (Exception e) {
            baseResponse.systemError();
        }
        return baseResponse;
    }


    @PostMapping("/speed-card-callback")
    public ResultMsg speedCardCallback(@RequestBody SpeedCardPayResultDto speedCardPayResultDto) {
        logger.info("speed-card-callback回调参数为:{}", JSON.toJSONString(speedCardPayResultDto));
        try {
            loanOrderService.updateSpeedCardPayStatus(speedCardPayResultDto);
            return successResp();
        } catch (IdempotentException ie) {
            logger.info("IdempotentException" + speedCardPayResultDto.getOrderId());
            return successResp();
        } catch (Exception e) {
            logger.error("speed-card-callback发生异常," + speedCardPayResultDto.getOrderId(), e);
            return innerErrorResp();
        }
    }

    /**
     * 支付宝还款入账
     *
     * @param alipayRepayRecordList
     * @return
     */
    @PostMapping("/alipay-repay-commit")
    public MQResponse alipayRepayCommit(@RequestBody List<AlipayRepaymentDto> alipayRepayRecordList) {

        MQResponse mqResponse = new MQResponse();
        if (alipayRepayRecordList != null && alipayRepayRecordList.size() > 0) {

            for (AlipayRepaymentDto alipayRepaymentDto : alipayRepayRecordList) {
                try {
                    logger.info(">>>>>>>>>>>>>支付宝还款入账接收到请求,开始操作还款入账：alipayOrderNo= {}, userPhone= {}, merchantNo= {}", alipayRepaymentDto.getAlipayOrderNo(), alipayRepaymentDto.getRemarkPhone(), alipayRepaymentDto.getMerchantNum());

                    //获取订单信息
                    LoanOrder loanOrder = loanOrderService.selectByUserPhone(alipayRepaymentDto.getRemarkPhone(), alipayRepaymentDto.getMerchantNum());

                    if (loanOrder == null) {
                        logger.info("支付宝还款入账，未匹配到正确的借款订单信息：(userPhone= {}, merchantNo= {})", alipayRepaymentDto.getRemarkPhone(), alipayRepaymentDto.getMerchantNum());
                        continue;
                    }
                    logger.info("匹配到的订单号，{}", loanOrder.getTrdLoanOrderId());

                    //放款时间
                    long loanTime = loanOrder.getLoanTime().getTime();
                    long repayTime = DateUtil.yyyyMMddHHmmssToDate(alipayRepaymentDto.getRepayTimeStr()).getTime();
                    if(loanTime > repayTime){
                        logger.info("订单入账时间异常，还款时间不能在放款时间之前，loanTime：{}，repayTime：{}", loanOrder.getLoanTime(), alipayRepaymentDto.getRepayTimeStr());
                        continue;
                    }

                    //入账
                    AlipayRepayIncomeReq cmsRepayReq = new AlipayRepayIncomeReq();
                    cmsRepayReq.setTrdLoanOrderId(loanOrder.getTrdLoanOrderId());
                    cmsRepayReq.setUserId(loanOrder.getUserId());
                    cmsRepayReq.setAmount(MoneyUtil.changeYuanToIntCent(alipayRepaymentDto.getMoneyAmount() == null ? 0 : alipayRepaymentDto.getMoneyAmount()).intValue());//金额转换为分
                    cmsRepayReq.setIncomeAccount(alipayRepaymentDto.getAlipayName());
                    cmsRepayReq.setThirdOrderNo(alipayRepaymentDto.getAlipayOrderNo());
                    cmsRepayReq.setPaymentChannel(PaymentChannelEnum.OTHER_CHANNEL.getName());
                    cmsRepayReq.setRepaymentTime(alipayRepaymentDto.getRepayTimeStr());
                    cmsRepayReq.setRemark(alipayRepaymentDto.getAlipayRemark());
                    repaymentService.repayIncomeByAlipay(cmsRepayReq);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>支付宝还款入账完成：alipayOrderNo= {}, userPhone= {}, merchantNo= {}", alipayRepaymentDto.getAlipayOrderNo(), alipayRepaymentDto.getRemarkPhone(), alipayRepaymentDto.getMerchantNum());

                } catch (IdempotentException e) {
                    logger.info("支付宝还款入账，幂等异常,返回成功,保证MQ不再重试!!!");
                } catch (ServiceException se) {
                    mqResponse.setCode(MQResponse.Code.ERROR.getCode());
                    logger.error("支付宝还款入账请求提交失败: ", se.getMsg());
                } catch (Exception e) {
                    mqResponse.setCode(MQResponse.Code.ERROR.getCode());
                    logger.error("支付宝还款入账请求提交异常", e.getMessage());
                }
            }

        }

        return mqResponse;
    }

    /**
     * 支付宝人工入账
     * @param alipayRepayIncomeDto
     * @return
     */
    @ApiOperation("repay commit by customer, void return")
    @PostMapping("/manual-repay-income")
    public BaseResponse<AlipayManualIncomeResponse> repayIncome(@RequestBody ManualAlipayRepaymentDto alipayRepayIncomeDto) {
        BaseResponse<AlipayManualIncomeResponse> baseResponse = new BaseResponse<>();
        try {

            logger.info(">>>>>支付宝人工入账开始, alipayOrderNo= {}, trdLoanOrderId= {}, amount= {}", alipayRepayIncomeDto.getAlipayOrderNo(), alipayRepayIncomeDto.getAssetOrderId(), alipayRepayIncomeDto.getMoneyAmount());

            //1. 检查参数
            alipayRepayIncomeDto.paramCheck();

            //2. 创建还款请求
            AlipayRepayIncomeReq alipayRepayIncomeReq = new AlipayRepayIncomeReq();
            alipayRepayIncomeReq.setAmount(MoneyUtil.changeYuanToIntCent(alipayRepayIncomeDto.getMoneyAmount()));//金额元转分
            alipayRepayIncomeReq.setPaymentChannel(PaymentChannelEnum.OTHER_CHANNEL.getName());
            alipayRepayIncomeReq.setRepaymentTime(alipayRepayIncomeDto.getRepayTimeStr());
            alipayRepayIncomeReq.setThirdOrderNo(alipayRepayIncomeDto.getAlipayOrderNo());
            alipayRepayIncomeReq.setRemark(alipayRepayIncomeDto.getAlipayRemark());
            alipayRepayIncomeReq.setIncomeAccount(alipayRepayIncomeDto.getAlipayName());
            alipayRepayIncomeReq.setTrdLoanOrderId(Long.valueOf(alipayRepayIncomeDto.getAssetOrderId()));
            repaymentService.repayIncomeByAlipayManual(alipayRepayIncomeReq);

            baseResponse.setData(new AlipayManualIncomeResponse(DateUtil.yyyyMMddHHmmss(new Date())));

        } catch (ServiceException se) {
            logger.error("还款请求提交失败:", se);
            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            baseResponse.setMsg(se.getMsg());
        } catch (IdempotentException e) {
            logger.error("支付宝还款入账，幂等异常!!!");
            baseResponse.setCode(BaseResponse.ResponseCode.SYS_ERROR.getValue());
            baseResponse.setMsg("已处理过相同的支付宝流水号和订单号，不能重复入账！");
        } catch (Exception e) {
            logger.error("还款请求提交异常", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }

}
