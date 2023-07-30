package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @模糊度检测
 *
 */
public class FaceBlur {
    // 模糊度检测示例
    public int imageFaceBlur() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        float[] blurList = Face.faceBlur(matAddr);
        
        if (blurList == null || blurList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < blurList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 模糊度分值
            System.out.println("blur score is:" + blurList[i]);
        }
        
        return 0;
    }
}
