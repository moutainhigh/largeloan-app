package com.xianjinxia.cashman.controller;

import com.xianjinxia.cashman.enums.ProductCategoryEnum;
import com.xianjinxia.cashman.exceptions.IdempotentException;
import com.xianjinxia.cashman.request.SyncLoanOrderReq;
import com.xianjinxia.cashman.response.BaseResponse;
import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.service.ILoanOrderService;
import com.xianjinxia.cashman.request.SyncShoppingLoanOrderDeliverRequest;

/**
 * 商城还款计划新增接口,订单发货时更新还款计划时间
 * @author chunliny
 *
 */
@Api(tags = "cashman_app shopping repayment plan controller")
@RestController
@RequestMapping("/service/shopping")
public class ShoppingController extends BaseController {
	
    private static final Logger logger = LoggerFactory.getLogger(ShoppingController.class);
    
    @Autowired
    private ILoanOrderService loanOrderService;
    
	/**
	 * 接收trade-app的MQ，同步更新还款计划时间
	 *
	 * @param syncShoppingLoanOrderDeliverRequest
	 *
	 * @return
	 */
	@PostMapping("/sync-repayment-plan-time")
	public BaseResponse<Void> syncRepaymentPlanTime(@RequestBody SyncShoppingLoanOrderDeliverRequest syncShoppingLoanOrderDeliverRequest) {
		BaseResponse<Void> response = new BaseResponse<>();
		logger.info("请求交易参数:" + syncShoppingLoanOrderDeliverRequest);
		try {
			if (!syncShoppingLoanOrderDeliverRequest.paramCheck(response)) {
				return response;
			}
			EventInfo event = new EventInfo();
			event.put("trdLoanOrderId", syncShoppingLoanOrderDeliverRequest.getTrdLoanOrderId());
			event.put("deliver-time", syncShoppingLoanOrderDeliverRequest.getPaymentPlanTime());
			event.setEventType("cashman_shopping-sync-repayment-plan-time");
			CatUtils.info(event);
			loanOrderService.updateListRepaymentPlanTime(syncShoppingLoanOrderDeliverRequest);
		} catch (IdempotentException e) {
			logger.error("sync-repayment-plan-time "+syncShoppingLoanOrderDeliverRequest.getTrdLoanOrderId(),e);
			return response;
        }  catch (Exception e) {
			logger.error("service/shopping/sync-repayment-plan-time  has  exception :{}", e);
			response.systemError();
		}
		return response;
	}

	@PostMapping(value = "/sync-virtual-order-status")
	public BaseResponse<Void> syncOrderStatus(@RequestBody SyncLoanOrderReq syncLoanOrderReq){
		logger.info("sync-order-status入参为:{}",syncLoanOrderReq);
		BaseResponse<Void> response = new BaseResponse<>();
		try {
			EventInfo eventInfo = new EventInfo();
			eventInfo.put("trdLoanOrderId", syncLoanOrderReq.getLoanOrderId());
			eventInfo.put("status", syncLoanOrderReq.getStatus());
			eventInfo.put("productCategory", syncLoanOrderReq.getProductCategory());
			eventInfo.setEventType("sync-order-status-to-cashman");
			CatUtils.info(eventInfo);
			if(!syncLoanOrderReq.paramCheck()){
				return response;
			}
			if (syncLoanOrderReq.getProductCategory().intValue() == ProductCategoryEnum.PRODUCT_CATEGORY_SHOPPING.getCode()){
				loanOrderService.syncLoanOrderStatus(syncLoanOrderReq);
			}
		} catch (IdempotentException e) {
			logger.error(e.getMessage(),e);
			response.systemError();
		}catch(Exception e){
			logger.error("syncOrderStatus error",e);
			response.systemError();

		}
		return response;
	}
}
