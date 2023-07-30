package com.jni.face;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageBuf {
    
    /**
     *  通过字节流byte转mat示例
     * @param imgPath
     * @return
     */
    public static Mat getImageMat(String imgPath) {
        Mat mat = null;
        File f = new File(imgPath);
        BufferedImage bi;      
        try {      
            bi = ImageIO.read(f);      
            ByteArrayOutputStream baos = new ByteArrayOutputStream();      
            ImageIO.write(bi, "jpg", baos);      
            byte[] bytes = baos.toByteArray();   
            mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);  
        } catch (IOException e) {      
            e.printStackTrace();      
        }             
        return mat;
    }
    
    /**
     * 从文件路径获取字节流byte示例
     * @param imgPath
     * @return
     */
    public static byte[] getImageBuffer(String imgPath) {
        File f = new File(imgPath);
        BufferedImage bi;      
        try {      
            bi = ImageIO.read(f);      
            ByteArrayOutputStream baos = new ByteArrayOutputStream();      
            ImageIO.write(bi, "jpg", baos);      
            byte[] bytes = baos.toByteArray();      
            return bytes;      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        return null; 
    }
    
    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}