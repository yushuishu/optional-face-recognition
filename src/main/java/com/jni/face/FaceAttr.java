package com.jni.face;

import org.opencv.core.Mat;

import com.jni.struct.Attribute;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @ 获取人脸属性的类及demo
 *
 */
public class FaceAttr {

    // 人脸属性示例
    public int imageFaceAttribute() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        Attribute[] attrList = Face.faceAttr(matAddr);
        if (attrList == null || attrList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        // 输出可参考Attribute的定义说明或doc目录的文档说明
        for (int i = 0; i < attrList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 年龄
            System.out.println("age is:" + attrList[i].age);
            // 表情
            System.out.println("emotion is:" + attrList[i].emotion);
            // 性别
            System.out.println("gender is:" + attrList[i].gender);
            // 种族
            System.out.println("race is:" + attrList[i].race);
            // 是否佩戴眼镜
            System.out.println("glasses is:" + attrList[i].glasses);
        }        
        return 0;
    }
}
