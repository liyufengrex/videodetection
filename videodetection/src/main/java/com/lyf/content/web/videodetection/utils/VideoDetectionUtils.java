package com.lyf.content.web.videodetection.utils;

import android.text.TextUtils;

/**
 * @Description: TODO
 * @Author: 李宇峰 on 2019-03-05 11:40 AM.
 * @NO.: 146714
 * @Phone: 13756017116
 * @Email: liyufeng@syswin.com
 * @Leader：yupengfei <yupengfei@syswin.com>
 * @Charge: 李宇峰
 */
public class VideoDetectionUtils {


    /**
     * 判断视频内容是否发生变化
     *
     * @author liyufeng[13756017116]@2019-03-05 11:46 AM
     */
    public static void checkVideoDetection(String filePath, int level, int interval,
            VideoDetectionCallBack callBack) {
        if (callBack == null || TextUtils.isEmpty(filePath)) {
            return;
        }
        if (interval <= 0) {
            interval = 500;
        } else if (interval > 1000) {
            interval = 1000;
        }
        new DetectionSingleCase(filePath, level, callBack).work(interval, 4);
    }


    public interface VideoDetectionCallBack {

        /**
         * @param isChanged 内容是否有变化
         * @param filePath 视频路径
         * @author liyufeng[13756017116]@2019-03-05 11:45 AM
         */
        void videoDetection(boolean isChanged, String filePath);
    }

}
