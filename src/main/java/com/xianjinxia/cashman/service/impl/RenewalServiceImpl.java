package com.xianjinxia.cashman.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.xianjinxia.cashman.enums.PaymentRequestStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.conf.ExtProperties;
import com.xianjinxia.cashman.domain.LoanEventControl;
import com.xianjinxia.cashman.domain.PaymentRequest;
import com.xianjinxia.cashman.domain.PaymentRequestConfig;
import com.xianjinxia.cashman.domain.Products;
import com.xianjinxia.cashman.domain.RenewalOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.PaymentParamDto;
import com.xianjinxia.cashman.dto.RenewalFeeInfoDto;
import com.xianjinxia.cashman.enums.AbleRenewalEnum;
import com.xianjinxia.cashman.enums.EventControlTypeEnum;
import com.xianjinxia.cashman.enums.PaymentBizTypeEnum;
import com.xianjinxia.cashman.enums.ProductTermTypeEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanStatusEnum;
import com.xianjinxia.cashman.enums.RenewalOrderStatusEnum;
import com.xianjinxia.cashman.mapper.LoanEventControlMapper;
import com.xianjinxia.cashman.mapper.PaymentRequestConfigMapper;
import com.xianjinxia.cashman.mapper.PaymentRequestMapper;
import com.xianjinxia.cashman.mapper.RenewalOrderMapper;
import com.xianjinxia.cashman.remote.OldCashmanRemoteService;
import com.xianjinxia.cashman.request.PayCenterCallbackReq;
import com.xianjinxia.cashman.request.PaymentCenterCallbackRequestCode;
import com.xianjinxia.cashman.request.UserCouponReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.IRenewalService;
import com.xianjinxia.cashman.service.repay.paycenter.IPayCenterService;
import com.xianjinxia.cashman.service.repay.IPaymentRequestService;
import com.xianjinxia.cashman.service.repay.IRepaymentPlanService;
import com.xianjinxia.cashman.utils.DateUtil;

@Service
public class RenewalServiceImpl implements IRenewalService {
	
	private static final Logger logger = LoggerFactory.getLogger(RenewalServiceImpl.class);
	
	@Autowired
	private RenewalOrderMapper	renewalOrderMapper;

	@Autowired
	private ExtProperties		extProperties;
	
	@Autowired
	private IPaymentRequestService paymentRequestService;
	
	@Autowired
	private OldCashmanRemoteService oldCashmanRemoteService;
	
	@Autowired
	private PaymentRequestMapper paymentRequestMapper;
	
	@Autowired
	private IRepaymentPlanService repaymentPlanService;

	@Autowired
	private IPayCenterService payCenterService;

	@Autowired
	private PaymentRequestConfigMapper paymentRequestConfigMapper;

	@Autowired
	private IProductsService productsService;
	
	@Autowired
	private LoanEventControlMapper loanEventControlMapper;
	
	/**
	 * 计算续费费用信息
	 */
	@Override
	public RenewalFeeInfoDto calculateRenewalFee(RepaymentPlan repaymentPlan, Products products) {

		//待还总金额(续期金额)
		Integer renewalAmount =  repaymentPlan.getRepaymentTotalAmount() + repaymentPlan.getRepaymentWaitingAmount();

		//续期次数	
		Integer renewalCount = repaymentPlan.getRenewalCount();

		//续期利息
		Integer renewalInterest = 0;//new BigDecimal(renewalAmount).multiply(products.getInterestRate()).intValue();

		// 固定续期手续费 20元+（5次之后*10元）
		Integer renewalFee = Integer.parseInt(extProperties.getLoanConfig().getRenewalFee());//续期手续费
		if (renewalCount >= 5) {
			renewalFee = renewalFee + ((renewalCount - 4) * 1000);
		} 

		// 总续期费用(续期手续费+续期利息)
		Integer totalRenewalFee = renewalFee + renewalInterest;

		RenewalFeeInfoDto renewalFeeInfoDto = new RenewalFeeInfoDto();
		renewalFeeInfoDto.setRenewalAmount(new BigDecimal(renewalAmount).divide(new BigDecimal(100)).intValue());//续期总金额
		renewalFeeInfoDto.setRenewalFee(new BigDecimal(renewalFee).divide(new BigDecimal(100)).intValue());//续期手续费
		renewalFeeInfoDto.setRenewalInterest(new BigDecimal(renewalInterest).divide(new BigDecimal(100)).intValue());//续期利息费用
		renewalFeeInfoDto.setTotalRenewalFee(new BigDecimal(totalRenewalFee).divide(new BigDecimal(100)).intValue());//续期总费用
		renewalFeeInfoDto.setTerm(products.getTerm());
		renewalFeeInfoDto.setRepaymentTime(repaymentPlan.getRepaymentPlanTime());//应还时间
		renewalFeeInfoDto.setNowApplyCount(renewalCount);
		renewalFeeInfoDto.setNextRepayDay(nextRepayDay(repaymentPlan,products));//续期后应还时间
		
        //获取能够使用的优惠券数目
		UserCouponReq userCouponReq = new UserCouponReq();
		userCouponReq.setUserId(repaymentPlan.getUserId());
		userCouponReq.setRepaymentTime(renewalFeeInfoDto.getRepaymentTime().getTime());
		userCouponReq.setLoanTerm(renewalFeeInfoDto.getTerm());
		Integer usebleCouponSize = getUsebleCouponSize(userCouponReq);
		renewalFeeInfoDto.setUsebleCoupon(usebleCouponSize);
		
		return renewalFeeInfoDto;
	}


