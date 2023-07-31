package com.shuishu.face.strategy.service.impl;


import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.entity.bo.FaceBO;
import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:41
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：虹软人脸识别接口
 * <p></p>
 */
public class ArcSoftFaceServiceImpl implements FaceRecognitionService {
    private FaceProperties faceProperties;
    private Logger logger;

    public ArcSoftFaceServiceImpl(FaceProperties faceProperties, Logger logger) {
        this.faceProperties = faceProperties;
        this.logger = logger;
    }
    public ArcSoftFaceServiceImpl() {
    }


    @Override
    public void initialize() {
        logger.info("开始初始化：虹软服务");
        if (faceProperties == null || !StringUtils.hasText(faceProperties.getApiName()) || faceProperties.getAllowedMultipleBinding() == null ||
                faceProperties.getFilePath() == null || faceProperties.getArcSoftProperties() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        FaceProperties.ArcSoftProperties arcSoftProperties = faceProperties.getArcSoftProperties();
        if (arcSoftProperties.getBindingMinThreshold() == null || arcSoftProperties.getRecognitionMinThreshold() == null ||
                arcSoftProperties.getBlur() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        logger.info("服务初始化结束");
    }

    @Override
    public FaceBO addFace(String libraryCode, String barcode, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public List<FaceBO> addFaceList(String libraryCode, List<MultipartFile> multipartFileList) {
        return null;
    }

    @Override
    public boolean deleteFace(String libraryCode, String barcode, List<String> originalImageUrlList, List<String> cropImageUrlList, boolean isConfineAllSuccess) {
        return false;
    }

    @Override
    public List<FaceBO> recognize(Map<Long, byte[]> faceFeatureMap, Map<Long, Integer> faceFeatureSizeMap, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public Integer comparisonFace(MultipartFile fileOne, MultipartFile fileTwo) {
        return null;
    }


}
