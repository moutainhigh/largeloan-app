package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.domain.CollectRequest;
import com.xianjinxia.cashman.enums.CollectWithholdEnum;
import com.xianjinxia.cashman.idempotent.IdempotentKey;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/** 催收代扣请求类
 * @author ganminghui
 * <p>
 *     {
 *      "money":330363,
 *      "repaymentId":"101231",
 *      "sign":"811616357c4a51f444f71abe6c72b730",
 *      "userId":"768093897",
 *      "uuid":"25158fe1f964440b817650833c96f950"
 *      }
 * </p>
 */
public class CollectWithholdReq extends BaseRequest{
    @IdempotentKey(order=2)
    @NotNull(message="repay plan id can't be null")
    private Long repaymentId;

    @NotNull(message="withhold amount can't be null")
    @Min(value = 0,message = "withhold amount should be gt 0")
    private Long money;

    @NotNull(message="sign can't be null")
    private String sign;

    @NotNull(message = "userId can't be null")
    private Long userId;

    @IdempotentKey(order=1)
    @NotNull(message="uuid can't be null")
    private String uuid;

    public Long getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(Long repaymentId) {
        this.repaymentId = repaymentId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CollectRequest buildWithhold(){
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRepaymentPlanId(this.getRepaymentId());
        collectRequest.setUserId(this.userId);
        collectRequest.setAmount(this.money);
        collectRequest.setStatus(CollectWithholdEnum.STATUS_NEW.getType());
        collectRequest.setCollectType(CollectWithholdEnum.TYPE_WITHHOLE.getType());
        collectRequest.setUuid(this.uuid);
        return collectRequest;
    }
}