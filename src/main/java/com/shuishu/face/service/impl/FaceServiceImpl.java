package com.shuishu.face.service.impl;


import com.shuishu.face.common.config.face.service.FaceRecognitionService;
import com.shuishu.face.service.FaceService;
import org.springframework.stereotype.Service;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:46
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别 业务接口
 * <p></p>
 */
@Service
public class FaceServiceImpl implements FaceService {

    private final FaceRecognitionService faceRecognitionService;
    public FaceServiceImpl(FaceRecognitionService faceRecognitionService) {
        this.faceRecognitionService = faceRecognitionService;
    }




}
