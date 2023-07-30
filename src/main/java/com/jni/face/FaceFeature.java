package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;

/**
 * 
 * @ 特征值提取
 *
 */
public class FaceFeature {
    // 特征值提取示例 (特征值通常为512个byte)
    public void imageFaceFeature() {
        Mat mat = Imgcodecs.imread("images/1.jpg");
        long matAddr = mat.getNativeObjAddr();
        // type 0： 表示rgb 生活照特征值 1: 表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        FeatureInfo[] feaList = Face.faceFeature(matAddr, type);

        if (feaList == null || feaList.length <= 0) {
            System.out.println("get feature fail");
            return;
        }
        
        // save to binary file
        int size = feaList[0].feature.size;
        System.out.println("size is:" + size);
        
        for (int i = 0; i < feaList.length; i++) {
            Feature fea = feaList[i].feature;
            System.out.println("fea size is:" + fea.size);
            for (int j = 0; j < fea.size; j++) {
                System.out.println("fea is:" + fea.data[j]);
            }
            FaceBox box = feaList[i].box;
            // 绘制人脸框
            FaceDraw.drawRects(mat, box);
        }
        
        // 取完特征值后可把绘制人脸框的图片保存本地
       // Imgcodecs.imwrite("feature.jpg", mat);
        mat.release(); // 用完释放，防止内存泄漏
    }    
}
