package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * 
 * @嘴巴闭合检测
 *
 */
public class FaceMouthClose {
    // 嘴巴闭合示例
    public int imageFaceMouthClose() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        float[] mouthList = Face.faceMouthClose(matAddr);
        
        if (mouthList == null || mouthList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < mouthList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 嘴巴闭合置信度分值
            System.out.println("mouth close score is:" + mouthList[i]);
        }
        return 0;
    }
}
