package com.xianjinxia.cashman.service.repay.confirm;

import com.xianjinxia.cashman.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class RepaymentConfirmContext {

	@Autowired
	private List<RepaymentConfirmProcessor> repaymentConfirmProcessorList;

	public void process(RepaymentConfirmProcessParam repaymentConfirmProcessParam){
		for (Iterator<RepaymentConfirmProcessor> iterator = repaymentConfirmProcessorList.iterator(); iterator.hasNext(); ) {
			RepaymentConfirmProcessor processor = iterator.next();
			RepaymentConfirmProcessResult result = processor.process(repaymentConfirmProcessParam);
			if (result.isProcessed()){
				return;
			}
		}
		throw new ServiceException("支付回调请求未处理成功，没找到匹配的处理类");
	}
}
