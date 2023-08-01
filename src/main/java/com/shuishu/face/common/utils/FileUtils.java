package com.shuishu.face.common.utils;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.shuishu.face.common.config.exception.BusinessException;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:22
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：图片工具类
 * <p></p>
 */
public class FileUtils {

    /**
     * 保存文件
     *
     * @param filePath      -文件全路径
     * @param multipartFile -文件
     * @return true 成功  false 失败
     */
    public static boolean saveFile(String filePath, MultipartFile multipartFile) {
        if (multipartFile != null && StringUtils.hasText(filePath)) {
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    System.out.println("创建文件夹异常：" + parentFile.getPath());
                    return false;
                }
            }
            try {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 批量删除文件
     *
     * @param filePathList 文件全路径
     * @return 成功删除的数量
     */
    public static int deleteFileList(List<String> filePathList) {
        int successNumber = 0;
        if (!ObjectUtils.isEmpty(filePathList)) {
            for (String filePath : filePathList) {
                try {
                    File fileFile = new File(filePath);
                    if (fileFile.exists()) {
                        boolean delete = fileFile.delete();
                        if (delete) {
                            successNumber++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return successNumber;
    }

    /**
     * 重新设置文件名称
     *
     * @param multipartFile -文件
     * @param barcode       -读者证
     * @param libraryCode   -馆code
     * @return FACE-xyz-opq-20230801666.jpg
     */
    public static String generateImageName(MultipartFile multipartFile, String barcode, String libraryCode) {
        String suffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if (!StringUtils.hasText(suffix)) {
            suffix = ".jpg";
        }
        if (!StringUtils.hasText(barcode)) {
            throw new BusinessException("条码不能为空");
        }
        if (StringUtils.hasText(libraryCode)) {
            return String.format("FACE-%s-%s-%s", libraryCode, barcode, DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)) + suffix;
        }
        return String.format("FACE-%s-%s", libraryCode, DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)) + suffix;
    }


    /**
     * MultipartFile 转 Mat
     *
     * @param file -
     * @return Mat
     */
    public static Mat convertMultipartFileToMat(MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            return convertBufferedImageToMat(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BufferedImage 转 Mat
     *
     * @param im -
     * @return -
     */
    public static Mat convertBufferedImageToMat(BufferedImage im) {
        im = convertBufferedImageToBufferedImage(im, BufferedImage.TYPE_3BYTE_BGR);
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();
        Mat image = new Mat(im.getHeight(), im.getWidth(), 16);
        image.put(0, 0, pixels);
        return image;
    }

    /**
     * 8-bit RGBA 转换 8-bit RGB
     *
     * @param original  -
     * @param imageType -
     * @return -
     */
    public static BufferedImage convertBufferedImageToBufferedImage(BufferedImage original, int imageType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }
        if (original.getType() == imageType) {
            return original;
        }
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imageType);
        Graphics2D graphics2D = image.createGraphics();
        try {
            graphics2D.setComposite(AlphaComposite.Src);
            graphics2D.drawImage(original, 0, 0, null);
        } finally {
            graphics2D.dispose();
        }
        return image;
    }


    /**
     * 裁剪图片并重新装换大小
     *
     * @param multipartFile -图片文件
     * @param imageFilePath -保存文件全路径
     * @param posX          -x坐标轴
     * @param posY          -y坐标轴
     * @param width         -宽度
     * @param height        -高度
     * @return true：剪切成功，剪切失败
     */
    public static boolean imageCut(MultipartFile multipartFile, String imageFilePath, int posX, int posY, int width, int height) {
        if (multipartFile == null || !StringUtils.hasText(imageFilePath)) {
            return false;
        }
        // MultipartFile 转换 Mat
        Mat mat = convertMultipartFileToMat(multipartFile);
        if (mat == null) {
            System.out.println("MultipartFile转换Mat异常");
            return false;
        }
        try {
            double cols = mat.cols();
            int rows = mat.rows();
            // 校验图像ROI区域是否超过了图像尺寸
            if (width > cols) {
                width = (int) Math.floor(cols);
            }
            if (height > rows) {
                height = (int) Math.floor(rows);
            }
            double col = posX + width;
            if (col > cols) {
                posX = (int) (cols - width);
            }
            double row = posY + height;
            if (row > rows) {
                posY = rows - height;
            }

            Rect rect = new Rect(posX, posY, width, height);
            // 两种方式都可以
            //Mat subMat = new Mat(mat, rect);
            Mat subMat = mat.submat(rect);
            Mat resultMat = new Mat();
            Size size = new Size(500, 500);
            //人脸截图
            Imgproc.resize(subMat, resultMat, size);
            //生成剪切图片文件
            File imageFile = new File(imageFilePath);
            if (!imageFile.exists()) {
                if (!imageFile.mkdirs()) {
                    System.out.println("创建文件异常");
                    return false;
                }
            }
            Imgcodecs.imwrite(imageFilePath, resultMat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
