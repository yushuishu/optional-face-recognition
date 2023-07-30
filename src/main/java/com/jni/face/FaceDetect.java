package com.jni.face;

import java.awt.Dimension;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import com.jni.struct.FaceBox;

/**
 * 
 * @ 人脸检测示例demo
 *
 */
public class FaceDetect {
    // usb摄像头视频检测示例
    public int usbVideoDetect() {
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
        int type = 0;
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
           // Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                continue;
            }
            if (!frame.empty()) {
                long matAddr = frame.getNativeObjAddr();
               
                FaceBox[] infos = Face.detect(matAddr, type);
                // 检测到人脸
                if (infos != null && infos.length > 0) {   
                    // 画人脸框
                    FaceDraw.drawRects(frame, infos);                   
                }
                gui.imshow(ShowVideo.conver2Image(frame));               
                gui.repaint();
            }           
        }
        return 0;
    }
       
    // 人脸检测示例（检测图片)
    public int imageDetect() {   
        // Mat mat = ImageBuf.getImageMat("images/1.jpg"); 根据图片路径，读buf示例
        Mat mat = Imgcodecs.imread("images/2.jpg");      
        if (mat.empty()) {
            System.out.println("image not exist or empty");
            return -1;
        }
        long matAddr = mat.getNativeObjAddr();
        // type 0： 表示rgb 人脸检测 1：表示nir人脸检测
        int type = 0;
        FaceBox[] boxList = Face.detect(matAddr, type);
        if (boxList == null || boxList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < boxList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 人脸框宽度
            System.out.println("face width is:" + boxList[i].width);
            // 人脸框高度
            System.out.println("face height is:" + boxList[i].height);           
            // 人脸框中心x坐标
            System.out.println("face center x is:" + boxList[i].centerx);
            // 人脸框中心y坐标
            System.out.println("face center y is:" + boxList[i].centery);
            // 人脸置信度
            System.out.println("face score is:" + boxList[i].score);
        }
        // 绘制
        FaceDraw.drawRects(mat, boxList);
        // 保存到本地
        Imgcodecs.imwrite("detect.jpg", mat);  
        mat.release(); // 用完释放，防止内存泄漏
        return 0;
    }
}
