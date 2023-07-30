package com.jni.struct;

/**
 * 
 * @人脸跟踪结构体
 *
 */
public class TrackFaceInfo{
    public int faceId; // 人脸id
    public FaceBox box; // 人脸框信息
    public Landmarks land; // 人脸关键点
}