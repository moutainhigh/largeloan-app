package com.xianjinxia.cashman.job;

import com.xianjinxia.cashman.schedule.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobTest {
	
	@Autowired
	private HandlerCollectionNotifyJob handlerCollectionNotifyJob;
	
	@Autowired
	private HandlerWithHoldPrepareJob handlerWithHoldPrepareJob;
	
	@Autowired
	private HandlerWithHoldExecuteJob handlerWithHoldExecuteJob;
	
	@Autowired
	private HandlerOverduePrepareJob handlerOverduePrepareJob;
	
	@Autowired
	private HandlerOverdueExecuteJob handlerOverdueExecuteJob;

	@Autowired
	private HandlerCustodyLoanJob handlerCustodyLoanJob;
	
	
	
	@Test
	public void jobRunTest() {  
		System.out.println("============>>>>>>>>>");
		handlerCollectionNotifyJob.execute();
	}
	
	
	@Test
	public void withHoldPrepareTest() {  
		System.out.println("============withHoldPrepareTest=========");
		handlerWithHoldPrepareJob.execute();
	}
	
	@Test
	public void withHoldExecuteTest() {  
		System.out.println("============withHoldExecuteTest=========");
		handlerWithHoldExecuteJob.execute();
	}
	
	@Test
	public void overduePrepareTest() {  
		System.out.println("============overduePrepareTest=========");
		handlerOverduePrepareJob.execute();
	}
	
	@Test
	public void overdueExecuteTest() {  
		System.out.println("============overdueExecuteTest=========");
		handlerOverdueExecuteJob.execute();
	}

	@Test
	public void custodyloanTest(){
		System.out.println("========custodyloanTest================");
		handlerCustodyLoanJob.execute();
	}

}
