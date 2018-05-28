package com.xianjinxia.cashman.strategy.money;

import com.xianjinxia.cashman.domain.RepaymentPlan;
import com.xianjinxia.cashman.dto.LoanConfigModalDto;
import com.xianjinxia.cashman.dto.ProductsDto;
import com.xianjinxia.cashman.dto.ServiceChargeFee;
import com.xianjinxia.cashman.response.LoanFeeDetails;
import com.xianjinxia.cashman.response.RepaymentPlanAdvance;
import java.util.List;

public interface MoneyCalculator {

    /**
     * 获取每期的利息
     * @param products
     * @param money
     * @param period
     * @return
     */
    List<Integer> getInterest(ProductsDto products, Integer money, Integer period);

    /**
     * 获取每期的本金
     * @param products
     * @param money
     * @param period
     * @return
     */
    List<Integer> getPeriodMoney(ProductsDto products,Integer money, Integer period);

    /**
     * 获取费用明细列表--（由于大额小额计算费用明细的方法一致，采用小额的计算方法）
     * @param productId
     * @param money
     * @return
     */
    List<ServiceChargeFee> getServiceChargeFees(Long productId,Integer money);
}
