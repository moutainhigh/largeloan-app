package com.xianjinxia.cashman.service.repay.collection;


import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.domain.RepaymentRecord;
import com.xianjinxia.cashman.mapper.RepaymentRecordMapper;
import com.xianjinxia.cashman.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectionNotifyDtoBuilderTest {

    @Autowired
    private CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;

    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    @Test
    public void buildTest(){
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(100057L, true);
        System.out.println(JSON.toJSONString(collectionNotifyDto));
    }

    @Test
    public void buildRepaymentDetailTest(){
        List<RepaymentRecord> repaymentRecords = repaymentRecordMapper.selectByRepaymentPlanId(100057l);
        CollectionNotifyRepaymentDetailDto repaymentDetailDto = new CollectionNotifyRepaymentDetailDto();
        RepaymentRecord lastestRepaymentRecord = repaymentRecords.get(0);
        repaymentDetailDto.setId(lastestRepaymentRecord.getId());
        repaymentDetailDto.setCreateDate(lastestRepaymentRecord.getCreatedAt().getTime());
        repaymentDetailDto.setPayId(1l);
        repaymentDetailDto.setReturnType(lastestRepaymentRecord.getPaymentType());

        CollectionNotifyDto collectionNotifyDto = new CollectionNotifyDto();
//        collectionNotifyDto.setRepaymentDetail(repaymentDetailDto);
        System.out.println(JSON.toJSONString(collectionNotifyDto));
    }

    @Test
    public void timeTest() {
        Date yesterday = DateUtil.daysBefore(new Date(), 1);
        Date date = DateUtil.dateFilter(yesterday);
        System.out.println(date.getTime());
    }
}
