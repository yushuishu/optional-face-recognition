package com.jni.struct;

/**
 * 
 * @ 人脸关键点
 *
 */
public class Landmarks {
    public int index; // 关键点索引
    public int size; // 关键点大小
    public float []data; // 关键点数据，144个点，72个x，y坐标
    public float score; // 关键点分值
}
