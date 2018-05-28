package com.xianjinxia.cashman.controller;

import com.xianjinxia.cashman.dto.IndexProductsDto;
import com.xianjinxia.cashman.remote.TradeAppRemoteService;
import com.xianjinxia.cashman.request.LoanIndexReq;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.service.IProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/service/products/")
@RestController
@Api(tags = {"products"})
public class ProductsController {

    private Logger logger= LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private IProductsService productsService;
    @Autowired
    private TradeAppRemoteService tradeAppRemoteService;

    @PostMapping("/get-products")
    @ApiOperation(value = "index products info", notes = "get products info to index page")
    public BaseResponse<IndexProductsDto> getProducts(@RequestBody LoanIndexReq loanIndexReq){
        logger.info("get-products入参为：",loanIndexReq);
        BaseResponse<IndexProductsDto> resp=new BaseResponse<>();
        try {
            resp.setData(productsService.getByCategoryAndMerchantNo(loanIndexReq.getProductCategory(),loanIndexReq.getMerchantNo()));
        } catch (Exception e) {
            logger.error("/service/products/get-products",e);
            resp.systemError();
        }
        return resp;
    }
}
