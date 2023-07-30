package com.jni.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import com.jni.struct.FaceBox;
import com.jni.struct.Landmarks;
import com.jni.struct.TrackFaceInfo;


/**
 * 
 * @ 绘制人脸框
 *
 */
public class FaceDraw {
    /**
     *  画人脸框
     * @param img
     * @param info
     * @return
     */
    public static int drawRects(Mat img, FaceBox[] info){
        if (info == null || info.length <= 0) {
            return -1;
        }
        int faceNum = info.length;
        Scalar color = new Scalar(0, 255, 0);
        for (int i = 0; i < faceNum; i++){
            int x = (int) (info[i].centerx - info[i].width / 2.0);
            int y = (int) (info[i].centery - info[i].height / 2.0);
            int w = (int) (info[i].width);
            int h = (int) (info[i].height);
            Point p1 = new Point(x,y);
            Point p2 = new Point(x + w, y + h);
            Imgproc.rectangle(img, p1, p2, color);           
        }
        return 0;
    }
    
    /**
     *  画人脸框
     * @param img
     * @param info
     * @return
     */
    public static int drawRects(Mat img, FaceBox info){
        if (info == null) {
            return -1;
        }
      
        Scalar color = new Scalar(0, 255, 0);       
        int x = (int) (info.centerx - info.width / 2.0);
        int y = (int) (info.centery - info.height / 2.0);
        int w = (int) (info.width);
        int h = (int) (info.height);
        Point p1 = new Point(x,y);
        Point p2 = new Point(x + w, y + h);
        Imgproc.rectangle(img, p1, p2, color);                  
        return 0;
    }

    /**
     *  画人脸框
     * @param img
     * @param trackinfo
     * @return
     */
    public static int drawRects(Mat img, TrackFaceInfo[] trackinfo){          
        if (trackinfo == null || trackinfo.length <= 0) {
            return -1;
        }
        int faceNum = trackinfo.length;
        
        Scalar color = new Scalar(0, 255, 0);
        for (int i = 0; i < faceNum; i++){
            int x = (int) (trackinfo[i].box.centerx - trackinfo[i].box.width / 2.0);
            int y = (int) (trackinfo[i].box.centery - trackinfo[i].box.height / 2.0);
            int w = (int) (trackinfo[i].box.width);
            int h = (int) (trackinfo[i].box.height);
            Point p1 = new Point(x,y);
            Point p2 = new Point(x + w, y + h);
            Imgproc.rectangle(img, p1, p2, color);  
        }
        return 0;
    }
    
    /**
     *  画人脸框
     * @param img
     * @param trackinfo
     * @return
     */
    public static int drawRects(Mat img, TrackFaceInfo trackinfo){          
        if (trackinfo == null) {
            return -1;
        }
        Scalar color = new Scalar(0, 255, 0);       
        int x = (int) (trackinfo.box.centerx - trackinfo.box.width / 2.0);
        int y = (int) (trackinfo.box.centery - trackinfo.box.height / 2.0);
        int w = (int) (trackinfo.box.width);
        int h = (int) (trackinfo.box.height);
        Point p1 = new Point(x,y);
        Point p2 = new Point(x + w, y + h);
        Imgproc.rectangle(img, p1, p2, color);       
        return 0;
    }
    
    /**
     *  画人脸关键点
     * @param img
     * @param trackinfo
     * @return
     */
    public static int drawShape(Mat img, TrackFaceInfo[] trackinfo){
        if (trackinfo == null || trackinfo.length <= 0) {
            return -1;
        }
        int faceNum = trackinfo.length;
        int faceid = 0;
        Scalar color = new Scalar(0, 255, 255);
        Scalar color2 = new Scalar(0, 0, 255);
        for (int i = 0; i < faceNum; ++i){
            int pointSize = trackinfo[i].land.size / 2;
            int radius = 2;
            faceid = (int) trackinfo[i].faceId;
            for (int j = 0; j < pointSize; ++j){
                Point p = new Point((int) trackinfo[i].land.data[j * 2], (int) trackinfo[i].land.data[j * 2 + 1]);
                Imgproc.circle(img, p, radius, color);
            }
            if (pointSize == 72){
                int components = 9;
                int[] comp1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
                int[] comp2 = { 13, 14, 15, 16, 17, 18, 19, 20, 13, 21 };
                int[] comp3 = { 22, 23, 24, 25, 26, 27, 28, 29, 22 };
                int[] comp4 = { 30, 31, 32, 33, 34, 35, 36, 37, 30, 38 };
                int[] comp5 = { 39, 40, 41, 42, 43, 44, 45, 46, 39 };
                int[] comp6 = { 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 47 };
                int[] comp7 = { 51, 57, 52 };
                int[] comp8 = { 58, 59, 60, 61, 62, 63, 64, 65, 58 };
                int[] comp9 = { 58, 66, 67, 68, 62, 69, 70, 71, 58 };
                int[][] idx = { comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9 };
                int[] npoints = { 13, 10, 9, 10, 9, 11, 3, 9, 9 };
        
                for (int m = 0; m < components; ++m){
                    for (int n = 0; n < npoints[m] - 1; ++n){
                        Point p1 = new Point(trackinfo[i].land.data[idx[m][n] * 2], 
                                trackinfo[i].land.data[idx[m][n] * 2 + 1]);
                        Point p2 = new Point(trackinfo[i].land.data[idx[m][n + 1] * 2],
                                trackinfo[i].land.data[idx[m][n + 1] * 2 + 1]);
                        Imgproc.line(img, p1, p2, color2);
                    }
                }
            }
                String sfaceid ="" + faceid;
                double fontscale = 2;
                Point pos = new Point(trackinfo[i].box.centerx, trackinfo[i].box.centery);
                Imgproc.putText(img, sfaceid, pos, radius, fontscale, color2);           
            }
           
            return 0;
    }
    
