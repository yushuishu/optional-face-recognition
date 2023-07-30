package com.jni.face;

import java.util.Scanner;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import com.jni.struct.LivenessInfo;

/**
 * 
 * @ 多线程示例（sdk的多线程仅支持多实例的多线程，同样的接口调用，需要另外一个实例的才能在不同的线程里面使用，
 * 同时，因为多实例的多线程，所以就需要多个sdk初始化和销毁，sdk支持最多3个实例，第二个实例方法接口名字都同
 * 第一个，但第二个在方法后带2，第三个实例方法接口带3，定义如Face.java所示）
 * 
 *
 */
public class MultiThread {
       
    public void imageFaceFeature(long addr) {
       
        // type 0： 表示rgb 生活照特征值 1: 表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        FeatureInfo[] feaList = Face.faceFeature2(addr, type);

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
          //  FaceDraw.drawRects(mat, box);
        }
        
        // 取完特征值后可把绘制人脸框的图片保存本地
       // Imgcodecs.imwrite("feature.jpg", mat);
       
    } 
    
    // rgb 图片活体检测
    public int faceLivenessByRgbImage(long addr) {
        // rgb liveness        
        LivenessInfo[] liveInfos = Face.rgbLiveness3(addr);
        if (liveInfos == null || liveInfos.length <= 0) {
            System.out.printf("detect no face");
            return -1;
        }
        FaceBox boxInfo = liveInfos[0].box;
        if (boxInfo == null) {
            System.out.println("detect no face");
            return -1;
        }
        float liveScore = liveInfos[0].livescore;
        // 活体置信度分值
        System.out.println("face liveness score is:" + liveScore);
        // 人脸框宽度
        System.out.println("face width is:" + boxInfo.width);
        // 人脸框高度
        System.out.println("face height is:" + boxInfo.height);       
        // 人脸框中心x坐标
        System.out.println("face center x is:" + boxInfo.centerx);
        // 人脸框中心y坐标
        System.out.println("face center y is:" + boxInfo.centery);
        // 人脸置信度
        System.out.println("face score is:" + boxInfo.score);
        
        return 0;        
    }
    
    public class RunnableA implements Runnable{
        @Override
        public void run(){
            Mat mat = Imgcodecs.imread("images/1.jpg");
            long matAddr = mat.getNativeObjAddr();
            // 取特征值          
            for (int i = 0; i < 10000; i++) {
                imageFaceFeature(matAddr); 
                System.out.println("thread1 index is:" + i);
            }
            mat.release();  // 用完释放，防止内存泄漏
        }
    }
    
    public class RunnableB implements Runnable{
        @Override
        public void run(){
            // 人脸检测
            FaceDetect detect = new FaceDetect();
            for (int i = 0; i < 10000; i++) {
                detect.imageDetect();
                System.out.println("thread2 index is:" + i);
            }            
        }
    }
    
    public class RunnableC implements Runnable{
        @Override
        public void run(){
            // 人脸活体检测
            Mat rgbMat = Imgcodecs.imread("images/rgb.png");
            long rgbMatAddr = rgbMat.getNativeObjAddr();
            for (int i = 0; i < 10000; i++) {
                faceLivenessByRgbImage(rgbMatAddr);
                System.out.println("thread3 index is:" + i);
            }
            rgbMat.release();  // 用完释放，防止内存泄漏
        }
    }
    
    // 多线程示例
    public int multiThreadDemo() {
        String modelPath ="";
        Face api2 = new Face();
        // 初始化多实例2
        System.out.printf("\nthread2 sdk_init\n");
        api2.sdkInit2(modelPath);
        // 初始化多实例3
        Face api3 = new Face();
        System.out.printf("thread3 sdk_init\n");
        api3.sdkInit3(modelPath);
        // opencv的mat并不是线程安全的，用完需要释放，同一个图片若同时imread可能出错
        RunnableA runa = new RunnableA();
        Thread t1=new Thread(runa);
        t1.start();
        
        RunnableB runb = new RunnableB();
        Thread t2=new Thread(runb);
        t2.start();
        
        RunnableC runc = new RunnableC();
        Thread t3=new Thread(runc);
        t3.start();
        // 回车后继续执行       
        Scanner input = new Scanner(System.in);
        input.next();
        
        // 销毁多实例2
        api2.sdkDestroy2();
        // 销毁多实例3
        api3.sdkDestroy3();
        return 0;
    }
}
