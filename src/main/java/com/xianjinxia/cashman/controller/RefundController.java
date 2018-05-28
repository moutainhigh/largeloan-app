package com.xianjinxia.cashman.controller;


import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.request.RefundReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.repay.IRefundService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 还款的restful接口
 *
 * @author zyj@xianjinxia.com
 */
@Api(tags = "cashman_app repay controller")
@RestController
@RequestMapping("/service/refund")
public class RefundController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    private IRefundService refundService;


    @PostMapping("/refund-cms")
    public BaseResponse<Void> refund(@RequestBody RefundReq refundReq) {
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        try {
            EventInfo event = new EventInfo();
            event.put("refundAmt", refundReq.getRefundAmt());
            event.put("repaymentRecordId", refundReq.getRepaymentRecordId());
            event.setEventType("rrefund-cms");
            CatUtils.info(event);

            refundReq.paramCheck();
            refundService.refund(refundReq.getUserPhone(), refundReq.getRepaymentRecordId(),refundReq.getRefundAmt(),refundReq.getRefundTime(),refundReq.getRefundChannel(),refundReq.getRefundOrderNo());
        } catch (ServiceException se) {
            logger.error("退款请求提交失败:", se);
            baseResponse.setCode(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue());
            baseResponse.setMsg(se.getMsg());
        } catch (Exception e) {
            logger.error("退款请求提交异常", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }
}
