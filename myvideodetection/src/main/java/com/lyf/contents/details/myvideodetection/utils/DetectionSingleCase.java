package com.lyf.contents.details.myvideodetection.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Log;
import com.cry.mediametaretriverwrapper.MediaMetadataRetrieverWrapper;
import com.cry.mediametaretriverwrapper.RetrieverProcessThread;
import com.lyf.contents.details.myvideodetection.utils.VideoDetectionUtils.VideoDetectionCallBack;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: 李宇峰 on 2019-03-05 2:03 PM.
 * @NO.: 146714
 * @Phone: 13756017116
 * @Email: liyufeng@syswin.com
 * @Leader：yupengfei <yupengfei@syswin.com>
 * @Charge: 李宇峰
 */
class DetectionSingleCase {

    private String sourcePath;
    private VideoDetectionCallBack callBack;
    private String preRes = null;
    private List<Integer> results = new ArrayList<Integer>(10);
    private int level = 5;
    private MediaMetadataRetrieverWrapper metadataRetriever2;

    public DetectionSingleCase(String sourcePath, int level, VideoDetectionCallBack callBack) {
        this.sourcePath = sourcePath;
        this.callBack = callBack;
        this.level = level;
    }

    public void work(int interval, int scale) {
        if (TextUtils.isEmpty(this.sourcePath)) {
            return;
        }
        metadataRetriever2 = new MediaMetadataRetrieverWrapper();
        metadataRetriever2.setDataSource(sourcePath);
        metadataRetriever2
                .getFramesInterval(interval, scale, new RetrieverProcessThread.BitmapCallBack() {
                    @Override
                    public void onComplete(final Bitmap frame) {
                        checkDiff(frame);
                    }

                    @Override
                    public void onEnd() {
                        if(metadataRetriever2 != null){
                            metadataRetriever2.release();
                        }
                        checkSimilar();
                    }
                });
    }

    private void checkDiff(Bitmap target) {
        Bitmap bitmap8 = ThumbnailUtils.extractThumbnail(target, 8, 8);
        Bitmap bitmap = SimilarPicture.convertGreyImg(bitmap8);
        int avg = SimilarPicture.getAvg(bitmap);
        String binary = SimilarPicture.getBinary(bitmap, avg);
        String result = SimilarPicture.binaryString2hexString(binary);
        if (preRes != null) {
            diff(preRes, result);
        }
        preRes = result;
    }

    private void diff(String s1, String s2) {
        char[] s1s = s1.toCharArray();
        char[] s2s = s2.toCharArray();
        int diffNum = 0;
        for (int i = 0; i < s1s.length; i++) {
            if (s1s[i] != s2s[i]) {
                diffNum++;
            }
        }
        results.add(diffNum);
        Log.i("rex", "" + diffNum);
    }

    private void checkSimilar() {
        boolean isChange = false;
        for (Integer diff : results) {
            if (diff > level) {
                isChange = true;
                break;
            }
        }
        if (callBack != null) {
            callBack.videoDetection(isChange, sourcePath);
        }
    }

}
