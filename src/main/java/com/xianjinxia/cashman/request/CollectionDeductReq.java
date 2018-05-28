package com.xianjinxia.cashman.request;

import com.xianjinxia.cashman.domain.CollectRequest;
import com.xianjinxia.cashman.enums.CollectWithholdEnum;
import com.xianjinxia.cashman.enums.DeductTypeEnum;
import com.xianjinxia.cashman.idempotent.IdempotentKey;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/** 催收减免请求类
 * @author ganminghui
 * <p>
{
"uuid": "3f54e2d1454c4f95b1cf4e37e8741da6",
"reductionMoney": 234000,
"status": "2",
"sign": "sign"，
"repaymentId":"12345",
"userId":"22222"
}
 * </p>
 */
public class CollectionDeductReq extends BaseRequest{
    /** 待减免的用户编号 */
    @NotNull(message = "userId can't be null") private Long userId;
    /** 待减免的还款计划 */
    @IdempotentKey(order=2) @NotNull(message="repay plan id can't be null") private Long repaymentId;
    /** 待减免的金额 */
    @NotNull(message="dedict amount can't be null") @Min(value = 0,message = "withhold amount should be gt 0") private Long reductionMoney;
    /** 待减免的类型(默认后置减免) */
    private Integer deductType = DeductTypeEnum.AFTER.getType();
    /** 催收传递的uuid(幂等一部分) */
    @IdempotentKey(order=1) @NotNull(message="uuid can't be null") private String uuid;
    @NotNull(message="sign can't be null") private String sign;
    @NotNull(message = "status can't be null") private String status;

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSign() { return sign; }

    public void setSign(String sign) { this.sign = sign; }

    public Long getRepaymentId() { return repaymentId; }

    public void setRepaymentId(Long repaymentId) { this.repaymentId = repaymentId; }

    public Long getReductionMoney() { return reductionMoney; }

    public void setReductionMoney(Long reductionMoney) { this.reductionMoney = reductionMoney; }

    public Integer getDeductType() { return deductType; }

    public void setDeductType(Integer deductType) { this.deductType = deductType; }

    public CollectRequest buildDeduct(){
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRepaymentPlanId(this.getRepaymentId());
        collectRequest.setUserId(this.userId);
        collectRequest.setAmount(this.reductionMoney);
        collectRequest.setStatus(CollectWithholdEnum.STATUS_NEW.getType());
        collectRequest.setCollectType(CollectWithholdEnum.TYPE_DEDUCT.getType());
        collectRequest.setUuid(this.uuid);
        return collectRequest;
    }
}