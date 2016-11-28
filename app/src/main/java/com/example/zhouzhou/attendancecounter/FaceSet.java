package com.example.zhouzhou.attendancecounter;

import android.util.Log;

import java.util.ArrayList;

class FaceSet {
    private static final String TAG = "OtureoFaceSet";

    ArrayList<ArrayList<Embedding>> set;
    final double threshold = 0.6;

    FaceSet() {
        this.set = new ArrayList<ArrayList<Embedding>>();
    }

    private double caldiff(Embedding a, Embedding b) {
        assert (a.val.size() == b.val.size()) : "Embeddings' size are not same!";
        double diff = 0.0;
        for (int i = 0; i < a.val.size(); ++i) {
            diff += Math.pow((a.val.get(i) - b.val.get(i)), 2);
        }
        return diff;
        //return Math.sqrt(diff);
    }

    private void add(int setid, Embedding embedding) {
        if (set.size() == setid) {
            ArrayList<Embedding> person = new ArrayList<Embedding>();
            person.add(embedding);
            set.add(person);
        } else {
            set.get(setid).add(embedding);
        }
    }

    public int identify(Embedding embedding) {
        for (int i = 0; i < set.size(); ++i) {
            int vote = 0;
            ArrayList<Embedding> person = set.get(i);
            for (int j = 0; j < person.size(); ++j) {
                double diff = caldiff(embedding, person.get(j));
                Log.i(TAG, "diff " + diff);
                if (diff < threshold) {
                    vote += 1;
                    if (vote >= person.size() / 2) {
                        add(i, embedding);
                        return i;
                    }
                }
            }
        }
        add(set.size(), embedding);
        return set.size() - 1;
    }
}
