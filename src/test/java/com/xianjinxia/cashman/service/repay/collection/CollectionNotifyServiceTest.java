package com.xianjinxia.cashman.service.repay.collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectionNotifyServiceTest {

    @Autowired
    private CollectionNotifyService collectionNotifyService;

    @Autowired
    private CollectionNotifyDtoBuilder collectionNotifyDtoBuilder;

    @Test
    public void notifyTest() {
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(1l, true);
        collectionNotifyService.notify(collectionNotifyDto);
    }

    @Test
    public void cancelTest() {
        CollectionNotifyDto collectionNotifyDto = collectionNotifyDtoBuilder.build(1l, true);
        collectionNotifyService.cancel(collectionNotifyDto);
    }
}