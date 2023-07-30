package com.jni.struct;

/**
 * 
 * @ 人脸框信息
 *
 */
public class FaceBox{
    public int index; // 人脸框索引
    public float centerx; // 人脸框中心x坐标
    public float centery;  // 人脸框中心y坐标
    public float width; // 人脸框宽度
    public float height; // 人脸框高度
    public float score; // 人脸置信度分值
}