	@Override
	@Transactional
	public PaymentParamDto repayRenewalFee(RepaymentPlan repaymentPlan, RenewalFeeInfoDto renewalFeeInfo) {
		//创建支付请求payment_request
		PaymentRequestConfig paymentRequestConfig = paymentRequestConfigMapper.selectOne();
		Date currentTime = new Date();
		Date expiredTime = DateUtil.minutesAfter(currentTime, paymentRequestConfig.getExpireMinutes());
		PaymentRequest paymentRequest = paymentRequestService.createPaymentRequest(repaymentPlan.getUserId(), renewalFeeInfo.getTotalRenewalFee(), PaymentRequestStatusEnum.NEW, PaymentBizTypeEnum.RENEWAL);
		
        //落续期订单表
		RenewalOrder renewalOrder = new RenewalOrder();
		renewalOrder.setRepaymentPlanId(repaymentPlan.getId());
		renewalOrder.setPaymentRequestId(paymentRequest.getId());
		renewalOrder.setLoanOrderId(repaymentPlan.getLoanOrderId());
		renewalOrder.setUserId(repaymentPlan.getUserId());
		renewalOrder.setRenewalFee(renewalFeeInfo.getRenewalFee()*100);//续期手续费用
		renewalOrder.setStatus(RenewalOrderStatusEnum.PAYING.getCode());
		renewalOrder.setPreRepaymentTime(repaymentPlan.getRepaymentPlanTime());//应还款时间
		renewalOrder.setRenewalRepaymentTime(renewalFeeInfo.getNextRepayDay());//续期后应还款时间
		renewalOrder.setRenewalAmount(renewalFeeInfo.getRenewalAmount()*100);//续期总金额
		renewalOrder.setInterestAmount(renewalFeeInfo.getRenewalInterest()*100);//续期利息费用
		renewalOrder.setRemark("续期");
		renewalOrder.setRenewalUnique(String.valueOf(RenewalOrderStatusEnum.PAYING.getCode()));
		renewalOrderMapper.insert(renewalOrder);
		
		//防止续期和还款接口并发操作，通过联合唯一键约束
		LoanEventControl loanEventControl = new LoanEventControl();
		loanEventControl.setEventId(repaymentPlan.getId());
		loanEventControl.setEventType(EventControlTypeEnum.REPAYMENT_PLAN.getValue());
		loanEventControl.setUkToken(0L);
		loanEventControl.setStatus("NEW");
		loanEventControl.setBusinessId(paymentRequest.getId());
		loanEventControl.setBusinessType(PaymentBizTypeEnum.RENEWAL.getCode());
		loanEventControl.setRemark("续期");
		loanEventControlMapper.insert(loanEventControl);
				

		//返回客户端结果
        PaymentParamDto paymentParamDto = null;//payCenterService.payWithH5(paymentRequestConfig.getMd5Salt(), repaymentPlan.getUserId().intValue(),paymentRequest.getId(), paymentRequest.getAmount(),null,null, JSON.toJSONString(paymentRequest), PaymentBizTypeEnum.RENEWAL.getCode(), Constant.APPLICATION_PAYMENT_SOURCE, expiredTime, null , null, null, null);
        return paymentParamDto;
	}
	
	@Override
    public Integer getUsebleCouponSize(UserCouponReq userCouponReq) {
    	return oldCashmanRemoteService.getUsebleCouponSize(userCouponReq);
	}
	
	
	@Override
	public int updateRenewalOrder(RenewalOrder renewalOrder) {
		return renewalOrderMapper.update(renewalOrder);
	}
	
	
	@Override
	public int countRenewalOrder(RenewalOrder renewalOrder) {
		return renewalOrderMapper.countRenewalOrder(renewalOrder);
	}
	
	


