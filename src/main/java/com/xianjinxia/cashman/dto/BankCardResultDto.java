package com.xianjinxia.cashman.dto;
import java.util.List;

public class BankCardResultDto {

    private List<BankCardDto> userCardList;

    public List<BankCardDto> getUserCardList() {
        return userCardList;
    }

    public void setUserCardList(List<BankCardDto> userCardList) {
        this.userCardList = userCardList;
    }
}
