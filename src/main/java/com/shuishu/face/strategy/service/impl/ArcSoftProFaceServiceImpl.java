package com.shuishu.face.strategy.service.impl;


import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.fastjson2.JSON;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.entity.bo.FaceBO;
import com.shuishu.face.common.entity.bo.arc.AttributeBO;
import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.common.utils.FileUtils;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.strategy.utils.ArcSoftProUtils;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 11:13
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：虹软（增值版）SDK 操作接口
 * <p></p>
 */
public class ArcSoftProFaceServiceImpl implements FaceRecognitionService {
    private FaceProperties faceProperties;
    private Logger logger;
    private FaceEngine faceEngine;


    public ArcSoftProFaceServiceImpl(FaceProperties faceProperties, Logger logger) {
        this.faceProperties = faceProperties;
        this.logger = logger;
    }

    public ArcSoftProFaceServiceImpl() {
    }


    @Override
    public void initialize() {
        logger.info("================================= 开始初始化：虹软（增值版）服务 =================================");
        if (faceProperties == null || !StringUtils.hasText(faceProperties.getApiName()) || faceProperties.getAllowedMultipleBinding() == null ||
                faceProperties.getFilePath() == null || faceProperties.getArcSoftProProperties() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        FaceProperties.ArcSoftProProperties arcSoftProProperties = faceProperties.getArcSoftProProperties();
        if (arcSoftProProperties.getBindingMinThreshold() == null || arcSoftProProperties.getRecognitionMinThreshold() == null ||
                arcSoftProProperties.getBlur() == null || arcSoftProProperties.getAppId() == null ||arcSoftProProperties.getSdkKey() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        // 激活码和激活文件不能同时为空
        if (arcSoftProProperties.getActiveKey() == null && arcSoftProProperties.getActiveFilePath() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }

        // 获取引擎
        FaceEngine faceEngine = new FaceEngine(arcSoftProProperties.getLibPath());
        int responseCode = 0;

        // 激活引擎
        if (arcSoftProProperties.getActiveKey() != null) {
            responseCode = faceEngine.activeOnline(arcSoftProProperties.getAppId(), arcSoftProProperties.getSdkKey(), arcSoftProProperties.getActiveKey());
            if (responseCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue() && responseCode != ErrorInfo.MOK.getValue()) {
                logger.error("引擎在线激活异常，code：{}", responseCode);
            }
        }
        // 采集设备信息
        ActiveDeviceInfo activeDeviceInfo = new ActiveDeviceInfo();
        responseCode = faceEngine.getActiveDeviceInfo(activeDeviceInfo);
        logger.info("采集设备信息responseCode：{}", responseCode);
        logger.info("设备信息：{}", activeDeviceInfo.getDeviceInfo());
        // 离线激活文件
        if (StringUtils.hasText(arcSoftProProperties.getActiveFilePath())) {
            File file = new File(arcSoftProProperties.getActiveFilePath());
            if (file.exists()) {
                responseCode = faceEngine.activeOffline(arcSoftProProperties.getActiveFilePath());
                logger.info("离线激活responseCode：{}", responseCode);
            } else {
                logger.error("激活文件不存在");
            }
        }
        // 激活文件激活
        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        responseCode = faceEngine.getActiveFileInfo(activeFileInfo);
        logger.info("引擎激活文件激活responseCode：" + responseCode);
        logger.info("激活文件信息：" + activeFileInfo);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        functionConfiguration.setSupportImageQuality(true);
        functionConfiguration.setSupportMaskDetect(true);
        functionConfiguration.setSupportUpdateFaceData(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        //初始化引擎
        responseCode = faceEngine.init(engineConfiguration);
        logger.info("初始化引擎errorCode：{}", responseCode);
        VersionInfo version = faceEngine.getVersion();
        logger.info("版本：{}", version);
        this.faceEngine = faceEngine;

        logger.info("======================================= 服务初始化结束 =======================================");
    }

    @Override
    public FaceBO addFace(String libraryCode, String barcode, MultipartFile multipartFile) {
        // 校验检测人脸
        FaceInfo faceInfo = verifyFaceDetect(multipartFile, true);
        // 获取人脸属性
        ImageInfo imageInfo = ArcSoftProUtils.getImageInfo(multipartFile);
        AttributeBO attributeBO = ArcSoftProUtils.imageAttr(faceEngine, imageInfo, faceInfo);
        FaceBO faceBO = new FaceBO();
        faceBO.setAge(attributeBO.age);
        faceBO.setGender(attributeBO.gender);
        // 获取特征值
        FaceFeature faceFeature = ArcSoftProUtils.imageFeature(faceEngine, imageInfo, faceInfo, 2);
        faceBO.setFeatureSize(faceFeature.getFeatureData().length);
        faceBO.setFeatureByte(faceFeature.getFeatureData());
        faceBO.setFeatureData(JSON.toJSONString(faceFeature.getFeatureData()));
        // 重新生成保存的图片全名称
        String imageFullName = FileUtils.generateImageName(multipartFile, barcode, libraryCode);
        // 保存原始人脸图片
        String originalImagePath = faceProperties.getOriginalFilePath() + "/" + imageFullName;
        boolean saveFile = FileUtils.saveFile(originalImagePath, multipartFile);
        if (!saveFile) {
            throw new BusinessException("保存人脸图片异常");
        }
        faceBO.setOriginalImageUrl(originalImagePath);
        // 人脸图片剪切
        String cropImagePath = faceProperties.getCropFilePath() + "/" + imageFullName;
        Rect rect = faceInfo.getRect();
        boolean imageCut = FileUtils.imageCut(multipartFile, cropImagePath, rect.getLeft(), rect.getTop(), (rect.getRight() - rect.getLeft()), (rect.getBottom() - rect.getTop()));
        if (!imageCut) {
            throw new BusinessException("剪切人脸图片异常");
        }
        // 保存剪切后的人脸图片
        faceBO.setCropImageUrl(cropImagePath);
        return faceBO;
    }

    @Override
    public List<FaceBO> addFaceList(String libraryCode, List<MultipartFile> multipartFileList) {
        List<FaceBO> faceBOList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            FaceBO faceBO = new FaceBO();
            // 获取读者证（图片名称）
            String barcode = FileNameUtil.getPrefix(multipartFile.getName());
            try {
                faceBO = addFace(libraryCode, barcode, multipartFile);
                faceBO.setCode(0);
            } catch (Exception e) {
                // 失败的读者证，将失败原因提取
                faceBO.setLibraryCode(libraryCode);
                faceBO.setBarcode(barcode);
                faceBO.setCode(1);
                faceBO.setMessage(e.getMessage());
            }
            faceBOList.add(faceBO);
        }
        return faceBOList;
    }

    @Override
    public boolean deleteFace(String libraryCode, String barcode, List<String> originalImageUrlList, List<String> cropImageUrlList, boolean isConfineAllSuccess) {
        int num1 = FileUtils.deleteFileList(originalImageUrlList);
        int num2 = FileUtils.deleteFileList(cropImageUrlList);
        int num3 = num1 + num2;
        if (!isConfineAllSuccess && num3 > 0) {
            return true;
        }
        return isConfineAllSuccess && num3 == (originalImageUrlList.size() + cropImageUrlList.size());
    }

    @Override
    public List<FaceBO> recognize(Map<Long, byte[]> faceFeatureMap, Map<Long, Integer> faceFeatureSizeMap, MultipartFile multipartFile) {
        if (faceFeatureMap.isEmpty() || faceFeatureSizeMap.isEmpty() || multipartFile == null || multipartFile.getSize() == 0) {
            return null;
        }
        // 获取当前图片特征值
        ImageInfo imageInfo = ArcSoftProUtils.getImageInfo(multipartFile);
        List<FaceInfo> faceInfoList = ArcSoftProUtils.imageDetect(faceEngine, imageInfo);
        if (faceInfoList.size() > 1) {
            throw new BusinessException("检测到多张人脸");
        }
        FaceFeature faceFeature = ArcSoftProUtils.imageFeature(faceEngine, imageInfo, faceInfoList.get(0), 2);
        // 配置信息
        FaceProperties.ArcSoftProProperties arcSoftProProperties = faceProperties.getArcSoftProProperties();
        // 响应结果
        List<FaceBO> faceBOList = new ArrayList<>();
        // 与 数据库所有特征值 比较
        Set<Map.Entry<Long, Integer>> entries = faceFeatureSizeMap.entrySet();
        for (Map.Entry<Long, Integer> entry : entries) {
            byte[] data = faceFeatureMap.get(entry.getKey());
            float compareFeature = ArcSoftProUtils.compareFeature(faceEngine, faceFeature.getFeatureData(), data);
            if (compareFeature >= arcSoftProProperties.getRecognitionMinThreshold()) {
                FaceBO faceBO = new FaceBO();
                faceBO.setFaceId(entry.getKey());
                faceBO.setScore(compareFeature);
                faceBOList.add(faceBO);
            }
        }
        return faceBOList;
    }

    @Override
    public Float comparisonFace(MultipartFile fileOne, MultipartFile fileTwo) {
        if (fileOne == null || fileTwo == null) {
            throw new BusinessException("人脸比对，必须上传两张照片");
        }
        FaceFeature faceFeature1 = ArcSoftProUtils.imageFeature(faceEngine, fileOne, 2);
        FaceFeature faceFeature2 = ArcSoftProUtils.imageFeature(faceEngine, fileTwo, 2);
        ArcSoftProUtils.compareFeature(faceEngine, faceFeature1.getFeatureData(), faceFeature2.getFeatureData());
        return null;
    }


    private FaceInfo verifyFaceDetect(MultipartFile file, boolean isBindingOperate) {
        // 先检测人脸可信度
        List<FaceInfo> faceInfoList = ArcSoftProUtils.imageDetect(faceEngine, file);
        if (faceInfoList.size() > 1) {
            // 有多张人脸
            throw new BusinessException("识别到多张人脸，请重新调整位置！");
        }
        FaceInfo faceInfo = faceInfoList.get(0);
        // 光线校验

        // 模糊度检测

        // 遮挡度

        return faceInfoList.get(0);
    }

}
