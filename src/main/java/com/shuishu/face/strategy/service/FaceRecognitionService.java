package com.shuishu.face.strategy.service;


import com.shuishu.face.common.entity.bo.FaceBO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
     * 添加 人脸
     *
     * @param libraryCode   -馆code
     * @param barcode       -读者证
     * @param multipartFile -人脸图片原文件
     * @return -
     */
    FaceBO addFace(String libraryCode, String barcode, MultipartFile multipartFile);

    /**
     * 批量添加多个 人脸
     *
     * @param libraryCode       -馆code
     * @param multipartFileList -人脸图片原文件
     * @return -
     */
    List<FaceBO> addFaceList(String libraryCode, List<MultipartFile> multipartFileList);

    /**
     * 删除人脸（解绑人脸）
     *
     * @param libraryCode          -馆code
     * @param barcode              -读者证
     * @param originalImageUrlList -原图片本地路径
     * @param cropImageUrlList     -剪切图片本地路径
     * @param isConfineAllSuccess  -true：所有cropImageUrlList图片必须全部删除成功，才返回成功
     *                             false：cropImageUrlList其中有一张图片删除成功，就返回成功（人脸库保存的cropImageUrl）
     * @return -true：成功  false：失败
     */
    boolean deleteFace(String libraryCode, String barcode, List<String> originalImageUrlList, List<String> cropImageUrlList, boolean isConfineAllSuccess);

    /**
     * 人脸识别
     *
     * @param faceFeatureMap -馆code
     * @param faceFeatureSizeMap -馆code
     * @param multipartFile -人脸图片
     * @return -
     */
    List<FaceBO> recognize(Map<Long, byte[]> faceFeatureMap, Map<Long, Integer> faceFeatureSizeMap, MultipartFile multipartFile);

    /**
     * 图片比对
     *
     * @param fileOne -人脸图片1
     * @param fileTwo -人脸图片2
     * @return -比对得分，满分100分，一般认为80分以上认为是同一个人脸
     */
    Integer comparisonFace(MultipartFile fileOne, MultipartFile fileTwo);
}
