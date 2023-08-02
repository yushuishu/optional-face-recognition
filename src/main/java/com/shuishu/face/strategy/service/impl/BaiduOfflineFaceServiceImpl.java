package com.shuishu.face.strategy.service.impl;


import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import com.jni.struct.Attribute;
import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.entity.bo.FaceBO;
import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.common.utils.FileUtils;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.strategy.utils.BaiduOfflineUtils;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:38
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：百度（离线版）SDK 操作接口
 * <p></p>
 */
public class BaiduOfflineFaceServiceImpl implements FaceRecognitionService {
    private FaceProperties faceProperties;
    private Logger logger;

    public BaiduOfflineFaceServiceImpl(FaceProperties faceProperties, Logger logger) {
        this.faceProperties = faceProperties;
        this.logger = logger;
    }

    public BaiduOfflineFaceServiceImpl() {
    }


    @Override
    public void initialize() {
        logger.info("================================= 开始初始化：百度（离线版）服务 =================================");
        if (faceProperties == null || !StringUtils.hasText(faceProperties.getApiName()) || faceProperties.getAllowedMultipleBinding() == null ||
                faceProperties.getFilePath() == null || faceProperties.getBaiduOfflineProperties() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        FaceProperties.BaiduOfflineProperties baiduOfflineProperties = faceProperties.getBaiduOfflineProperties();
        if (baiduOfflineProperties.getBindingMinThreshold() == null || baiduOfflineProperties.getRecognitionMinThreshold() == null ||
                baiduOfflineProperties.getBlur() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }

        logger.info("======================================= 服务初始化结束 =======================================");
    }

    @Override
    public FaceBO addFace(String libraryCode, String barcode, MultipartFile multipartFile) {
        // 校验检测人脸
        FaceBox faceBox = verifyFaceDetect(multipartFile, true);
        // 获取人脸属性
        String imageAttrJson = BaiduOfflineUtils.imageAttr(multipartFile);
        if (!StringUtils.hasText(imageAttrJson)) {
            throw new BusinessException("获取人脸属性值异常");
        }
        List<Attribute> attributeList = JSON.parseArray(imageAttrJson, Attribute.class);
        if (attributeList == null || attributeList.size() == 0) {
            throw new BusinessException("获取人脸属性值异常");
        }
        Attribute attribute = attributeList.get(0);
        FaceBO faceBO = new FaceBO();
        faceBO.setAge(attribute.age);
        faceBO.setGender(attribute.gender);
        // 获取特征值
        String featureJson = BaiduOfflineUtils.imageRgbdFeature(multipartFile);
        if (featureJson == null) {
            throw new BusinessException("获取人脸特征值异常");
        }
        Feature feature = JSON.parseObject(featureJson, Feature.class);
        if (feature == null) {
            throw new BusinessException("获取人脸特征值异常");
        }
        faceBO.setFeatureSize(feature.size);
        faceBO.setFeatureByte(feature.data);
        faceBO.setFeatureData(JSONB.toJSONString(feature.data));
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
        int width = Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(String.valueOf(faceBox.width)))));
        int height = Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(String.valueOf(faceBox.height)))));
        int left = Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(String.valueOf(faceBox.centerx)))));
        int top = Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(String.valueOf(faceBox.centery)))));
        boolean imageCut = FileUtils.imageCut(multipartFile, cropImagePath, left, top, width, height);
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
        String imageRgbdFeatureJson = BaiduOfflineUtils.imageRgbdFeature(multipartFile);
        if (!StringUtils.hasText(imageRgbdFeatureJson)) {
            throw new BusinessException("人脸识别失败");
        }
        Feature feature = JSON.parseObject(imageRgbdFeatureJson, Feature.class);
        // 配置信息
        FaceProperties.BaiduOfflineProperties baiduOfflineProperties = faceProperties.getBaiduOfflineProperties();
        // 响应结果
        List<FaceBO> faceBOList = new ArrayList<>();
        // 与 数据库所有特征值 比较
        Set<Map.Entry<Long, Integer>> entries = faceFeatureSizeMap.entrySet();
        for (Map.Entry<Long, Integer> entry : entries) {
            Feature tempFeature = new Feature();
            tempFeature.size = entry.getValue();
            tempFeature.data = faceFeatureMap.get(entry.getKey());
            float compareFeature = BaiduOfflineUtils.compareFeature(feature, tempFeature, 0);
            if (compareFeature >= baiduOfflineProperties.getRecognitionMinThreshold()) {
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
            throw new BusinessException("人脸比对，必须两种照片");
        }
        String featureJson1 = BaiduOfflineUtils.imageRgbdFeature(fileOne);
        String featureJson2 = BaiduOfflineUtils.imageRgbdFeature(fileTwo);
        return BaiduOfflineUtils.compareFeature(featureJson1, featureJson2, 0);
    }


    private FaceBox verifyFaceDetect(MultipartFile file, boolean isBindingOperate) {
        // 先检测人脸可信度
        String imageDetectJson = BaiduOfflineUtils.imageDetect(file);
        if (!StringUtils.hasText(imageDetectJson) || "null".equals(imageDetectJson)) {
            throw new BusinessException("未检测到人脸");
        }
        List<FaceBox> detectList = JSON.parseArray(imageDetectJson, FaceBox.class);
        if (detectList == null || detectList.size() == 0) {
            throw new BusinessException("未检测到人脸");
        }
        if (detectList.size() > 1) {
            // 有多张人脸
            throw new BusinessException("识别到多张人脸，请重新调整位置！");
        }
        FaceBox faceBox = detectList.get(0);
        // 光线校验
        String illumination = BaiduOfflineUtils.imageIllumination(file);
        if (StringUtils.hasText(illumination)) {
            throw new BusinessException(illumination);
        }
        if (isBindingOperate) {
            if (faceBox.score < faceProperties.getBaiduOfflineProperties().getBindingMinThreshold()) {
                // 人脸置信度低，忽略
                throw new BusinessException("置信度过低，请重新调整！");
            }
        } else {
            if (faceBox.score < faceProperties.getBaiduOfflineProperties().getRecognitionMinThreshold()) {
                // 人脸置信度低，忽略
                throw new BusinessException("置信度过低，请重新调整！");
            }
        }
        // 模糊度检测
        float blurTemp = BaiduOfflineUtils.imageBlur(file);
        if (blurTemp > faceProperties.getBaiduOfflineProperties().getBlur()) {
            throw new BusinessException("检测到人脸模糊，请调整识别位置");
        }
        // 遮挡度
        String occlusionInfo = BaiduOfflineUtils.imageOcclusion(file);
        if (StringUtils.hasText(occlusionInfo)) {
            throw new BusinessException(occlusionInfo);
        }
        return faceBox;
    }

}
