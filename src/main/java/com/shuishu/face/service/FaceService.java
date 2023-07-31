package com.shuishu.face.service;


import com.shuishu.face.common.entity.dto.*;
import com.shuishu.face.common.entity.vo.FaceVO;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:37
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别 业务接口
 * <p></p>
 */
public interface FaceService {
    /**
     * 查询人脸
     *
     * @param faceQueryDTO -
     * @return -
     */
    List<FaceVO> findFaceList(FaceQueryDTO faceQueryDTO);

    /**
     * 添加人脸
     * 可以一次性的添加多个读者的人脸绑定，如果barcode为NULL，就将上传的每个图片名作为人脸图片的读者证
     *
     * @param faceAddDTO -
     * @return -
     */
    List<FaceVO> addFace(FaceAddDTO faceAddDTO);

    /**
     * 更新人脸
     * 可以一次性的更新多个读者的人脸绑定，，如果barcode为NULL，就将上传的每个图片名作为人脸图片的读者证
     *
     * @param faceUpdateDTO -
     * @return -
     */
    List<FaceVO> updateFace(FaceUpdateDTO faceUpdateDTO);

    /**
     * 人脸识别
     *
     * @param faceRecognitionDTO -
     * @return -
     */
    FaceVO recognize(FaceRecognitionDTO faceRecognitionDTO);

    /**
     * 人脸图书比对
     *
     * @param faceComparisonFaceDTO -
     * @return -
     */
    Float comparisonFace(FaceComparisonFaceDTO faceComparisonFaceDTO);

    /**
     * 删除读者证所有的人脸绑定数据
     *
     * @param faceDeleteDTO -
     */
    void deleteFace(FaceDeleteDTO faceDeleteDTO);
}
