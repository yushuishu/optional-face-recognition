package com.jni.face;

import java.awt.Dimension;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.jni.struct.FaceBox;
/**
 * 
 * @动作活体
 *
 */
public class FaceActionLive {
    // usb摄像头动作活体示例
    public int usbVideoActionLive() {
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
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat frame = new Mat();
        // 动作类型，0为眨眨眼，其他的如注释。
        int actionType = 0; // 0:眨眨眼 1：张张嘴 2：点点头 3：摇摇头 4：抬头 5：向左转 6：向右转
        // 定义动作活体结果1维数组，0为未做动作，1为存在该动作
        int[] actionResult = new int[1];
        // 初始化为0
        actionResult[0] = 0;
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
                
                FaceBox[] infos = Face.faceActionLive(matAddr, actionType, actionResult);
                // 检测到人脸
                if (infos != null && infos.length > 0) {   
                    // 画人脸框
                    FaceDraw.drawRects(frame, infos);                   
                }
                // 为1，存在该动作
                if (actionResult[0] == 1) {
                    System.out.println("--------has action---------");
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
}
