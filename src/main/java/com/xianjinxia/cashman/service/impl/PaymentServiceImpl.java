package com.xianjinxia.cashman.service.impl;

import com.xianjinxia.cashman.constants.Constant;
import com.xianjinxia.cashman.domain.FeeDetail;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.domain.LoanOrder;
import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.ContractDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.dto.SmsDto;
import com.xianjinxia.cashman.enums.LoanContractStatusEnum;
import com.xianjinxia.cashman.enums.RepaymentPlanOperationFlagEnum;
import com.xianjinxia.cashman.mapper.ContractMapper;
import com.xianjinxia.cashman.mapper.FeeDetailMapper;
import com.xianjinxia.cashman.mapper.LoanCapitalInfoMapper;
import com.xianjinxia.cashman.mapper.LoanContractMapper;
import com.xianjinxia.cashman.mapper.LoanOrderMapper;
import com.xianjinxia.cashman.mapper.RepaymentPlanMapper;
import com.xianjinxia.cashman.request.PaymentMessage;
import com.xianjinxia.cashman.service.ILoanService;
import com.xianjinxia.cashman.service.IPaymentService;
import com.xianjinxia.cashman.service.ISmsService;
import com.xianjinxia.cashman.strategy.money.MoneyContext;
import com.xianjinxia.cashman.utils.BeanPropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by MJH on 2017/9/1.
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

    private static Logger logger= LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private FeeDetailMapper feeDetailMapper;

    @Autowired
    private ILoanService loanService;

    @Autowired
    private MoneyContext moneyContext;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private LoanContractMapper loanContractMapper;

    @Autowired
    private LoanCapitalInfoMapper loanCapitalInfoMapper;

    @Override
    @Transactional
    public void processLoanSuccess(PaymentMessage paymentMessage) {
        //---------开始插入loanOrder 冗余表
        LoanOrder loanOrder = new LoanOrder();
        BeanPropertyUtils.copyProperties(paymentMessage, loanOrder);
//        loanOrderMapper.insert(loanOrder);
//        logger.info("insert loan order success");
        //---------插入loanOrder表结束

        //--------插入借款合同表
        List<ContractDto> contracts=contractMapper.selectByProductId(paymentMessage.getProductId());
        for(ContractDto contract:contracts){
            LoanContract loanContract=new LoanContract(contract.getContractName(),
                    contract.getContractType(),paymentMessage.getTrdLoanOrderId(),
                    paymentMessage.getUserId(),LoanContractStatusEnum.NOT_HANDLE.getCode());

            loanContractMapper.insert(loanContract);
        }

        //----插入loanCapitalInfo
        paymentMessage.getLoanCaptionInfo().setTrdLoanOrderId(paymentMessage.getTrdLoanOrderId());
        loanCapitalInfoMapper.insert(paymentMessage.getLoanCaptionInfo());

        //-------开始插入费用明细
        Long loanOrderId = paymentMessage.getTrdLoanOrderId();
        //获取服务费明细
        /*List<ServiceChargeFee> feeDetails = loanService.getServiceChargeFees(paymentMessage.getOrderAmount(), paymentMessage.getProductId());
        for (ServiceChargeFee serviceChargeFee : feeDetails) {
            FeeDetail feeDetail = new FeeDetail(serviceChargeFee.getFeeType(),
                    serviceChargeFee.getFeeMoney().intValue(), loanOrderId);
            feeDetailMapper.insert(feeDetail);
        }
        logger.info("insert feeDetail success");*/
        //-------插入费用明细结束

        //-------开始插入还款明细

//        List<RepaymentPlan> list = moneyContext.calcPeriodData(paymentMessage.getProductId(),paymentMessage.getOrderAmount(),
//                paymentMessage.getPeriods(), paymentMessage.getPaymentTime());
//        for (int i=0;i<list.size();i++) {
//            RepaymentPlan repay=list.get(i);
//            repay.setUserId(paymentMessage.getUserId());
//            repay.setRepaymentOriginAmount(repay.getRepaymentPrincipalAmount()+repay.getRepaymentInterestAmount());
//            repay.setRepaymentTotalAmount(repay.getRepaymentOriginAmount());
//            repay.setLoanOrderId(loanOrderId);
//            repay.setProductId(paymentMessage.getProductId());
//            repay.setOperationFlag(RepaymentPlanOperationFlagEnum.INIT.getCode());
//            repaymentPlanMapper.insert(repay);
//        }
//        logger.info("insert repay order success");
        //-------插入还款明细结束
    }

}
