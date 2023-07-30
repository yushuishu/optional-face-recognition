package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @光照检测
 *
 */
public class FaceIllumination {
    // 光照检测示例
    public int imageFaceIllumination() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        float[] illuList = Face.faceIllumination(matAddr);
        if (illuList == null || illuList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < illuList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 光照分值
            System.out.println("illumination score is:" + illuList[i]);
        }       
        return 0;
    }
}
