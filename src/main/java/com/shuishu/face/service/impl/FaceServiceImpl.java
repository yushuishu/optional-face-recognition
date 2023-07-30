package com.shuishu.face.service.impl;


import cn.hutool.core.io.file.FileNameUtil;
import com.shuishu.face.common.config.exception.BusinessException;
import com.shuishu.face.common.dsl.FaceDsl;
import com.shuishu.face.common.entity.bo.FaceBO;
import com.shuishu.face.common.entity.dto.*;
import com.shuishu.face.common.entity.po.Face;
import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.common.repository.FaceRepository;
import com.shuishu.face.common.utils.FileUtils;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.common.entity.vo.FaceVO;
import com.shuishu.face.service.FaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    private final FaceRepository faceRepository;
    private final FaceDsl faceDsl;

    private final FaceProperties faceProperties;


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
            if (!Boolean.TRUE.equals(faceProperties.getAllowedMultipleBinding())) {
                int bindingCount = faceDsl.findBindingCount(faceAddDTO.getLibraryCode(), faceAddDTO.getBarcode());
                if (bindingCount > 0) {
                    throw new BusinessException("您已绑定人脸，不可重复绑定");
                }
            }
            // 获取第一张图片
            MultipartFile multipartFile = fileList.get(0);
            FaceBO faceBO = faceRecognitionService.addFace(multipartFile);
            if (faceBO == null || faceBO.getOriginalImageUrl() == null || faceBO.getCropImageUrl() == null) {
                throw new BusinessException("绑定人脸失败");
            }
            // 保存人脸绑定的数据
            Face face = new Face();
            BeanUtils.copyProperties(faceBO, face);
            face.setBarcode(faceAddDTO.getBarcode());
            face.setLibraryCode(faceAddDTO.getLibraryCode());
            faceRepository.saveAndFlush(face);
            FaceVO faceVO = new FaceVO();
            BeanUtils.copyProperties(face, faceVO);
            return Collections.singletonList(faceVO);
        } else {
            List<FaceVO> faceVOList = new ArrayList<>();
            List<FaceBO> faceBOList = new ArrayList<>();
            // 当前请求是批量绑定人脸，图片名称作为读者证号
            if (!Boolean.TRUE.equals(faceProperties.getAllowedMultipleBinding())) {
                // 检查所有读者证是否存在已经绑定人脸的图片
                List<MultipartFile> notBindMultipartFileList = new ArrayList<>();
                Map<String, MultipartFile> multipartFileMap = new HashMap<>(fileList.size());
                List<String> barcodeList = new ArrayList<>();
                for (MultipartFile multipartFile : fileList) {
                    String imageName = FileNameUtil.getPrefix(multipartFile.getName());
                    if (imageName != null) {
                        barcodeList.add(imageName);
                        multipartFileMap.put(imageName, multipartFile);
                    }
                }
                if (barcodeList.size() == 0) {
                    throw new BusinessException("绑定人脸失败");
                }
                // 查询所有读者绑定人脸的数据
                List<FaceVO> faceList = faceDsl.findByBarcodeList(faceAddDTO.getLibraryCode(), barcodeList);
                if (!ObjectUtils.isEmpty(faceList)) {
                    // 以馆code分组
                    Map<String, List<FaceVO>> allLibraryFaceMap = faceList.stream().collect(Collectors.groupingBy(FaceVO::getLibraryCode));
                    Set<Map.Entry<String, List<FaceVO>>> entries = allLibraryFaceMap.entrySet();
                    for (Map.Entry<String, List<FaceVO>> entry : entries) {
                        List<FaceVO> currentLibraryFaceMap = entry.getValue();
                        // 以读者证分组，统计每个读者证绑定人脸的次数
                        Map<String, Long> barcodeMap = currentLibraryFaceMap.stream().collect(Collectors.groupingBy(FaceVO::getBarcode, Collectors.counting()));
                        Set<Map.Entry<String, Long>> entries2 = barcodeMap.entrySet();
                        for (Map.Entry<String, Long> entry2 : entries2) {
                            if (entry2.getValue() == null || entry2.getValue() == 0) {
                                // 当前读者还未绑定人脸
                                notBindMultipartFileList.add(multipartFileMap.get(entry2.getKey()));
                            } else {
                                FaceVO faceVO = new FaceVO();
                                faceVO.setLibraryCode(faceVO.getLibraryCode());
                                faceVO.setBarcode(entry2.getKey());
                                faceVO.setCode(1);
                                faceVO.setMessage("您已绑定人脸，不可重复绑定");
                                faceVOList.add(faceVO);
                            }
                        }
                    }
                }
                faceBOList = faceRecognitionService.addFaceList(notBindMultipartFileList);
            } else {
                faceBOList = faceRecognitionService.addFaceList(fileList);
            }
            if (!ObjectUtils.isEmpty(faceBOList)) {
                List<Face> faceList = new ArrayList<>();
                for (FaceBO faceBO : faceBOList) {
                    FaceVO faceVO = new FaceVO();
                    BeanUtils.copyProperties(faceBO, faceVO);
                    faceVOList.add(faceVO);
                    if (faceBO.getCode() == 0 && faceBO.getOriginalImageUrl() != null && faceBO.getCropImageUrl() != null) {
                        Face face = new Face();
                        BeanUtils.copyProperties(faceBO, face);
                        faceList.add(face);
                    }
                }
                faceRepository.saveAll(faceList);
            } else {
                throw new BusinessException("绑定人脸失败");
            }
            return faceVOList;
        }
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

    @Override
    public void deleteFace(FaceDeleteDTO faceDeleteDTO) {
        List<FaceVO> faceVOList = faceDsl.findByBarcode(faceDeleteDTO.getLibraryCode(), faceDeleteDTO.getBarcode());
        if (ObjectUtils.isEmpty(faceVOList)) {
            throw new BusinessException("读者未绑定过人脸");
        }
        List<String> imagePathList = new ArrayList<>();
        faceVOList.forEach(t -> {
            imagePathList.add(faceProperties.getFilePath() + "/" + t.getOriginalImageUrl());
            imagePathList.add(faceProperties.getFilePath() + "/" + t.getCropImageUrl());
        });
        FileUtils.deleteFileList(imagePathList);
    }


}
