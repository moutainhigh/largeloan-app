package com.xianjinxia.cashman.mapper;

import com.xianjinxia.cashman.domain.JobLock;
import org.apache.ibatis.annotations.Param;

public interface JobLockMapper {
	
    int insert(JobLock jobLock);
    
    JobLock selectByJobName(String jobName);
   
    int update(@Param("id") long id,@Param("ukToken") long ukToken,@Param("jobName")String jobName);

}
