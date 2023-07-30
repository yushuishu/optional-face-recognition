package com.shuishu.face.strategy.service.impl;


import com.shuishu.face.common.entity.bo.FaceBO;
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


    @Override
    public void initialize() {

    }

    @Override
    public FaceBO addFace(MultipartFile multipartFile) {
        return null;
    }

    @Override
    public List<FaceBO> addFaceList(List<MultipartFile> multipartFileList) {
        return null;
    }

}
