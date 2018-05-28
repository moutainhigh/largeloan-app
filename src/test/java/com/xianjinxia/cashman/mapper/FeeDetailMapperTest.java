package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.FeeDetail;
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
public class FeeDetailMapperTest {

    @Autowired
    private FeeDetailMapper feeDetailMapper;

    private Integer feeAmount=10000;
    private String feeType="11";
    private Long loanOrderId=123L;


    private FeeDetail getFeeDetail(){
        FeeDetail feeDetail=new FeeDetail();
        feeDetail.setFeeAmount(feeAmount);
        feeDetail.setFeeType(feeType);
        feeDetail.setTrdLoanOrderId(loanOrderId);
        feeDetail.setCreatedTime(new Date());
        feeDetail.setCreatedUser("aaa");
        return feeDetail;
    }

    @Test
    @Transactional
    public void insert() throws Exception {
        FeeDetail fd=getFeeDetail();
        int result=feeDetailMapper.insert(fd);
        Long id=fd.getId();
        FeeDetail feeDetail=feeDetailMapper.selectByPrimaryKey(id);
        Assert.assertEquals(1,result);
        Assert.assertEquals(feeAmount,feeDetail.getFeeAmount());
        Assert.assertEquals(feeType,feeDetail.getFeeType());
        Assert.assertEquals(loanOrderId,feeDetail.getTrdLoanOrderId());
    }

}