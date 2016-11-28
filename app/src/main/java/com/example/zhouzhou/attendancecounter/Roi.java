package com.example.zhouzhou.attendancecounter;

import java.util.Scanner;

public class Roi implements Comparable<Roi> {
    public int x1, y1, x2, y2, w, h;
    public float score, area;
    public Embedding embedding;
    public int id;

    Roi(int x1, int y1, int x2, int y2, float score, float area) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.w = this.x2 - this.x1;
        this.h = this.y2 - this.y1;
        this.score = score;
        this.area = area;
        this.id = -1;
    }

    Roi(String content, int x_offset, int y_offset) {
        Scanner scan = new Scanner(content);
        //获取抠出的小图做检测所得到的检测目标的左上角坐标（x,y）
        this.x1 = scan.nextInt() + x_offset;
        this.y1 = scan.nextInt() + y_offset;
        this.x2 = scan.nextInt() + x_offset;
        this.y2 = scan.nextInt() + y_offset;
        this.w = this.x2 - this.x1;
        this.h = this.y2 - this.y1;
        this.score = scan.nextFloat();
        this.area = (y2 - y1 + 1) * (x2 - x1 + 1);
        this.id = -1;
    }

    public void setEmbedding(String content) {
        this.embedding = new Embedding(content);
    }

    @Override
    public int compareTo(Roi roi) {
        if (this.score > roi.score) {
            return -1;
        } else if (this.score < roi.score) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        return String.valueOf(this.x1) + " "
                + String.valueOf(this.y1) + " "
                + String.valueOf(this.x2) + " "
                + String.valueOf(this.y2) + " "
                + String.valueOf(this.score) + " "
                + String.valueOf(this.id);
    }
}
