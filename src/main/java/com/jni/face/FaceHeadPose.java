package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import com.jni.struct.HeadPose;

/**
 * 
 * @ 人脸姿态角检测示例
 *
 */
public class FaceHeadPose {
    // 人脸姿态角示例
    public int imageFaceHeadPose() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        HeadPose[] headList = Face.faceHeadPose(matAddr);
        if (headList == null || headList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < headList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 上下偏转角
            System.out.println("pitch angle is:" + headList[i].pitch);
            // 与人脸平行平面内的头部旋转角
            System.out.println("roll angle is:" + headList[i].roll);
            // 左右偏转角
            System.out.println("yaw angle is:" + headList[i].yaw);
        }       
        return 0;
    }
}
