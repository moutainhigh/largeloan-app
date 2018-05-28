package com.xianjinxia.cashman.dto;

public class BankCardDto {
    private String card_no;
    private String openName;
    private String bankName;


    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "BankCardDto{" +
                "card_no='" + card_no + '\'' +
                ", openName='" + openName + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
