/**
 *
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ActivityInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年8月27日
 */
public interface QRCodeService {

    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @return
     */
    public boolean qRCodeCommon(ActivityInfo activityInfo, String fileName,
                                HttpServletRequest res, HttpServletResponse response);
}
