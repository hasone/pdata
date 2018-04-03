package com.cmcc.vrp.province.service;

import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;

import java.util.List;

/**
 * Created by leelyn on 2016/12/7.
 */
public interface VirtualProductService {

    /**
     * 
     * title: initProcess desc:
     * 
     * @param superiorProId
     * @param size
     * @return
     * @throws ProductInitException
     *             wuguoping 2017年6月2日
     */
    public Product initProcess(Long superiorProId, String size) throws ProductInitException;

    /**
     * 
     * title: batchInitProcess desc:
     * 
     * @param presentRecordJsons
     * @return
     * @throws ProductInitException
     *             wuguoping 2017年6月2日
     */
    public List<PresentRecordJson> batchInitProcess(List<PresentRecordJson> presentRecordJsons) throws ProductInitException;

    /**
     * 
     * title: activityInitProcess desc:
     * 
     * @param activityPrizesParams
     * @return
     * @throws ProductInitException
     *             wuguoping 2017年6月2日
     */
    public List<ActivityPrize> activityInitProcess(String activityPrizesParams) throws ProductInitException;

    public Long getMaxSizeOfVirtualMobileFee();

    public Long getMaxSizeOfVirtualFlowProduct();
}
