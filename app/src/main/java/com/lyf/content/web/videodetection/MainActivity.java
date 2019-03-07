package com.lyf.content.web.videodetection;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.lyf.contents.details.myvideodetection.utils.VideoDetectionUtils;
import com.lyf.contents.details.myvideodetection.utils.VideoDetectionUtils.VideoDetectionCallBack;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_CAMERA = 1023;
    private ListView mListView2;
    private BitmapAdapter mBitmapAdapter;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView2 = (ListView) findViewById(R.id.list_view2);
        mBitmapAdapter = new BitmapAdapter(bitmapArrayList);
        mListView2.setAdapter(mBitmapAdapter);
    }

    public void video(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Uri uri = data.getData();
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    // 视频路径
                    String filePath = cursor
                            .getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
//                    getFrameInterval(filePath);

                    VideoDetectionUtils
                            .checkVideoDetection(filePath, 5, 500, new VideoDetectionCallBack() {
                                @Override
                                public void videoDetection(boolean isChanged, String filePath) {
                                    Toast.makeText(MainActivity.this, isChanged ? "有改变" : "无改变",
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                    cursor.close();
                }
            }
        }
    }

//    private void getFrameInterval(String targetPath) {
//        preRes = null;
//        results.clear();
//        bitmapArrayList.clear();
//        mBitmapAdapter.notifyDataSetChanged();
//        MediaMetadataRetrieverWrapper metadataRetriever2 = new MediaMetadataRetrieverWrapper();
//        metadataRetriever2.setDataSource(targetPath);
//        metadataRetriever2.getFramesInterval(500, 4, new RetrieverProcessThread.BitmapCallBack() {
//            @Override
//            public void onComplete(final Bitmap frame2) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bitmapArrayList.add(frame2);
//                        mBitmapAdapter.notifyDataSetChanged();
//                        checkDiff(frame2);
//                    }
//                });
//            }
//
//            @Override
//            public void onEnd() {
//                checkSimilar();
//            }
//        });
//    }

    private String preRes = null;
    private List<Integer> results = new ArrayList<Integer>(10);

//    private void checkDiff(Bitmap target) {
//        Bitmap bitmap8 = ThumbnailUtils.extractThumbnail(target, 8, 8);
//        Bitmap bitmap = SimilarPicture.convertGreyImg(bitmap8);
//        int avg = SimilarPicture.getAvg(bitmap);
//        String binary = SimilarPicture.getBinary(bitmap,avg);
//        String result = SimilarPicture.binaryString2hexString(binary);
//        if(preRes != null){
//            diff(preRes,result);
//        }
//        preRes = result;
//    }

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
        for (Integer diff : results) {
            if (diff > 5) {
                Toast.makeText(MainActivity.this, "有变化", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Toast.makeText(MainActivity.this, "基本没变化", Toast.LENGTH_LONG).show();
    }




    private class BitmapAdapter extends BaseAdapter {

        private ArrayList<Bitmap> bitmaps;

        public BitmapAdapter(ArrayList<Bitmap> bitmaps) {
            this.bitmaps = bitmaps;
        }

        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.simple_image, parent, false);
//            }
//            ((ImageView) convertView.findViewById(R.id.image))
//                    .setImageBitmap(getItem(position));
            return convertView;
        }
    }


}
