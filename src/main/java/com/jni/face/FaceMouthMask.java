package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * 
 * @ 口罩佩戴检测
 *
 */
public class FaceMouthMask {
    // 口罩佩戴检测示例
    public int imageFaceMouthMask() {
        Mat mat = Imgcodecs.imread("images/mask.jpg");
        long matAddr = mat.getNativeObjAddr();
        float[] maskList = Face.faceMouthMask(matAddr);
        
        if (maskList == null || maskList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < maskList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 口罩佩戴置信度分值
            System.out.println("mouth mask score is:" + maskList[i]);
        }
        return 0;
    }
}
