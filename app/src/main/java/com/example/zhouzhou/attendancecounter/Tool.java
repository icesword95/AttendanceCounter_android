package com.example.zhouzhou.attendancecounter;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import java.lang.Math;
import java.util.Collections;

public class Tool {
    private int test_max_size = 1000;

    //设置overlap阈值参数
    private float threshold = (float) 0.20;   //overlap threshold

    private static final String TAG = "OtureoFaceTool";

    private FaceSet faceset = new FaceSet();

    static {
        System.loadLibrary("face");
    }

    public native int initializeDetecter(AssetManager assetManager, String model);

    public native int initializeRecognizer(AssetManager assetManager, String model);

    private native String detectBmp(Bitmap bitmap, int height, int width,
                                    float scale, int level, float nms_thres, float score_thres);

    private native String recognizeBmp(Bitmap bitmap);

    private ArrayList<Roi> Qoverlap(ArrayList<Roi> dataArray) {
        Collections.sort(dataArray);
        for (int i_first = 0; i_first < dataArray.size(); i_first++) {
            for (int i_next = i_first + 1; i_next < dataArray.size(); ) {
                int cross_x1 = Math.max(dataArray.get(i_first).x1,
                        dataArray.get(i_next).x1);
                int cross_y1 = Math.max(dataArray.get(i_first).y1,
                        dataArray.get(i_next).y1);
                int cross_x2 = Math.min(dataArray.get(i_first).x2,
                        dataArray.get(i_next).x2);
                int cross_y2 = Math.min(dataArray.get(i_first).y2,
                        dataArray.get(i_next).y2);
                int cross_w = Math.max(0, cross_x2 - cross_x1 + 1);
                int cross_h = Math.max(0, cross_y2 - cross_y1 + 1);
                int inter = cross_w * cross_h;
                float over_ratio = (float) inter / (dataArray.get(i_first).area +
                        dataArray.get(i_next).area - (float) inter);

                if (over_ratio >= threshold) {     //overlap ratio 超出阈值 threshold，
                    dataArray.remove(i_next);      //去掉score较小的检测结果
                } else if (over_ratio > 0.0 & inter == dataArray.get(i_next).area) {
                    dataArray.remove(i_next);
                } else {
                    i_next++;
                }
            }
        }
        return dataArray;
    }


    private ArrayList<String> Detect(
            Bitmap bitmap, final int level, float nms_thres, float score_thres) {
        int imageHeight = bitmap.getHeight();
        int imageWidth = bitmap.getWidth();
        int minSize;
        int maxSize;

        if (imageHeight < imageWidth) {
            minSize = imageHeight;
            maxSize = imageWidth;
        } else {
            minSize = imageWidth;
            maxSize = imageHeight;
        }

        float imScale = (float) (level * 100) / minSize;

        if (Math.round(imScale * maxSize) > test_max_size) {
            imScale = (float) test_max_size / (float) maxSize;
        }

        int dstHeight = Math.round(imageHeight * imScale);
        int dstWidth = Math.round(imageWidth * imScale);

        Bitmap dstImg = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);

        String results = detectBmp(dstImg, dstHeight, dstWidth, imScale, level, nms_thres, score_thres);

        final ArrayList<String> recognitions = new ArrayList<String>();

        if (results == null || results.isEmpty()) {
            return recognitions;
        }

        for (final String result : results.split("\n")) {
            recognitions.add(result);
        }

        return recognitions;
    }

    public List<Roi> DetectAndRecognize(final Bitmap bitmap, final int level) {
        final ArrayList<Roi> results = new ArrayList<Roi>();
        final ArrayList<String> rois = Detect(bitmap, 2, (float) 0.3, (float) 0.7);

        for (int i = 0; i < rois.size(); ++i) {
            Roi one_roi = new Roi(rois.get(i), 0, 0);
            Bitmap tmpImg = Bitmap.createBitmap(bitmap, one_roi.x1, one_roi.y1, one_roi.w, one_roi.h);
            Bitmap dstImg = Bitmap.createScaledBitmap(tmpImg, 96, 96, false);
            final String embedding = recognizeBmp(dstImg);
            one_roi.setEmbedding(embedding);
            one_roi.id = faceset.identify(one_roi.embedding);
            results.add(one_roi);
        }
        return results;
    }


}
