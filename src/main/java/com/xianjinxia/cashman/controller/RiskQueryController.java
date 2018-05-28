package com.xianjinxia.cashman.controller;

import com.dianping.cat.common.EventInfo;
import com.dianping.cat.utils.CatUtils;
import com.xianjinxia.cashman.dto.RiskQueryDto;
import com.xianjinxia.cashman.dto.RiskQueryParamDto;
import com.xianjinxia.cashman.exceptions.ServiceException;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;
import com.xianjinxia.cashman.service.IRiskQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liquan on 2018/1/4.
 */

@Api(tags = "cashman_app loan query controller")
@RestController
@RequestMapping("/service/loan/")
public class RiskQueryController extends BaseController{


    private Logger logger = LoggerFactory.getLogger(RiskQueryController.class);


    @Autowired IRiskQueryService riskQueryService;


    @ApiOperation("loan query info by order id")
    @PostMapping("queryOrderList")
    public OpenApiBaseResponse<RiskQueryDto> query(@RequestBody RiskQueryParamDto dto){
        OpenApiBaseResponse<RiskQueryDto> baseResponse = new OpenApiBaseResponse<>();
        try {
            EventInfo event = new EventInfo();
            event.setEventType("risk-query-order-list");
            CatUtils.info(event);
            baseResponse = riskQueryService.query(dto.getOrder_id(),dto.getUser_id());
        } catch (ServiceException se) {
            logger.error("风控查询失败:", se);
            baseResponse.systemError();
        } catch (Exception e) {
            logger.error("风控查询异常", e);
            baseResponse.systemError();
        }
        return baseResponse;
    }
}
