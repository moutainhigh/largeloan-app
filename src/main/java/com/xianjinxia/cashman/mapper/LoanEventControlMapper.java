package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.LoanEventControl;

public interface LoanEventControlMapper {
	
	public int insert(LoanEventControl loanEventControl);
	
	public int update(LoanEventControl loanEventControl);
	
	public Long selectByBusinessId(Long requestId);

}
