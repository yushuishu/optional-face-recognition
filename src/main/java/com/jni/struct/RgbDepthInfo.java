package com.jni.struct;

/**
 * 
 * @ rgb&depth 双目活体分值+人脸信息
 *
 */
public class RgbDepthInfo {
    public FaceBox box; // 人脸跟踪框信息
    public float rgbscore; // rgb可见光活体分值
    public float depthscore; // depth深度活体分值
}
