package com.shuishu.face.strategy.service.impl;


import com.shuishu.face.strategy.service.FaceRecognitionService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:48
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：旷视人脸识别接口
 * <p></p>
 */
public class MegviiFaceServiceImpl implements FaceRecognitionService {
    @Override
    public void initialize() {

    }

    @Override
    public Map<String, String> addFace(MultipartFile multipartFile) {
        return null;
    }
}