package com.jni.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;

/**
 * 
 * @绘制类
 *
 */
public class ShowVideo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }    
    public static RotatedRect boundingBox(int[] landmarks, int size) {
        int minX = 1000000;
        int minY = 1000000;
        int maxX = -1000000;
        int maxY = -1000000;
        for (int i = 0; i < size / 2; ++i) {
            minX = (minX < landmarks[2 * i] ? minX : landmarks[2 * i]);
            minY = (minY < landmarks[2 * i + 1] ? minY : landmarks[2 * i + 1]);
            maxX = (maxX > landmarks[2 * i] ? maxX : landmarks[2 * i]);
            maxY = (maxY > landmarks[2 * i + 1] ? maxY : landmarks[2 * i + 1]);
        }
        int width = ((maxX - minX) + (maxY - minY)) / 2;
        float angle = 0;
        Point center = new Point((minX + maxX) / 2, (minY + maxY) / 2);
        return new RotatedRect(center, new Size(width, width), angle);
    }

    public static void drawRotatedBox(Mat img, RotatedRect box, Scalar color) {
        Point[] vertices = new Point[4];
        box.points(vertices);
        for (int j = 0; j < 4; j++) {
            Imgproc.line(img, vertices[j], vertices[(j + 1) % 4], color);
        }
    }

    public static BufferedImage conver2Image(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int dims = mat.channels();
        int[] pixels = new int[width * height];
        byte[] rgbdata = new byte[width * height * dims];
        mat.get(0, 0, rgbdata);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int index = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (dims == 3) {
                    index = row * width * dims + col * dims;
                    b = rgbdata[index] & 0xff;
                    g = rgbdata[index + 1] & 0xff;
                    r = rgbdata[index + 2] & 0xff;
                    pixels[row * width + col] =
                            ((255 & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | b & 0xff;
                }
                if (dims == 1) {
                    index = row * width + col;
                    b = rgbdata[index] & 0xff;
                    pixels[row * width + col] =
                            ((255 & 0xff) << 24) | ((b & 0xff) << 16) | ((b & 0xff) << 8) | b & 0xff;
                }
            }
        }
        setRGB(image, 0, 0, width, height, pixels);
        return image;
    }

    public static void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

}
