package com.shuishu.face.strategy.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:23
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸service
 * <p></p>
 */
public interface FaceRecognitionService {
    /**
     * 人脸SDK初始化
     */
    void initialize();

    /**
     * 添加人脸图片
     *
     * @param multipartFile 人脸图片原文件
     * @return -
     */
    Map<String, String> addFace(MultipartFile multipartFile);
}
