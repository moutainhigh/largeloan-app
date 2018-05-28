package com.xianjinxia.cashman.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xianjinxia.cashman.conf.MyRestTemplate;
import com.xianjinxia.cashman.dto.RiskQueryDto;
import com.xianjinxia.cashman.dto.UserInfoDto;
import com.xianjinxia.cashman.dto.UserInfoQueryDto;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IRiskQueryService;
import org.apache.commons.beanutils.BeanMap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by liquan on 2018/1/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RiskQueryServiceImplTest {

    @Autowired IRiskQueryService riskQueryService;

    @Autowired MyRestTemplate restTemplate;

    @Test
    public void testQuery(){
        BaseResponse<RiskQueryDto> dto = riskQueryService.query("1002",null);
        System.out.println(JSON.toJSONString(dto));
        BaseResponse<RiskQueryDto> dto1 = riskQueryService.query(null,"4724234");
        System.out.println(JSON.toJSONString(dto1));
    }

    @Test
    public void testUserInfo(){
        UserInfoQueryDto infoDto = restTemplate.httpGetWithAbsoluteUrl("192.168.4.40:8010/user/4324442",null,
            new ParameterizedTypeReference<UserInfoQueryDto>() {
            });
        Gson gson = new Gson();
        System.out.println(gson.toJson(infoDto));
    }


    @Test
    public void testGson(){
        UserInfoQueryDto dto = new UserInfoQueryDto();
        UserInfoDto udto = new UserInfoDto();
        udto.setCreditTotal("100");
        udto.setCompanyName("fff");
        dto.setUser_info(udto);
        Gson gson = new Gson();
        System.out.println(gson.toJson(dto));
    }




}