	@Override
	public void checkCanRenewal(Long userId, RepaymentPlan repaymentPlan, Products products) {
		if(repaymentPlan == null || products == null) {
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "参数不合法.");
		}
		if(userId.longValue()!= repaymentPlan.getUserId().longValue()) {
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "参数不合法.");
		}
		if(repaymentPlan.getIsOverdue()) {//已逾期
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "已逾期，不能续期");
		}
		if(repaymentPlan.getRepaymentWaitingAmount()>0) {//在途金额不为0
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "有处于支付中的订单，请稍后重试");
		}
		if(repaymentPlan.getStatus().intValue() != RepaymentPlanStatusEnum.Waiting.getCode()) {
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "此状态不能续期");
		}
		if(products.getIsRenewal() == AbleRenewalEnum.DISABLE_RENEWAL.getCode()) {//此产品是否支持续期
			throw new ServiceException(BaseResponse.ResponseCode.BIZ_CHECK_FAIL.getValue(), "此产品不支持续期");
		}
	}

	/**
	 * 计算续期后下一次还款时间
	 * @param repaymentPlan
	 * @param products
	 * @return
	 */
	private Date nextRepayDay(RepaymentPlan repaymentPlan,Products products) {
		Date repaymentTime = null;
		if (products.getTermType().equals(ProductTermTypeEnum.MONTH.getCode())) {
            repaymentTime = DateUtil.addMonth(repaymentPlan.getRepaymentPlanTime(), products.getTerm());
        }
        if (products.getTermType().equals(ProductTermTypeEnum.DAY.getCode())) {
            repaymentTime = DateUtil.addDay(repaymentPlan.getRepaymentPlanTime(), products.getTerm());
        }
        return repaymentTime;
	}
	
	
	/**
	 * 续期支付回调
	 * @param payCenterCallbackReq
	 * @return
	 */
	@Transactional
	public void renewalPayCallback(PayCenterCallbackReq payCenterCallbackReq) {
		if(payCenterCallbackReq == null) {
			throw new ServiceException("续期支付中心回调请求参数为空");
		}
		Long paymentRequestId = new Long(payCenterCallbackReq.getOrderDetailId());
        PaymentRequest paymentRequest = paymentRequestMapper.selectByPrimaryKey(paymentRequestId);
        if (paymentRequest == null) {
            throw new ServiceException("续期支付请求未找到");
        }
        // 续期支付回调已被处理过, 不做任何处理, 记录log
        if (paymentRequest.getStatus().intValue() != PaymentRequestStatusEnum.NEW.getCode()) {
            logger.info("续期支付请求回调[{}]已经被处理过[{}]", paymentRequest.getId(), paymentRequest.getRespTime());
            return;
        }
        String payCenterResponseId = payCenterCallbackReq.getOrderNo();
        String payCenterResponseMsg = JSON.toJSONString(payCenterCallbackReq);
        
        Map<String,Long> repaymentPlanIdMap = renewalOrderMapper.getRepaymentPlanIdByPaymentRequestId(paymentRequestId);
        Long repaymentPlanId = repaymentPlanIdMap.get("repaymentPlanId");
        Long renewalOrderId = repaymentPlanIdMap.get("id");
        RepaymentPlan repaymentPlan = repaymentPlanService.getRepaymentPlanByIdWithoutCheck(repaymentPlanId);
		Products products = productsService.getById(repaymentPlan.getProductId());
		
		//更新并发控制表，破坏唯一性约束键
		Long id = loanEventControlMapper.selectByBusinessId(paymentRequestId);
		LoanEventControl loanEventControl = new LoanEventControl();
		loanEventControl.setBusinessId(paymentRequestId);
		loanEventControl.setUkToken(id);
		loanEventControl.setStatus("UPDATED");
		loanEventControlMapper.update(loanEventControl);
		
        if (PaymentCenterCallbackRequestCode.SUCCESS.equals(payCenterCallbackReq.getCode())) {
        	//更新payment_request表状态
        	paymentRequestService.updatePaymentRequestStatusToSuccess(paymentRequestId, payCenterResponseId, payCenterResponseMsg,"","");
    		
        	//更新还款时间和续期次数加1
    		Date nextRepayDate = nextRepayDay(repaymentPlan,products);//下次还款时间
    		repaymentPlanService.updateRenewalCountAndRepaymentPlanTime(repaymentPlan.getId(), nextRepayDate,repaymentPlan.getVersion());

    		//更新续期订单表状态
    		RenewalOrder renewalOrder = new RenewalOrder();
    		renewalOrder.setRepaymentPlanId(repaymentPlan.getId());
    		renewalOrder.setStatus(RenewalOrderStatusEnum.SUCCESS.getCode());
    		renewalOrder.setRenewalUnique(String.valueOf(renewalOrderId));
    		updateRenewalOrder(renewalOrder);
        }else{
        	//更新payment_request表状态
            paymentRequestService.updatePaymentRequestStatusToFailure(paymentRequestId, payCenterResponseId, payCenterResponseMsg,"","");
            
            //更新续期订单表状态
    		RenewalOrder renewalOrder = new RenewalOrder();
    		renewalOrder.setRepaymentPlanId(repaymentPlan.getId());
    		renewalOrder.setStatus(RenewalOrderStatusEnum.FAIL.getCode());
    		renewalOrder.setRenewalUnique(String.valueOf(renewalOrderId));
    		updateRenewalOrder(renewalOrder);
        }
	}
	

}
