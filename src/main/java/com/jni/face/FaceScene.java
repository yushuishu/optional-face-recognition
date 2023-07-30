package com.jni.face;

import java.awt.Dimension;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import com.jni.struct.LiveFeatureInfo;

/**
 * 
 * @ 场景化示例
 *
 */
public class FaceScene {
    // usb摄像头视频检测示例
    public int videoLivenessFeature() {
        
        Mat mat = Imgcodecs.imread("images/1.jpg");
        long matAddr = mat.getNativeObjAddr();
        // type 0： 表示rgb 人脸检测 1：表示nir人脸检测
        int type = 0;
        FeatureInfo[] feaList = Face.faceFeature(matAddr, type);

        if (feaList == null || feaList.length <= 0) {
            System.out.println("get feature fail");
            return 0;
        }
        // save to binary file
        int size = feaList[0].feature.size;
        System.out.println("size is:" + size);
        
        Feature feaJpg = feaList[0].feature;
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id
        VideoCapture capture = new VideoCapture();
        capture.open(0);
        if (!capture.isOpened()) {
            System.out.println("could not open camera...");
            return -1;
        }
       // type 0： 表示rgb 人脸检测 1：表示nir人脸检测
       
        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat frame = new Mat();  
        // 设置循环结束条件
        int maxCount = 100000;
        int index = 0;
        while (index <= maxCount) {
            index ++;
            boolean have = capture.read(frame);         
            if (!have) {
                continue;
            }
            if (!frame.empty()) {
                matAddr = frame.getNativeObjAddr();
               
                LiveFeatureInfo[] infos = Face.livenessFeature(matAddr, type);
                // 检测到人脸
                if (infos != null && infos.length > 0) { 
                    for (int j = 0; j < infos.length; j++) {
                        FaceBox box = infos[j].box;
                        // 活体分值
                        float liveScore = infos[j].score;
                        System.out.println("liveness score is:" + liveScore);
                        // 特征值
                        Feature fea = infos[j].feature;
                        
                        float compScore = Face.compareFeature(fea, feaJpg, 0);
                        
                        System.out.println("compare score is:" + compScore);
                        /*
                        for(int k=0;k<fea.size;k++) {
                            System.out.println("fea is:" + fea.data[k]);
                        } */
                        // 画人脸框
                        FaceDraw.drawRects(frame, box); 
                        // 画活体分值
                        String sScore ="score:" + liveScore;
                        double fontScale = 2;
                        Point pos = new Point(20, 100);
                        Imgproc.putText(frame, sScore, pos, radius, fontScale, color); 
                    }
                }
                gui.imshow(ShowVideo.conver2Image(frame));               
                gui.repaint();
                frame.release();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
