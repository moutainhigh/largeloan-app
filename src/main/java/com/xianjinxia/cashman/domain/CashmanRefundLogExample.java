package com.xianjinxia.cashman.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CashmanRefundLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CashmanRefundLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdIsNull() {
            addCriterion("loan_order_id is null");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdIsNotNull() {
            addCriterion("loan_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdEqualTo(Long value) {
            addCriterion("loan_order_id =", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdNotEqualTo(Long value) {
            addCriterion("loan_order_id <>", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdGreaterThan(Long value) {
            addCriterion("loan_order_id >", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("loan_order_id >=", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdLessThan(Long value) {
            addCriterion("loan_order_id <", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("loan_order_id <=", value, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdIn(List<Long> values) {
            addCriterion("loan_order_id in", values, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdNotIn(List<Long> values) {
            addCriterion("loan_order_id not in", values, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdBetween(Long value1, Long value2) {
            addCriterion("loan_order_id between", value1, value2, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andLoanOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("loan_order_id not between", value1, value2, "loanOrderId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdIsNull() {
            addCriterion("repayment_plan_id is null");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdIsNotNull() {
            addCriterion("repayment_plan_id is not null");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdEqualTo(Long value) {
            addCriterion("repayment_plan_id =", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdNotEqualTo(Long value) {
            addCriterion("repayment_plan_id <>", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdGreaterThan(Long value) {
            addCriterion("repayment_plan_id >", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdGreaterThanOrEqualTo(Long value) {
            addCriterion("repayment_plan_id >=", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdLessThan(Long value) {
            addCriterion("repayment_plan_id <", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdLessThanOrEqualTo(Long value) {
            addCriterion("repayment_plan_id <=", value, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdIn(List<Long> values) {
            addCriterion("repayment_plan_id in", values, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdNotIn(List<Long> values) {
            addCriterion("repayment_plan_id not in", values, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdBetween(Long value1, Long value2) {
            addCriterion("repayment_plan_id between", value1, value2, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentPlanIdNotBetween(Long value1, Long value2) {
            addCriterion("repayment_plan_id not between", value1, value2, "repaymentPlanId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdIsNull() {
            addCriterion("repayment_record_id is null");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdIsNotNull() {
            addCriterion("repayment_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdEqualTo(Long value) {
            addCriterion("repayment_record_id =", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdNotEqualTo(Long value) {
            addCriterion("repayment_record_id <>", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdGreaterThan(Long value) {
            addCriterion("repayment_record_id >", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("repayment_record_id >=", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdLessThan(Long value) {
            addCriterion("repayment_record_id <", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdLessThanOrEqualTo(Long value) {
            addCriterion("repayment_record_id <=", value, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdIn(List<Long> values) {
            addCriterion("repayment_record_id in", values, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdNotIn(List<Long> values) {
            addCriterion("repayment_record_id not in", values, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdBetween(Long value1, Long value2) {
            addCriterion("repayment_record_id between", value1, value2, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRepaymentRecordIdNotBetween(Long value1, Long value2) {
            addCriterion("repayment_record_id not between", value1, value2, "repaymentRecordId");
            return (Criteria) this;
        }

        public Criteria andRefundAmtIsNull() {
            addCriterion("refund_amt is null");
            return (Criteria) this;
        }

        public Criteria andRefundAmtIsNotNull() {
            addCriterion("refund_amt is not null");
            return (Criteria) this;
        }

        public Criteria andRefundAmtEqualTo(Integer value) {
            addCriterion("refund_amt =", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtNotEqualTo(Integer value) {
            addCriterion("refund_amt <>", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtGreaterThan(Integer value) {
            addCriterion("refund_amt >", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtGreaterThanOrEqualTo(Integer value) {
            addCriterion("refund_amt >=", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtLessThan(Integer value) {
            addCriterion("refund_amt <", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtLessThanOrEqualTo(Integer value) {
            addCriterion("refund_amt <=", value, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtIn(List<Integer> values) {
            addCriterion("refund_amt in", values, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtNotIn(List<Integer> values) {
            addCriterion("refund_amt not in", values, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtBetween(Integer value1, Integer value2) {
            addCriterion("refund_amt between", value1, value2, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundAmtNotBetween(Integer value1, Integer value2) {
            addCriterion("refund_amt not between", value1, value2, "refundAmt");
            return (Criteria) this;
        }

        public Criteria andRefundChannelIsNull() {
            addCriterion("refund_channel is null");
            return (Criteria) this;
        }

        public Criteria andRefundChannelIsNotNull() {
            addCriterion("refund_channel is not null");
            return (Criteria) this;
        }

        public Criteria andRefundChannelEqualTo(String value) {
            addCriterion("refund_channel =", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelNotEqualTo(String value) {
            addCriterion("refund_channel <>", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelGreaterThan(String value) {
            addCriterion("refund_channel >", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelGreaterThanOrEqualTo(String value) {
            addCriterion("refund_channel >=", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelLessThan(String value) {
            addCriterion("refund_channel <", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelLessThanOrEqualTo(String value) {
            addCriterion("refund_channel <=", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelLike(String value) {
            addCriterion("refund_channel like", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelNotLike(String value) {
            addCriterion("refund_channel not like", value, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelIn(List<String> values) {
            addCriterion("refund_channel in", values, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelNotIn(List<String> values) {
            addCriterion("refund_channel not in", values, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelBetween(String value1, String value2) {
            addCriterion("refund_channel between", value1, value2, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundChannelNotBetween(String value1, String value2) {
            addCriterion("refund_channel not between", value1, value2, "refundChannel");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoIsNull() {
            addCriterion("refund_order_no is null");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoIsNotNull() {
            addCriterion("refund_order_no is not null");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoEqualTo(String value) {
            addCriterion("refund_order_no =", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoNotEqualTo(String value) {
            addCriterion("refund_order_no <>", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoGreaterThan(String value) {
            addCriterion("refund_order_no >", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("refund_order_no >=", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoLessThan(String value) {
            addCriterion("refund_order_no <", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoLessThanOrEqualTo(String value) {
            addCriterion("refund_order_no <=", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoLike(String value) {
            addCriterion("refund_order_no like", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoNotLike(String value) {
            addCriterion("refund_order_no not like", value, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoIn(List<String> values) {
            addCriterion("refund_order_no in", values, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoNotIn(List<String> values) {
            addCriterion("refund_order_no not in", values, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoBetween(String value1, String value2) {
            addCriterion("refund_order_no between", value1, value2, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundOrderNoNotBetween(String value1, String value2) {
            addCriterion("refund_order_no not between", value1, value2, "refundOrderNo");
            return (Criteria) this;
        }

        public Criteria andRefundTimeIsNull() {
            addCriterion("refund_time is null");
            return (Criteria) this;
        }

        public Criteria andRefundTimeIsNotNull() {
            addCriterion("refund_time is not null");
            return (Criteria) this;
        }

        public Criteria andRefundTimeEqualTo(Date value) {
            addCriterion("refund_time =", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeNotEqualTo(Date value) {
            addCriterion("refund_time <>", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeGreaterThan(Date value) {
            addCriterion("refund_time >", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("refund_time >=", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeLessThan(Date value) {
            addCriterion("refund_time <", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeLessThanOrEqualTo(Date value) {
            addCriterion("refund_time <=", value, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeIn(List<Date> values) {
            addCriterion("refund_time in", values, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeNotIn(List<Date> values) {
            addCriterion("refund_time not in", values, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeBetween(Date value1, Date value2) {
            addCriterion("refund_time between", value1, value2, "refundTime");
            return (Criteria) this;
        }

        public Criteria andRefundTimeNotBetween(Date value1, Date value2) {
            addCriterion("refund_time not between", value1, value2, "refundTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("created_time is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("created_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Date value) {
            addCriterion("created_time =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Date value) {
            addCriterion("created_time <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Date value) {
            addCriterion("created_time >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("created_time >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Date value) {
            addCriterion("created_time <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Date value) {
            addCriterion("created_time <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Date> values) {
            addCriterion("created_time in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Date> values) {
            addCriterion("created_time not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Date value1, Date value2) {
            addCriterion("created_time between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Date value1, Date value2) {
            addCriterion("created_time not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNull() {
            addCriterion("updated_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNotNull() {
            addCriterion("updated_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeEqualTo(Date value) {
            addCriterion("updated_time =", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotEqualTo(Date value) {
            addCriterion("updated_time <>", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThan(Date value) {
            addCriterion("updated_time >", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_time >=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThan(Date value) {
            addCriterion("updated_time <", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThanOrEqualTo(Date value) {
            addCriterion("updated_time <=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIn(List<Date> values) {
            addCriterion("updated_time in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotIn(List<Date> values) {
            addCriterion("updated_time not in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeBetween(Date value1, Date value2) {
            addCriterion("updated_time between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotBetween(Date value1, Date value2) {
            addCriterion("updated_time not between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andDataValidIsNull() {
            addCriterion("data_valid is null");
            return (Criteria) this;
        }

        public Criteria andDataValidIsNotNull() {
            addCriterion("data_valid is not null");
            return (Criteria) this;
        }

        public Criteria andDataValidEqualTo(Boolean value) {
            addCriterion("data_valid =", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidNotEqualTo(Boolean value) {
            addCriterion("data_valid <>", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidGreaterThan(Boolean value) {
            addCriterion("data_valid >", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidGreaterThanOrEqualTo(Boolean value) {
            addCriterion("data_valid >=", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidLessThan(Boolean value) {
            addCriterion("data_valid <", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidLessThanOrEqualTo(Boolean value) {
            addCriterion("data_valid <=", value, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidIn(List<Boolean> values) {
            addCriterion("data_valid in", values, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidNotIn(List<Boolean> values) {
            addCriterion("data_valid not in", values, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidBetween(Boolean value1, Boolean value2) {
            addCriterion("data_valid between", value1, value2, "dataValid");
            return (Criteria) this;
        }

        public Criteria andDataValidNotBetween(Boolean value1, Boolean value2) {
            addCriterion("data_valid not between", value1, value2, "dataValid");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}