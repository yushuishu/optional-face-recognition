package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.jni.struct.Occlusion;
/**
 * 
 * @ 遮挡度检测
 *
 */
public class FaceOcclusion {
    // 遮挡度检测示例
    public int imageFaceOcclusion() {
        Mat mat = Imgcodecs.imread("images/mask.jpg");
        long matAddr = mat.getNativeObjAddr();
        Occlusion[] occlList = Face.faceOcclusion(matAddr);
        
        if (occlList == null || occlList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < occlList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 左眼遮挡置信度分值
            System.out.println("left_eye occlu score is:" + occlList[i].leftEye);
            // 右眼遮挡置信度分值
            System.out.println("right_eye occlu score is:" + occlList[i].rightEye);
            // 鼻子遮挡置信度分值
            System.out.println("nose occlu score is:" + occlList[i].nose);
            // 嘴巴遮挡置信度分值
            System.out.println("mouth occlu score is:" + occlList[i].mouth);
            // 左脸遮挡置信度分值
            System.out.println("left_cheek occlu score is:" + occlList[i].leftCheek);
            // 右脸遮挡置信度分值
            System.out.println("right_cheek occlu score is:" + occlList[i].rightCheek);
            // 下巴遮挡置信度分值
            System.out.println("chin occlu score is:" + occlList[i].chin);
        }
        
        return 0;
    }
}
