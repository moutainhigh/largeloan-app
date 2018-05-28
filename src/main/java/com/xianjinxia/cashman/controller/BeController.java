package com.xianjinxia.cashman.controller;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.cashman.dto.StatisticRepayOrderDto;
import com.xianjinxia.cashman.exceptions.IdempotentException;
import com.xianjinxia.cashman.request.BeforeHandPayReq;
import com.xianjinxia.cashman.request.ProductReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.BeforeHandPayService;
import com.xianjinxia.cashman.service.IProductsService;
import com.xianjinxia.cashman.service.IStatisticalService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liquan on 2017/12/25.
 */
@RestController
@RequestMapping("/be")
public class BeController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BeController.class);

    @Autowired
    private IProductsService productsService;
    @Autowired
    private IStatisticalService statisticalService;
    @Autowired
    private BeforeHandPayService beforeHandPayService;
    @ApiOperation("save-product")
    @PostMapping("/product/save-product")
    public BaseResponse<Void> saveProduct(@RequestBody ProductReq productReq) {
        logger.info("产品配置数据新增,参数:{}", productReq);
        BaseResponse response = new BaseResponse();
        try {
            response = productsService.saveProduct(productReq);
        } catch (Exception e) {
            logger.info("产品配置数据新增", e);
            response.systemError();
        }
        return response;
    }

    @ApiOperation("delete-product with productId")
    @PostMapping("/product/delete-product")
    public BaseResponse<Void> deleteProduct(@RequestBody ProductReq productReq) {
        logger.info("产品配置数据删除,参数:{}", productReq.getProductId());
        BaseResponse response = new BaseResponse();
        try {
            int result = productsService.deleteProduct(productReq);
            logger.info("产品配置数据删除,result:{}", result);
        } catch (Exception e) {
            logger.info("产品配置数据删除", e);
            response.systemError();
        }
        return response;
    }

    @ApiOperation("update product")
    @PostMapping("/product/update-product")
    public BaseResponse<Void> updateProduct(@RequestBody ProductReq productReq) {
        logger.info("产品配置数据更新,参数:{}", productReq);
        BaseResponse<Void> response = new BaseResponse<>();
        try {
            response =  productsService.updateProduct(productReq);
        } catch (Exception e) {
            logger.info("产品配置数据更新", e);
            response.systemError();
        }
        return response;
    }

    /**
     * 添加接口提供给催收系统，统计时间区间内应还单数和金额
     * @param startDay
     * @param endDay
     * @return
     */
    @GetMapping("/getRepaymentOrderForCollection")
    public  BaseResponse<List<StatisticRepayOrderDto>> getRepaymentOrderForCollection(String startDay, String endDay){
        BaseResponse<List<StatisticRepayOrderDto>> response =new BaseResponse<List<StatisticRepayOrderDto>>();
        List<StatisticRepayOrderDto> listRepayMentPlan = statisticalService.getRepayOrderForDay(startDay,endDay);
        response.setData(listRepayMentPlan);
        return response;
    }

    /**
     * 冲正订单，提前还款
     */
    @PostMapping("/beforePay")
    public BaseResponse<Void>  beforeHandPay(@RequestBody BeforeHandPayReq beforeHandPayReq){
        logger.info("beforeHandPay,入参为:{}", JSON.toJSONString(beforeHandPayReq));
        BaseResponse<Void> response = new BaseResponse<>();
        if(beforeHandPayReq.paramCheck()){
            try {
                beforeHandPayService.beforeHandPay(beforeHandPayReq);
            }catch (IdempotentException ide){
                logger.info("订单号为:{}已经处理过了",beforeHandPayReq.getOrderNo());
                response.setMsg("该订单已经在处理中，请稍后刷新结果");
            }catch(Exception e){
                response.systemError();
            }
        }
        return  response;
    }
}
