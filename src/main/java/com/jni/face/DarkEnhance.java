package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @function: 暗光恢复
 *
 */
public class DarkEnhance {
    // 暗光恢复
    public int imageDarkEnhance() {
        Mat mat = Imgcodecs.imread("images/mask.jpg");
        long matAddr = mat.getNativeObjAddr();
        // 定义输出mat
        Mat outMat = new Mat();
        long outAddr = outMat.getNativeObjAddr();
        int res = Face.darkEnhance(matAddr, outAddr);              
        if (res != 0) {
            System.out.println("darkenhance error and res is:" + res);
            return res;
        }
        try {
            if (outMat.empty()) {
                System.out.println("dark enhance fail");
                return -1;
            }
        }catch (Exception e) {
            // 未检测到人脸或其他原因导致sdk无图片返回
            System.out.println("outMat excepiton");
            return -1;
        }
        // 图片可保存到本地
        Imgcodecs.imwrite("dakenhance.jpg", outMat);      
        return 0;
    }
}
