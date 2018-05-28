package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.dto.DemoUserInfo;

import java.util.List;

/**
 * Created by liquan on 2017/9/30.
 */
public interface DemoUserInfoMapper {

    //根据id查询用户
    DemoUserInfo findUserById(Long id);
    //根据数据查询用户
    DemoUserInfo findUser(DemoUserInfo demoUserInfo);
    //分页查询用户信息
    List<DemoUserInfo> selectSelective(DemoUserInfo demoUserInfo);
    //添加用户
    Integer addDemoUser(DemoUserInfo demoUserInfo);
    //删除用户
    Integer deleteDemoUser (DemoUserInfo demoUserInfo);
    //修改用户
    Integer updateDemoUser(DemoUserInfo demoUserInfo);
}
