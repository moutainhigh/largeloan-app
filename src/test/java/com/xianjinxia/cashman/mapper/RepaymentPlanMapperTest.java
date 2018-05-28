package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepaymentPlanMapperTest {

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;


    private Long id=100000L;
    private Long productId=2L;
    private Integer orderType=3;
    private Long userId=55555L;
    private Long loanOrderId=123456L;
    private Integer repaymentTotalAmount=123123;
    private Integer repaymentOriginAmount=4567;
    private Integer repaymentIncomeAmount=1231231;
    private Integer repaymentWaitingAmount=1231231123;
    private Integer repaymentPrincipalAmount=111111;
    private Integer repaymentInterestAmount=232131;
    private Date repaymentPlanTime=new Date();
    private Date repaymentRealTime=new Date();
    private Integer period=4;
    private Integer status=123;
    private Boolean isCollection=false;
    private Boolean isOverdue=false;
    private Integer overdueFeeAmount=123;
    private Integer overdueDayCount=444;
    private String operationFlag="123";
    private Integer renewalCount=111;
    private Integer badLevel=1;
    private Date createdTime=new Date();
    private Date updatedTime=new Date();
    private String remark="remark";
    private Boolean dataValid=true;
    private Integer version=1;

    private RepaymentPlan getRepaymentPlan(){
        RepaymentPlan rp=new RepaymentPlan();
        rp.setId(id);
        rp.setProductId(productId);
        rp.setOrderType(orderType);
        rp.setUserId(userId);
        rp.setLoanOrderId(loanOrderId);
        rp.setRepaymentTotalAmount(repaymentTotalAmount);
        rp.setRepaymentOriginAmount(repaymentOriginAmount);
        rp.setRepaymentIncomeAmount(repaymentIncomeAmount);
        rp.setRepaymentWaitingAmount(repaymentWaitingAmount);
        rp.setRepaymentPrincipalAmount(repaymentPrincipalAmount);
        rp.setRepaymentInterestAmount(repaymentInterestAmount);
        rp.setRepaymentPlanTime(repaymentPlanTime);
        rp.setRepaymentRealTime(repaymentRealTime);
        rp.setPeriod(period);
        rp.setStatus(status);
        rp.setIsCollection(isCollection);
        rp.setIsOverdue(isOverdue);
        rp.setOverdueFeeAmount(overdueFeeAmount);
        rp.setOverdueDayCount(overdueDayCount);
        rp.setOperationFlag(operationFlag);
        rp.setRenewalCount(renewalCount);
        rp.setBadLevel(badLevel);
        rp.setCreatedTime(createdTime);
        rp.setUpdatedTime(updatedTime);
        rp.setRemark(remark);
        rp.setDataValid(dataValid);
        rp.setVersion(version);
        return rp;
    }


    @Test
    @Transactional
    public void insert() throws Exception {
        RepaymentPlan repaymentPlan=getRepaymentPlan();
        int result=repaymentPlanMapper.insert(repaymentPlan);
        RepaymentPlan rp=repaymentPlanMapper.selectByPrimaryKey(id);
        Assert.assertEquals(1,result);
        Assert.assertEquals(productId,rp.getProductId());
        Assert.assertEquals(orderType,rp.getOrderType());
        Assert.assertEquals(userId,rp.getUserId());
        Assert.assertEquals(loanOrderId,rp.getLoanOrderId());
        Assert.assertEquals(repaymentTotalAmount,rp.getRepaymentTotalAmount());
        Assert.assertEquals(repaymentOriginAmount,rp.getRepaymentOriginAmount());
        Assert.assertEquals(repaymentIncomeAmount,rp.getRepaymentIncomeAmount());
        Assert.assertEquals(repaymentWaitingAmount,rp.getRepaymentWaitingAmount());
        Assert.assertEquals(repaymentPrincipalAmount,rp.getRepaymentPrincipalAmount());
        Assert.assertEquals(repaymentInterestAmount,rp.getRepaymentInterestAmount());
        Assert.assertEquals(period,rp.getPeriod());
        Assert.assertEquals(status,rp.getStatus());
        Assert.assertEquals(isCollection,rp.getIsCollection());
        Assert.assertEquals(isOverdue,rp.getIsOverdue());
        Assert.assertEquals(overdueFeeAmount,rp.getOverdueFeeAmount());
        Assert.assertEquals(overdueDayCount,rp.getOverdueDayCount());
        Assert.assertEquals(operationFlag,rp.getOperationFlag());
        Assert.assertEquals(renewalCount,rp.getRenewalCount());
        Assert.assertEquals(badLevel,rp.getBadLevel());
        Assert.assertEquals(remark,rp.getRemark());
        Assert.assertEquals(dataValid,rp.getDataValid());
        Assert.assertEquals(version,rp.getVersion());
    }

}