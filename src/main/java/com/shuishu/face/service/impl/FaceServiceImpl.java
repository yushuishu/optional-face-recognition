package com.shuishu.face.service.impl;


import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.dsl.FaceDsl;
import com.shuishu.face.common.entity.dto.*;
import com.shuishu.face.common.repository.FaceRepository;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.common.entity.vo.FaceVO;
import com.shuishu.face.service.FaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:46
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别 业务接口
 * <p></p>
 */
@RequiredArgsConstructor
@Service
public class FaceServiceImpl implements FaceService {
    private final FaceRecognitionService faceRecognitionService;
    private FaceRepository faceRepository;
    private FaceDsl faceDsl;



    @Override
    public List<FaceVO> findFaceList(FaceQueryDTO faceQueryDTO) {
        return faceDsl.findFaceList(faceQueryDTO);
    }

    @Override
    public List<FaceVO> addFace(FaceAddDTO faceAddDTO) {
        if (ObjectUtils.isEmpty(faceAddDTO.getFileList())) {
            throw new BusinessException("绑定人脸图片不能为空");
        }
        List<MultipartFile> fileList = faceAddDTO.getFileList();
        if (StringUtils.hasText(faceAddDTO.getBarcode())) {
            // 获取第一张图片
            MultipartFile multipartFile = fileList.get(0);
            Map<String, String> imageMap = faceRecognitionService.addFace(multipartFile);
        } else {
            // 当前请求是批量绑定人脸，图片名称作为读者证号
            // 批量操作的图片大于10张，就先将图片缓存
            if (fileList.size() > 10) {

            } else {

            }
        }
        return null;
    }

    @Override
    public List<FaceVO> updateFace(FaceUpdateDTO faceUpdateDTO) {
        return null;
    }

    @Override
    public FaceVO recognize(FaceRecognitionDTO faceRecognitionDTO) {
        return null;
    }

    @Override
    public Boolean comparisonFace(FaceComparisonFaceDTO faceComparisonFaceDTO) {
        return null;
    }


}
