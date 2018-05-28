package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.CollectRequest;
import org.apache.ibatis.annotations.Param;

/**
 * @author ganminghui
 */
public interface CollectRequestMapper {

    /**
     * 催收请求落库
     * @param collectRequest 待保存的实例
     * */
    void insert(CollectRequest collectRequest);

    /**
     * 根据id修改催收请求状态
     * @param id 请求编号
     * @param status 待修改的状态值
     * @param remark 支付中心回调响应消息
     * @return 返回受影响行数
     * */
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status, @Param("remark")String remark);

    /**
     * 根据请求编号获取uuid
     * @param id 待查询的主键
     * @return 返回催收请求的uuid
     * */
    String getUuidById(@Param("id") Long id);
}