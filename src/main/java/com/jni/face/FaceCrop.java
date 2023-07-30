package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * 
 * @ 人脸抠图
 *
 */
public class FaceCrop {
    // 人脸抠图示例
    public int imageFaceCrop() {
        Mat mat = Imgcodecs.imread("images/1.jpg");
        long matAddr = mat.getNativeObjAddr();
        // 定义输出mat
        Mat outMat = new Mat();
        long outAddr = outMat.getNativeObjAddr();
      
        int res = Face.faceCrop(matAddr, outAddr);
        if (res != 0) {
            System.out.println("crop error and res is:" + res);
            return res;
        }
                        
        try {
            if (outMat.empty()) {
                System.out.println("face crop fail");
                return -1;
            }
        }catch (Exception e) {
            // 未检测到人脸或其他原因导致sdk无图片返回
            System.out.println("outMat empty exception");
            return -1; 
        }
        // 抠图完毕可保存到本地
        Imgcodecs.imwrite("crop.jpg", outMat); 
        return 0;
    }
}
