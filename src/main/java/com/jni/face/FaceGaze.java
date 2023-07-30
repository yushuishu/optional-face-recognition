package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import com.jni.struct.GazeInfo;

/**
 * 
 * @ 注意力检测示例
 *
 */
public class FaceGaze {
    // 注意力检测示例
    public int imageFaceGaze() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();       
        GazeInfo[] gazeList = Face.faceGaze(matAddr);
        if (gazeList == null || gazeList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        // 输出的含义请参考GazeInfo类定义说明或doc目录的文档说明
        for (int i = 0; i < gazeList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 左眼注意力方向
            System.out.println("left_eye_direction:" + gazeList[i].leftEyeDirection);
            // 左眼注意力置信度
            System.out.println("left eye conf:" + gazeList[i].leftEyeConf);
            // 右眼注意力方向
            System.out.println("right eye direction:" + gazeList[i].rightEyeDirection);
            // 右眼注意力置信度
            System.out.println("right eye conf:" + gazeList[i].rightEyeConf);
        }
        
        return 0;
    }
}
