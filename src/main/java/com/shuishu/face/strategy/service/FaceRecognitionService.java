package com.shuishu.face.strategy.service;


import com.shuishu.face.common.entity.bo.FaceBO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * 添加 人脸
     *
     * @param multipartFile 人脸图片原文件
     * @return -
     */
    FaceBO addFace(MultipartFile multipartFile);

    /**
     * 批量添加多个 人脸
     *
     * @param multipartFileList -
     */
    List<FaceBO> addFaceList(List<MultipartFile> multipartFileList);
}
