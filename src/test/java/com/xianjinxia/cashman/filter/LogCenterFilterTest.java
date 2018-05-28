package com.xianjinxia.cashman.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class LogCenterFilterTest {

    private static final Logger logger = LoggerFactory.getLogger(LogCenterFilterTest.class);

    @Test
    public void mdcTest() throws Exception {
        MDC.put("traceId","131");
        logger.info("test{}", MDC.get("traceId"));
        logger.trace("traceId");
    }


}