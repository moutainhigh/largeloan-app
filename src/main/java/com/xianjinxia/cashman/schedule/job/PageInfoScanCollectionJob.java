package com.xianjinxia.cashman.schedule.job;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.Collection;

/**
 * Created by liquan on 2018/1/3.
 */
public abstract class PageInfoScanCollectionJob<M, K extends Collection<M>> extends AbstractJob<K> {

    @Override
    protected boolean isContinue(JobRuntimeContext jrc, K item) {
        PageInfoScanCollectionJobContext context = (PageInfoScanCollectionJobContext) jrc;
        if (this.threshold() > 0) {
            if (context.fetchedRecords >= this.threshold()) {
                jrc.isAdvancedOver = true;
                return false;
            }
        }
        if (context.currentPageIndex <= context.endPageIndex&&isNotEnd()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected K fetchItem(JobRuntimeContext context) {
        context= createContext();
        PageInfoScanCollectionJobContext cnt =(PageInfoScanCollectionJobContext) context;
//        context = createContext();
        PageInfo<M> item = null;
        PageHelper.startPage(cnt.currentPageIndex, this.pageSize());
        item = this.fetch();
        cnt.currentPageIndex++;
        if (item != null) {
            cnt.fetchedRecords += item.getPageSize();
            cnt.currentFetchedRecords = item.getPageSize();
            cnt.endPageIndex = item.getNavigateLastPage();
        } else {
            cnt.currentFetchedRecords = 0;
            return null;
        }
        return (K) item.getList();
    }

    @Override
    protected JobRuntimeContext createContext() {
        JobRuntimeContext context = new PageInfoScanCollectionJobContext(this.startPageIndex(), this.pageSize(), this.currentPageIndex(), this.endPageIndex());
        return context;
    }

    public abstract int startPageIndex();

    public abstract int pageSize();

    public abstract int currentPageIndex();

    public abstract int endPageIndex();

    public abstract PageInfo<M> fetch();

    public abstract Boolean isNotEnd();
}

class PageInfoScanCollectionJobContext extends JobRuntimeContext {
    int startPageIndex;
    int endPageIndex;
    int currentPageIndex;
    int pageSize;
    int fetchedRecords = 0;

    public PageInfoScanCollectionJobContext(int startPageIndex, int pageSize, int currentPageIndex, int endPageIndex) {
        super();
        this.startPageIndex = startPageIndex;
        this.pageSize = pageSize;
        this.currentPageIndex = currentPageIndex;
        this.endPageIndex = endPageIndex;
    }
}