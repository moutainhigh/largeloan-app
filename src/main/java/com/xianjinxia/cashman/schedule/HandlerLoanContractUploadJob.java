package com.xianjinxia.cashman.schedule;

import com.github.pagehelper.Page;
import com.xianjinxia.cashman.domain.LoanContract;
import com.xianjinxia.cashman.enums.ContractTypeEnum;
import com.xianjinxia.cashman.enums.LoanContractStatusEnum;
import com.xianjinxia.cashman.mapper.LoanContractMapper;
import com.xianjinxia.cashman.schedule.job.PagebleScanCollectionJob;
import com.xianjinxia.cashman.service.ILoanContractService;
import com.xianjinxia.cashman.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandlerLoanContractUploadJob extends PagebleScanCollectionJob<LoanContract, List<LoanContract>> {

    private static Logger logger= LoggerFactory.getLogger(HandlerLoanContractUploadJob.class);
    @Autowired
    private LoanContractMapper loanContractMapper;

    @Autowired
    private ILoanContractService loanContractService;

    @Override
    public void process(List<LoanContract> item) {
        for(LoanContract loanContract:item){
            try {
                for(int i=0;i<4;i++) {
                    if (loanContractService.uploanLoanContract(loanContract)) {
                        break;
                    }
                    if (i == 3) {
                        //3次都操作失敗則放棄，不再循環，直接更新表状态为失败
                        loanContractMapper.updateStatusAndcontractPathById(loanContract.getId(), LoanContractStatusEnum.UPLOAN_FAIL.getCode(), StringUtil.NULL_CHARACTER_STRING);
                        logger.info("合同生成和上传失败");
                    }
                }
            } catch (Exception e) {
                loanContractMapper.updateStatusAndcontractPathById(loanContract.getId(), LoanContractStatusEnum.UPLOAN_FAIL.getCode(), StringUtil.NULL_CHARACTER_STRING);
                logger.error("上传借款合同失败,loanContract trd_loan_id:"+loanContract.getId(),e);
            }
        }

    }

    @Override
    public int pageSize() {
        return 100;
    }

    @Override
    public int threshold() {
        return 3000;
    }

    @Override
    public Page<LoanContract> fetch() {
        return (Page<LoanContract>)loanContractMapper.getByStatusAndType(LoanContractStatusEnum.NOT_HANDLE.getCode(), ContractTypeEnum.LOAN_AGREEMENT.getType());
    }
}
