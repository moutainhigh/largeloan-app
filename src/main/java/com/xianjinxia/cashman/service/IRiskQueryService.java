package com.xianjinxia.cashman.service;

import com.xianjinxia.cashman.dto.RiskQueryDto;
import com.xianjinxia.cashman.response.BaseResponse;
import com.xianjinxia.cashman.response.OpenApiBaseResponse;

/**
 * 风控查询自有数据service
 * Created by liquan on 2018/1/4.
 */
public interface IRiskQueryService  {


    /**
     * 风控查询自有数据接口
     * 包含获取用户相关信息
     * @param trdLoanOrderId 订单id
     * @param userId  用户id
     * @return
     */
    OpenApiBaseResponse<RiskQueryDto> query(String trdLoanOrderId,String userId);

}
