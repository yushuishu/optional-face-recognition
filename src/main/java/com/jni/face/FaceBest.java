package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * 
 * @ 最佳人脸
 *
 */
public class FaceBest {
    // 最佳人脸
    public int imageFaceBest() {
        Mat mat = Imgcodecs.imread("images/multi.jpg");
        long matAddr = mat.getNativeObjAddr();
        float[] bestList = Face.faceBest(matAddr);
        
        if (bestList == null || bestList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < bestList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 最佳人脸置信度分值
            System.out.println("best face score is:" + bestList[i]);
        }     
        return 0;
    }
}
