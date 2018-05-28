package com.xianjinxia.cashman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.dto.RenewalFeeInfoDto;
import com.xianjinxia.cashman.request.RenewalConfirmReq;
import com.xianjinxia.cashman.request.RenewalReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.IRenewalService;
import com.xianjinxia.cashman.service.IRepaymentService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/service/renewal")
public class RenewalController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(RenewalController.class);
	
	@Autowired
	private IRenewalService renewalService;
	
	@Autowired
	private IRepaymentService repaymentService;
	
	@Autowired
	private IProductsService productsService;
	
	/**
	 * 获取续期费用信息
	 * @param renewalReq
	 * @return
	 */
	@ApiOperation("get renewal info")
	@PostMapping("renewal-info")
	@ResponseBody
	public BaseResponse<RenewalFeeInfoDto> renewalInfo(@RequestBody RenewalReq renewalReq) {
		logger.info("请求参数为{}", renewalReq);
		BaseResponse<RenewalFeeInfoDto> response = new BaseResponse<RenewalFeeInfoDto>();
		if(renewalReq.paramCheck(response)) {
			try {
				RepaymentPlan repaymentPlan = repaymentService.getRepaymentOrderById(String.valueOf(renewalReq.getRepaymentId()));
				Products products = productsService.getById(repaymentPlan.getProductId());
				//校验能否续期
				renewalService.checkCanRenewal(renewalReq.getUserId(), repaymentPlan, products);
				//计算续期数据
				RenewalFeeInfoDto renewalFeeInfoDto = renewalService.calculateRenewalFee(repaymentPlan, products);
				response.setData(renewalFeeInfoDto);
			}catch(Exception e) {
				logger.error("get renewal info exception", e);
				response.systemError();
			}
		}
        return response;
	}
	
	
	/**
	 * 确认续期
	 * @param renewalConfirmReq
	 * @return
	 */
	@ApiOperation("confirm renewal")
	@PostMapping("confirm-renewal")
	@ResponseBody
	public BaseResponse<PaymentParamDto> confirmRenewal(@RequestBody RenewalConfirmReq renewalConfirmReq) {
		BaseResponse<PaymentParamDto> response = new BaseResponse<PaymentParamDto>();
		if(renewalConfirmReq.paramCheck(response)) {
			try {
				RepaymentPlan repaymentPlan = repaymentService.getRepaymentOrderById(String.valueOf(renewalConfirmReq.getRepaymentId()));
				Products products = productsService.getById(repaymentPlan.getProductId());
				//校验能否续期
				//renewalService.checkCanRenewal(renewalConfirmReq.getUserId(), repaymentPlan, products);
				
				//计算续期数据
				RenewalFeeInfoDto renewalFeeInfoDto = renewalService.calculateRenewalFee(repaymentPlan, products);
				
				//发起支付请求
				PaymentParamDto paymentParamDto = renewalService.repayRenewalFee(repaymentPlan, renewalFeeInfoDto);
				response.setData(paymentParamDto);
			}catch(DuplicateKeyException e) {
				logger.error("重复插入", e);
				return response;
			}catch(Exception e) {
				logger.error("confirm renewal exception", e);
				response.systemError();
			}
		}
		return response;
	}
	
}
