package com.jni.face;

import java.awt.Dimension;

import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

/**
 * 
 * @ 人脸比对及特征值示例
 *
 */
public class FaceCompare {
    
    public void testFaceCompare() {
        // 通过图片帧进行人脸1:1比对  
         imageMatch();
        // 通过特征值进行人脸1:1比对        
         imageFaceMatch();
         // 通过提取图片的特征值和视频帧提取特征值实时比对
        // videoFaceMatch();
        // 测试人脸1：N识别(和预加载的库比较)
        // faceIdentifyWithDb();
        // 测试人脸1：N识别(和实时查找的库比较)
        // faceIdentify();        
    }
    // 通过图片帧1:1对比
    public void imageMatch() {
        Mat mat1 = Imgcodecs.imread("images/1.jpg");
        long mat1Addr = mat1.getNativeObjAddr();        
        // 获取第二个人的特征值
        Mat mat2 = Imgcodecs.imread("images/2.jpg");
        long mat2Addr = mat2.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;    
        float score =  Face.match(mat1Addr, mat2Addr, type);
        System.out.println("face match score is:" + score);
    }

    // 1:1人脸比对比接口 (通过特征值比对)
    public void imageFaceMatch() {
        // 获取第一个人的特征值
        Mat mat1 = Imgcodecs.imread("images/1.jpg");
        long mat1Addr = mat1.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        FeatureInfo[] fea1List = Face.faceFeature(mat1Addr, type);
        if (fea1List == null || fea1List.length <= 0) {
            System.out.println("get feature fail");
            return;
        }
        
        // 获取第二个人的特征值
        Mat mat2 = Imgcodecs.imread("images/2.jpg");
        long mat2Addr = mat2.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        FeatureInfo[] fea2List = Face.faceFeature(mat2Addr, type);
        if (fea2List == null || fea2List.length <= 0) {
            System.out.println("get feature fail");
            return;
        }
        Feature f1 = fea1List[0].feature;
        Feature f2 = fea2List[0].feature;        
        float score =  Face.compareFeature(f1, f2, type);
        System.out.println("face match score is:" + score);

    }

    // 图片和视频帧比对
    public void videoFaceMatch() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id
        VideoCapture capture = new VideoCapture();
        capture.open(0);

        if (!capture.isOpened()) {
            System.out.println("could not load video data...");
            return;
        }
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat mat1 = Imgcodecs.imread("images/1.jpg");
        long mat1Addr = mat1.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        // 提取要比对的图片特征值
        FeatureInfo[] feaList1 = Face.faceFeature(mat1Addr, type);
        Feature fea1 = feaList1[0].feature;
      
        Mat frame = new Mat();
        while (true) {
            boolean have = capture.read(frame);
            Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                break;
            }
            if (!frame.empty()) {              
                long matAddr = frame.getNativeObjAddr();
                // 提取要比对的图片特征值
                FeatureInfo[] feaList = Face.faceFeature(matAddr, type);
                
                if (feaList != null && feaList.length > 0) {
                    // 获取特征值
                    Feature fea = feaList[0].feature;
                    // 人脸比对
                    float score = Face.compareFeature(fea1, fea, type);
                    System.out.println("compare score is:" + score);
                    FaceBox box = feaList[0].box;
                    // 绘制人脸框
                    FaceDraw.drawRects(frame, box);
                }
                gui.imshow(ShowVideo.conver2Image(frame));
                
                gui.repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }

 // 1:N比对(传图片帧和库里的N比对，库为比对时候实时查找,人脸库参考FaceManager)
    public void faceIdentifyByMat() {
         Mat mat1 = Imgcodecs.imread("images/1.jpg");
         long mat1Addr = mat1.getNativeObjAddr();
         // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
         int type = 0;
         // 用户id，可自定义
         String userId = "user";
         // 组id，可自定义
         String groupId = "group";
         // 和人脸库里面的人脸特征值比较（人脸识别）
         String res = Face.identifyByMat(mat1Addr, groupId, userId, type);     
         System.out.println("identify res is:" + res);
    }
    
    // 1:N比对(传特征值和库里的N比对，库为比对时候实时查找,人脸库参考FaceManager)
    public void faceIdentify() {
         Mat mat1 = Imgcodecs.imread("images/1.jpg");
         long mat1Addr = mat1.getNativeObjAddr();
         // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
         int type = 0;
         // 提取要比对的图片特征值
         FeatureInfo[] feaList1 = Face.faceFeature(mat1Addr, type);
         Feature fea1 = feaList1[0].feature;
         // 用户id，可自定义
         String userId = "user";
         // 组id，可自定义
         String groupId = "group";
         // 和人脸库里面的人脸特征值比较（人脸识别）
         String res = Face.identify(fea1, groupId, userId, type);     
         System.out.println("identify res is:" + res);
    }

    // 1:N比对(传图片帧和库里的N比对，库为比对时候先提前加载,人脸库参考FaceManager)
    public void faceIdentifyWithDbByMat() {
        // 提前加载数据库（和全库比较，所以可先把全部库加载到内存,和全库比较，该句必须先调用）
        Face.loadDbFace();
        // 提取第一个人脸特征值
        Mat mat1 = Imgcodecs.imread("images/1.jpg");
        long mat1Addr = mat1.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        // 和人脸库里面的人脸特征值比较（人脸识别）
        String res = Face.identifyWithAllByMat(mat1Addr, type);     
        System.out.println("identify res is:" + res);
    }  
    
    // 1:N比对(传特征值和库里的N比对，库为比对时候先提前加载,人脸库参考FaceManager)
    public void faceIdentifyWithDb() {
        // 提前加载数据库（和全库比较，所以可先把全部库加载到内存,和全库比较，该句必须先调用）
        Face.loadDbFace();
        // 提取第一个人脸特征值
        Mat mat1 = Imgcodecs.imread("images/1.jpg");
        long mat1Addr = mat1.getNativeObjAddr();
        // type 0： 表示rgb生活照特征值，1:表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        // 提取要比对的图片特征值
        FeatureInfo[] feaList1 = Face.faceFeature(mat1Addr, type);
        Feature fea1 = feaList1[0].feature;
        // 和人脸库里面的人脸特征值比较（人脸识别）
        String res = Face.identifyWithAll(fea1, type);     
        System.out.println("identify res is:" + res);
    }   
}
