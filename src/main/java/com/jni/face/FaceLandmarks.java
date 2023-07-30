package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import com.jni.struct.Landmarks;
/**
 * 
 * @ 获取人脸关键点
 *
 */
public class FaceLandmarks {
    // 获取人脸关键点示例
    public int imageFaceLandmarks() {
        Mat mat = Imgcodecs.imread("images/multi.jpg");
        long matAddr = mat.getNativeObjAddr();
        // type:(0表示rgb人脸关键点，采用accurate模型，1表示rgb人脸关键点，采用fast模型，2表示ir人脸关键点，采用accurate模型)
        int type = 0;
        Landmarks[] landList = Face.faceLandmark(matAddr, type);
        if (landList == null || landList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < landList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
        }
        FaceDraw.drawLandmark(mat, landList);
        // 图片可保存到本地
        Imgcodecs.imwrite("landmark.jpg", mat); 
        return 0;
    }
}
