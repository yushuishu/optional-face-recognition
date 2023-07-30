package com.jni.face;

import java.awt.Dimension;

import com.jni.struct.FaceBox;
import com.jni.struct.LivenessInfo;
import com.jni.struct.RgbDepthInfo;
import com.jni.struct.TrackFaceInfo;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * 
 * @静默活体类
 *
 */
public class FaceLiveness {

    // 适配ir,rgb双目深度活体检测镜头
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public int faceLivenessByImage() {
        // rgb 图片活体检测
        faceLivenessByRgbImage();
        // nir 图片活体检测
        // faceLivenessByNirImage();
        return 0;
    }
    // rgb 图片活体检测
    public int faceLivenessByRgbImage() {
        // rgb liveness
        Mat rgbMat = Imgcodecs.imread("images/rgb.png");
        long rgbMatAddr = rgbMat.getNativeObjAddr();
        LivenessInfo[] liveInfos = Face.rgbLiveness(rgbMatAddr);
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
        rgbMat.release(); // 用完释放，防止内存泄漏
        return 0;        
    }
    // nir 图片活体检测
    public int faceLivenessByNirImage() {
        // nir liveness
        Mat nirMat = Imgcodecs.imread("images/nir.png");
        long nirMatAddr = nirMat.getNativeObjAddr();
        LivenessInfo[] liveInfos = Face.nirLiveness(nirMatAddr);
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
      
    // usb摄像头视频静默活体检测示例
    public int usbVideoRgbLiveness() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id
        VideoCapture capture = new VideoCapture();
        // 打开device 0
        capture.open(0);
        if (!capture.isOpened()) {
            System.out.println("could not open camera...");
            return -1;
        }
        // type 0： 表示rgb 人脸检测 1：表示nir人脸检测
        int type = 0;
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat frame = new Mat();
        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        // 设置循环结束条件
        int maxCount = 100000;
        int index = 0;
        while (index <= maxCount) {
            index ++;
            boolean have = capture.read(frame);
           // Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                continue;
            }
            if (!frame.empty()) {
                long matAddr = frame.getNativeObjAddr();
               
                LivenessInfo[] liveInfos = Face.rgbLiveness(matAddr);
                
                if (liveInfos == null || liveInfos.length <= 0) {
                    System.out.printf("detect no face");
                    continue;
                }
              
                // 检测到人脸
                if (liveInfos != null && liveInfos.length > 0) { 
                    for (int j = 0; j < liveInfos.length; j++) {
                        FaceBox info = liveInfos[j].box;
                        float score = liveInfos[j].livescore;
                        // 画人脸框
                        FaceDraw.drawRects(frame, info);  
                        // 画活体分值
                        String sScore ="" + score;
                        double fontScale = 2;
                        Point pos = new Point(20, 100);
                        Imgproc.putText(frame, sScore, pos, radius, fontScale, color);   
                    }
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
        return 0;
    }

    // 可见光+红外双目活体检测
    public void rgbNirLiveness() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id(另外也可设备管理器中禁用电脑自带摄像头)
        VideoCapture cap1 = new VideoCapture();
        // 打开device 0
        cap1.open(0);
        if (!cap1.isOpened()) {
            System.out.println("could not load video1 data...");
            return;
        }
        VideoCapture cap2 = new VideoCapture();
        // 打开device 1
        cap2.open(1);
        if (!cap2.isOpened()) {
            System.out.println("could not load video2 data...");
            return;
        }
        int width1 = (int) cap1.get(3);
        int height1 = (int) cap1.get(4);
        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("frame1", new Dimension(width1, height1));
        Mat frame1 = new Mat();

        int width2 = (int) cap2.get(3);
        int height2 = (int) cap2.get(4);
        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("frame2", new Dimension(width2, height2));
        Mat frame2 = new Mat();
        int maxTrack = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        while (!stop) {
            boolean have1 = cap1.read(frame1);
            Core.flip(frame1, frame1, 1); // Win上摄像头
            if (!have1) {
                break;
            }

            boolean have2 = cap2.read(frame2);
            Core.flip(frame2, frame2, 1); // Win上摄像头
            if (!have2) {
                break;
            }

            if (!frame1.empty() && !frame2.empty()) {
               
                System.out.println("get frame ---");
                long matAddr1 = frame1.getNativeObjAddr();
                long matAddr2 = frame2.getNativeObjAddr();
                // 请区分rgb和ir对应的frame(参数第一个为rgb的视频帧，第二个为ir的视频帧)
                LivenessInfo[] rgbLives = Face.rgbLiveness(matAddr1);
                LivenessInfo[] irLives = Face.nirLiveness(matAddr2);
                if (rgbLives != null && irLives != null && rgbLives.length > 0 
                        && irLives.length > 0) {
                    for (int j = 0; j < rgbLives.length; j++) {
                        FaceBox info  = rgbLives[j].box;
                        float rgbScore = rgbLives[j].livescore;
                        // 画人脸框
                        FaceDraw.drawRects(frame1, info);  
                        // 画rgb活体分值
                        String sRgbScore ="rgb:" + rgbScore;
                        double fontScale = 2;
                        Point pos1 = new Point(20, 100);
                        Imgproc.putText(frame1, sRgbScore, pos1, radius, fontScale, color);
                    }
                    for (int j = 0; j < irLives.length; j++) {
                        float nirScore = irLives[j].livescore;
                        // 画ir活体分值
                        String sNirScore ="nir:" + nirScore;
                        double fontScale = 2;
                        Point pos1 = new Point(20, 100);
                        Imgproc.putText(frame2, sNirScore, pos1, radius, fontScale, color);  
                    }
                }

                gui1.imshow(ShowVideo.conver2Image(frame1));
                gui1.repaint();
                
                gui2.imshow(ShowVideo.conver2Image(frame2));
                gui2.repaint();
                index++;
                if (index > maxTrack) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 奥比中光双目摄像头深度活体
    public void rgbDepthLivenessByOrbe() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // 打开摄像头或者视频文件 

        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("depth", new Dimension(640, 480));

        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("rgb", new Dimension(640, 480));

        Mat cvRgb = new Mat(640, 480, CvType.CV_8UC3);
        Mat cvDepth = new Mat(640, 480, CvType.CV_16UC1);
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();

        int ok = Orbe.openOrbe();
        if (ok != 0) {
            System.out.println("camera open fail!");
            return;
        }
        int maxTrack = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        while (!stop) {
            int bopen = Orbe.cameraOrbe(rgbAddr, depthAddr);
            if (bopen != 0) {
                System.out.println("read frame fail!");
                continue;
            } 

            if (!cvRgb.empty() && !cvDepth.empty()) {
                rgbAddr = cvRgb.getNativeObjAddr();
                depthAddr = cvDepth.getNativeObjAddr();
                 
                RgbDepthInfo[] infos = Face.rgbAndDepthLiveness(rgbAddr, depthAddr);
                 
                if (infos != null && infos.length > 0) {
                    for (int j = 0; j < infos.length; j++) {
                        FaceBox info  = infos[j].box;
                        float rgbScore = infos[j].rgbscore;
                        float depthScore = infos[j].depthscore;
                        // 画人脸框
                        FaceDraw.drawRects(cvRgb, info);  
                        // 画rgb活体分值
                        String sRgbScore ="rgb:" + rgbScore;
                        double fontScale = 2;
                        Point pos1 = new Point(20, 100);
                        Imgproc.putText(cvRgb, sRgbScore, pos1, radius, fontScale, color); 
                        // 画depth活体分值
                        String sDepthScore ="depth:" + depthScore;                  
                        Point pos2 = new Point(20, 100);
                        Imgproc.putText(cvDepth, sDepthScore, pos2, radius, fontScale, color); 
                    }
                }
                
                Mat newDetph = new Mat();
                cvDepth.convertTo(newDetph, CvType.CV_8UC3);
                gui1.imshow(ShowVideo.conver2Image(newDetph));
                gui1.repaint();

                // 显示rgb图像
                gui2.imshow(ShowVideo.conver2Image(cvRgb));
                gui2.repaint();
                index++;
                if (index > maxTrack) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Orbe.closeOrbe();
                e.printStackTrace();
            }

        }
        // 释放镜头资源
        Orbe.closeOrbe(); 
    }

    // 华杰艾米双目摄像头深度活体(适应于华捷艾米A-200)
    public void rgbDepthLivenessByHjimi() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // 打开摄像头或者视频文件 
        
        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("depth", new Dimension(480, 640));
        
        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("rgb", new Dimension(480, 640));
        
        Mat cvRgb = new Mat(480, 640, CvType.CV_8UC3);
        Mat cvDepth = new Mat(480, 640, CvType.CV_16UC1);
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();
        
        int ok = Aimi.openHjimi();
        if (ok != 0) {
            System.out.println("camera open fail!");
            return;
        }
        int maxTrack = 100000; // 假设的结束条件
        int index = 0;
        Scalar color = new Scalar(0, 255, 255);
        int radius = 2;
        boolean stop = false;
        while (!stop) {
            int bopen = Aimi.cameraHjimi(rgbAddr, depthAddr);
            if (bopen != 0) {
                System.out.println("read frame fail!");
                continue;
            } 
            if (!cvRgb.empty() && !cvDepth.empty()) {
                rgbAddr = cvRgb.getNativeObjAddr();
                depthAddr = cvDepth.getNativeObjAddr();
                 
                RgbDepthInfo[] infos = Face.rgbAndDepthLiveness(rgbAddr, depthAddr);
                 
                if (infos != null && infos.length > 0) {
                    for (int j = 0; j < infos.length; j++) {
                        FaceBox info  = infos[j].box;
                        float rgbScore = infos[j].rgbscore;
                        float depthScore = infos[j].depthscore;
                        // 画人脸框
                        FaceDraw.drawRects(cvRgb, info);  
                        // 画rgb活体分值
                        String sRgbScore ="rgb:" + rgbScore;
                        double fontScale = 2;
                        Point pos1 = new Point(20, 100);
                        Imgproc.putText(cvRgb, sRgbScore, pos1, radius, fontScale, color); 
                        // 画depth活体分值
                        String sDepthScore ="depth:" + depthScore;                  
                        Point pos2 = new Point(20, 100);
                        Imgproc.putText(cvDepth, sDepthScore, pos2, radius, fontScale, color); 
                     }
                 }

                 Mat newDetph = new Mat();
                 cvDepth.convertTo(newDetph, CvType.CV_8UC3);
                 gui1.imshow(ShowVideo.conver2Image(newDetph));
                 gui1.repaint();

                 // 显示rgb图像
                 gui2.imshow(ShowVideo.conver2Image(cvRgb));
                 gui2.repaint();
                 index++;
                 if (index > maxTrack) {
                     stop = true;
                 }
             }
             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 Aimi.closeHjimi();
                 e.printStackTrace();
             }

         }
         // 释放镜头资源
        Aimi.closeHjimi(); 
    }
}
