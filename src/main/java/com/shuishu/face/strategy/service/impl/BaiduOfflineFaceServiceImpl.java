package com.shuishu.face.strategy.service.impl;


import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.entity.bo.FaceBO;
import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:38
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：百度离线人脸识别接口
 * <p></p>
 */
public class BaiduOfflineFaceServiceImpl implements FaceRecognitionService {
    private FaceProperties faceProperties;
    public BaiduOfflineFaceServiceImpl(FaceProperties faceProperties) {
        this.faceProperties = faceProperties;
    }
    public BaiduOfflineFaceServiceImpl() {
    }


    @Override
    public void initialize() {
        if (faceProperties == null) {
            throw new BusinessException("初始化人脸配置信息对象失败（FaceProperties）");
        }
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
    public List<FaceBO> recognize(String libraryCode, MultipartFile file) {
        return null;
    }

    @Override
    public Integer comparisonFace(MultipartFile fileOne, MultipartFile fileTwo) {
        return null;
    }


}
