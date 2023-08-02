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
 * @Date ：2023-07-27 22:48
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：旷视（在线版）SDK 操作接口
 * <p></p>
 */
public class MegviiFaceServiceImpl implements FaceRecognitionService {
    private FaceProperties faceProperties;
    private Logger logger;

    public MegviiFaceServiceImpl(FaceProperties faceProperties, Logger logger) {
        this.faceProperties = faceProperties;
        this.logger = logger;
    }
    public MegviiFaceServiceImpl() {
    }


    @Override
    public void initialize() {
        logger.info("开始初始化：旷视服务");
        if (faceProperties == null || !StringUtils.hasText(faceProperties.getApiName()) || faceProperties.getAllowedMultipleBinding() == null ||
                faceProperties.getFilePath() == null || faceProperties.getMegviiProperties() == null) {
            throw new BusinessException("人脸配置信息对象失败（FaceProperties）");
        }
        FaceProperties.MegviiProperties megviiProperties = faceProperties.getMegviiProperties();
        if (megviiProperties.getBindingMinThreshold() == null || megviiProperties.getRecognitionMinThreshold() == null ||
                megviiProperties.getBlur() == null) {
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
    public Float comparisonFace(MultipartFile fileOne, MultipartFile fileTwo) {
        return null;
    }


}