    /**
     *  画人脸关键点
     * @param img
     * @param markList
     * @return
     */
    public static int drawLandmark(Mat img, Landmarks[] markList){
        if (markList == null || markList.length <= 0) {
            return -1;
        }
        int faceNum = markList.length;
        Scalar color = new Scalar(0, 255, 255);
        Scalar color2 = new Scalar(0, 0, 255);
        for (int i = 0; i < faceNum; ++i){
            int pointSize = markList[i].size / 2;
            int radius = 2;
            for (int j = 0; j < pointSize; ++j){
                Point p = new Point((int) markList[i].data[j * 2], (int) markList[i].data[j * 2 + 1]);
                Imgproc.circle(img, p, radius, color);              
            }
            if (pointSize == 72){
                int components = 9;
                int[] comp1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
                int[] comp2 = { 13, 14, 15, 16, 17, 18, 19, 20, 13, 21 };
                int[] comp3 = { 22, 23, 24, 25, 26, 27, 28, 29, 22 };
                int[] comp4 = { 30, 31, 32, 33, 34, 35, 36, 37, 30, 38 };
                int[] comp5 = { 39, 40, 41, 42, 43, 44, 45, 46, 39 };
                int[] comp6 = { 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 47 };
                int[] comp7 = { 51, 57, 52 };
                int[] comp8 = { 58, 59, 60, 61, 62, 63, 64, 65, 58 };
                int[] comp9 = { 58, 66, 67, 68, 62, 69, 70, 71, 58 };
                int[][] idx = { comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9 };
                int[] npoints = { 13, 10, 9, 10, 9, 11, 3, 9, 9 };

                for (int m = 0; m < components; ++m){
                    for (int n = 0; n < npoints[m] - 1; ++n){
                        Point p1 = new Point(markList[i].data[idx[m][n] * 2], markList[i].data[idx[m][n] * 2 + 1]);
                        Point p2 = new Point(markList[i].data[idx[m][n + 1] * 2], 
                                markList[i].data[idx[m][n + 1] * 2 + 1]);
                        Imgproc.line(img, p1, p2, color2);
                    }
                }
            }
            
        }
        return 0;
    }
    
    /**
     *  画人脸关键点
     * @param img
     * @param tracklist
     * @return
     */
    public static int drawLandmark(Mat img, TrackFaceInfo[] tracklist){
        if (tracklist == null || tracklist.length <= 0) {
            return -1;
        }
        int faceNum = tracklist.length;
        Scalar color = new Scalar(0, 255, 255);
        Scalar color2 = new Scalar(0, 0, 255);
        Scalar color3 = new Scalar(255, 0, 255);
        for (int i = 0; i < faceNum; ++i){
            int pointSize = tracklist[i].land.size / 2;
            int radius = 2;
            for (int j = 0; j < pointSize; ++j){
                Point p = new Point((int) tracklist[i].land.data[j * 2], (int) tracklist[i].land.data[j * 2 + 1]);
                Imgproc.circle(img, p, radius, color);              
            }
            if (pointSize == 72){
                int components = 9;
                int[] comp1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
                int[] comp2 = { 13, 14, 15, 16, 17, 18, 19, 20, 13, 21 };
                int[] comp3 = { 22, 23, 24, 25, 26, 27, 28, 29, 22 };
                int[] comp4 = { 30, 31, 32, 33, 34, 35, 36, 37, 30, 38 };
                int[] comp5 = { 39, 40, 41, 42, 43, 44, 45, 46, 39 };
                int[] comp6 = { 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 47 };
                int[] comp7 = { 51, 57, 52 };
                int[] comp8 = { 58, 59, 60, 61, 62, 63, 64, 65, 58 };
                int[] comp9 = { 58, 66, 67, 68, 62, 69, 70, 71, 58 };
                int[][] idx = { comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9 };
                int[] npoints = { 13, 10, 9, 10, 9, 11, 3, 9, 9 };

                for (int m = 0; m < components; ++m){
                    for (int n = 0; n < npoints[m] - 1; ++n){
                        Point p1 = new Point(tracklist[i].land.data[idx[m][n] * 2], 
                                tracklist[i].land.data[idx[m][n] * 2 + 1]);
                        Point p2 = new Point(tracklist[i].land.data[idx[m][n + 1] * 2],
                                tracklist[i].land.data[idx[m][n + 1] * 2 + 1]);
                        Imgproc.line(img, p1, p2, color2);
                    }
                }
            }
            
            String sfaceid = "" + tracklist[i].faceId;          
            // double font_scale = 2;
            // int thickness = 18;
            int baseline = 4;
            Point pos = new Point(tracklist[i].box.centerx, tracklist[i].box.centery);
            // System.out.println("face id =" + sfaceid);
            Imgproc.putText(img, sfaceid, pos, Core.FONT_HERSHEY_PLAIN, baseline, color3);
        }
        return 0;
    }
   
}
