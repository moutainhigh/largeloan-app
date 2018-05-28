package com.xianjinxia.cashman.enums;

/**
 * 人工入账逾期状态操作类型
 *
 * @author JaJian
 * @create 2018-05-15 16:40
 **/
public enum ManualIncomOverdueStatusEnum {

    CONDITIION_1(1, "人工入账情形1"),
    CONDITIION_2(2, "人工入账情形2"),
    CONDITIION_3(3, "人工入账情形3"),
    CONDITIION_4(4, "人工入账情形4");

    private int type;

    private String desc;

    ManualIncomOverdueStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
