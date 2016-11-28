package com.example.zhouzhou.attendancecounter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;

class Embedding {
    private static final String TAG = "OtureoEmbedding";

    ArrayList<Float> val = new ArrayList<Float>();

    Embedding(String content) {
        Scanner scan = new Scanner(content);
        while (scan.hasNextFloat()) {
            this.val.add(scan.nextFloat());
        }
    }
}
