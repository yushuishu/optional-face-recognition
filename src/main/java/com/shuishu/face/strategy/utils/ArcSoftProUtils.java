package com.shuishu.face.strategy.utils;


import com.arcsoft.face.*;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ExtractType;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.entity.bo.arc.AttributeBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 11:12
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：虹软SDK 操作函数
 * <p></p>
 */
public class ArcSoftProUtils {
    private static final Logger logger = LoggerFactory.getLogger(ArcSoftProUtils.class);



    private static void verifyImageInfo(ImageInfo imageInfo) {
        if (imageInfo == null || imageInfo.getImageData() == null || imageInfo.getImageData().length == 0) {
            throw new BusinessException("图片文件处理异常");
        }
    }
    private static void verifyFaceInfo(FaceInfo faceInfo) {
        if (faceInfo == null || faceInfo.getFaceData() == null || faceInfo.getFaceData().length == 0) {
            throw new BusinessException("图片文件处理异常");
        }
    }
    private static void verifyImageInfoAndFaceInfo(ImageInfo imageInfo, FaceInfo faceInfo) {
        verifyImageInfo(imageInfo);
        verifyFaceInfo(faceInfo);
    }


    /**
     * MultipartFile ---》 ImageInfo
     *
     * @param multipartFile -
     * @return -
     */
    public static ImageInfo getImageInfo(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new BusinessException("人脸图像文件不能为空");
        }
        try {
            ImageInfo rgbData = ImageFactory.getRGBData(multipartFile.getInputStream());
            verifyImageInfo(rgbData);
            return rgbData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * File ---》 ImageInfo
     *
     * @param file -
     * @return -
     */
    public static ImageInfo getImageInfo(File file) {
        if (file == null) {
            throw new BusinessException("人脸图像文件不能为空");
        }
        ImageInfo rgbData = ImageFactory.getRGBData(file);
        verifyImageInfo(rgbData);
        return rgbData;
    }

    /**
     * 人脸检测
     *
     * @param faceEngine -
     * @param multipartFile -
     * @return -
     */
    public static List<FaceInfo> imageDetect(FaceEngine faceEngine, MultipartFile multipartFile) {
        List<FaceInfo> faceInfoList = new ArrayList<>();
        int code = faceEngine.detectFaces(getImageInfo(multipartFile), faceInfoList);
        if (code != ErrorInfo.MOK.getValue()) {
            logger.error("人脸检测失败：" + code);
        }
        if (faceInfoList.size() == 0) {
            throw new BusinessException("未检测到人脸");
        }
        logger.info("检测到人脸数：{}", faceInfoList.size());
        return faceInfoList;
    }

    /**
     * 人脸检测
     *
     * @param faceEngine -
     * @param imageInfo -
     * @return -
     */
    public static List<FaceInfo> imageDetect(FaceEngine faceEngine, ImageInfo imageInfo) {
        verifyImageInfo(imageInfo);
        List<FaceInfo> faceInfoList = new ArrayList<>();
        int code = faceEngine.detectFaces(imageInfo, faceInfoList);
        if (code != ErrorInfo.MOK.getValue()) {
            throw new BusinessException("人脸图像检测失败：" + code);
        }
        if (faceInfoList.size() == 0) {
            throw new BusinessException("未检测到人脸");
        }
        logger.info("检测到人脸数：{}", faceInfoList.size());
        return faceInfoList;
    }

    /**
     * 人脸质量检测
     *
     * @param faceEngine -
     * @param imageInfo -
     * @param faceInfo -
     * @return -
     */
    public static float imageQuality(FaceEngine faceEngine, ImageInfo imageInfo, FaceInfo faceInfo) {
        verifyImageInfoAndFaceInfo(imageInfo, faceInfo);
        ImageQuality imageQuality = new ImageQuality();
        int code = faceEngine.imageQualityDetect(imageInfo, faceInfo, 0, imageQuality);
        if (code == ErrorInfo.MOK.getValue()) {
            logger.info("图像质量分数：{}", imageQuality.getFaceQuality());
            return imageQuality.getFaceQuality();
        }
        return 0;
    }

    /**
     * 人脸特征提取
     *
     * @param faceEngine -
     * @param imageInfo -
     * @param faceInfo -
     * @param type -特征提取类型：1注册，2识别
     * @return -
     */
    public static FaceFeature imageFeature(FaceEngine faceEngine, ImageInfo imageInfo, FaceInfo faceInfo, int type) {
        verifyImageInfoAndFaceInfo(imageInfo, faceInfo);
        FaceFeature faceFeature = new FaceFeature();
        int code;
        if (type == 1) {
            // 注册特征提取
            code = faceEngine.extractFaceFeature(imageInfo, faceInfo, ExtractType.REGISTER, 0, faceFeature);
        } else if (type == 2) {
            // 识别特征提取
            code = faceEngine.extractFaceFeature(imageInfo, faceInfo, ExtractType.RECOGNIZE, 0, faceFeature);
        } else {
            throw new BusinessException("人脸特征值，提取类型错误");
        }
        if (code != ErrorInfo.MOK.getValue() || faceFeature.getFeatureData() == null || faceFeature.getFeatureData().length == 0) {
            throw new BusinessException("人脸特征值，提取失败：" + code);
        }
        return faceFeature;
    }

    /**
     * 人脸特征提取
     *
     * @param faceEngine -
     * @param type -特征提取类型：1注册，2识别
     * @return -
     */
    public static FaceFeature imageFeature(FaceEngine faceEngine, MultipartFile multipartFile, int type) {
        ImageInfo imageInfo = getImageInfo(multipartFile);
        verifyImageInfo(imageInfo);
        List<FaceInfo> faceInfoList = imageDetect(faceEngine, imageInfo);
        FaceInfo faceInfo = faceInfoList.get(0);
        verifyFaceInfo(faceInfo);
        FaceFeature faceFeature = new FaceFeature();
        int code;
        if (type == 1) {
            // 注册特征提取
            code = faceEngine.extractFaceFeature(imageInfo, faceInfo, ExtractType.REGISTER, 0, faceFeature);
        } else if (type == 2) {
            // 识别特征提取
            code = faceEngine.extractFaceFeature(imageInfo, faceInfo, ExtractType.RECOGNIZE, 0, faceFeature);
        } else {
            throw new BusinessException("人脸特征值，提取类型错误");
        }
        if (code != ErrorInfo.MOK.getValue() || faceFeature.getFeatureData() == null || faceFeature.getFeatureData().length == 0) {
            throw new BusinessException("人脸特征值，提取失败：" + code);
        }
        return faceFeature;
    }


    /**
     * 人脸特征值 比对
     *
     * @param faceEngine -
     * @param featureData1 -特征值1
     * @param featureData2 -特征值2
     * @return -
     */
    public static float compareFeature(FaceEngine faceEngine, byte[] featureData1, byte[] featureData2) {
        if (featureData1 == null || featureData1.length == 0 || featureData2 == null || featureData2.length == 0) {
            throw new BusinessException("人脸比对失败");
        }
        FaceFeature sourceFaceFeature = new FaceFeature(featureData1);
        FaceFeature targetFaceFeature = new FaceFeature(featureData2);

        FaceSimilar faceSimilar = new FaceSimilar();
        int code = faceEngine.compareFaceFeature(sourceFaceFeature, targetFaceFeature, faceSimilar);
        if (code == ErrorInfo.MOK.getValue()) {
            return faceSimilar.getScore();
        }
        return 0;
    }

    /**
     * 人脸属性检测
     *
     * @param faceEngine -
     * @param imageInfo -
     * @param faceInfo -
     */
    public static AttributeBO imageAttr(FaceEngine faceEngine, ImageInfo imageInfo, FaceInfo faceInfo) {
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportMaskDetect(true);
        int code = faceEngine.process(imageInfo, Collections.singletonList(faceInfo), functionConfiguration);
        if (code  != ErrorInfo.MOK.getValue()) {
            throw new BusinessException("人脸属性检测失败");
        }

        AttributeBO attributeBO = new AttributeBO();

        //性别检测
        List<GenderInfo> genderInfoList = new ArrayList<>();
        code = faceEngine.getGender(genderInfoList);
        if (code  != ErrorInfo.MOK.getValue()) {
            logger.error("性别检测失败：" + code);
        }
        if (genderInfoList.size() == 0) {
            logger.error("性别检测失败：" + code);
        } else {
            int gender = genderInfoList.get(0).getGender();
            logger.info("性别：{}", gender);
            if (gender == 0) {
                // 男
                attributeBO.gender = 1;
            } else if (gender == 1) {
                // 女
                attributeBO.gender = 0;
            }
        }

        //年龄检测
        List<AgeInfo> ageInfoList = new ArrayList<>();
        code = faceEngine.getAge(ageInfoList);
        if (code  != ErrorInfo.MOK.getValue()) {
            logger.error("年龄检测失败：" + code);
        }
        if (ageInfoList.size() == 0) {
            logger.error("年龄检测失败：" + code);
        } else {
            logger.info("年龄：{}", ageInfoList.get(0).getAge());
            attributeBO.age = ageInfoList.get(0).getAge();
        }

        //活体检测
        List<LivenessInfo> livenessInfoList = new ArrayList<>();
        code = faceEngine.getLiveness(livenessInfoList);
        if (code  != ErrorInfo.MOK.getValue()) {
            logger.error("活体检测失败：" + code);
        } else {
            if (livenessInfoList.size() == 0) {
                logger.error("活体检测失败：" + code);
            } else {
                int liveness = livenessInfoList.get(0).getLiveness();
                logger.info("活体：{}", liveness);
                attributeBO.liveness = liveness;
            }
        }

        //口罩检测
        List<MaskInfo> maskInfoList = new ArrayList<>();
        code = faceEngine.getMask(maskInfoList);
        if (code  != ErrorInfo.MOK.getValue()) {
            logger.error("口罩检测失败：" + code);
        } else {
            if (maskInfoList.size() == 0) {
                logger.error("口罩检测失败：" + code);
            } else {
                int mask = maskInfoList.get(0).getMask();
                logger.info("口罩：{}", mask);
                attributeBO.mask = mask;
            }
        }

        return attributeBO;
    }



}
