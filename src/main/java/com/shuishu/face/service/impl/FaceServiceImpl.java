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
import java.util.stream.Stream;

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
        if (StringUtils.hasText(faceAddDTO.getBarcode())) {
            // 添加当个读者
            return addFaceForSingle(faceAddDTO);
        } else {
            // 添加多个读者
            return addFaceForMultiple(faceAddDTO);
        }
    }

    private List<FaceVO> addFaceForSingle(FaceAddDTO faceAddDTO) {
        if (!Boolean.TRUE.equals(faceProperties.getAllowedMultipleBinding())) {
            int bindingCount = faceDsl.findBindingCount(faceAddDTO.getLibraryCode(), faceAddDTO.getBarcode());
            if (bindingCount > 0) {
                throw new BusinessException("您已绑定人脸，不可重复绑定");
            }
        }
        // 获取第一张图片
        MultipartFile multipartFile = faceAddDTO.getFileList().get(0);
        FaceBO faceBO = faceRecognitionService.addFace(faceAddDTO.getLibraryCode(), faceAddDTO.getBarcode(), multipartFile);
        if (faceBO == null || faceBO.getOriginalImageUrl() == null || faceBO.getCropImageUrl() == null) {
            throw new BusinessException("绑定人脸失败");
        }
        // 保存人脸绑定的数据
        Face face = new Face();
        BeanUtils.copyProperties(faceBO, face);
        face.setBarcode(faceAddDTO.getBarcode());
        face.setLibraryCode(faceAddDTO.getLibraryCode());
        face.setDeviceSerialNumber(faceAddDTO.getDeviceSerialNumber());
        face.setScene(faceAddDTO.getScene());
        faceRepository.saveAndFlush(face);
        FaceVO faceVO = new FaceVO();
        BeanUtils.copyProperties(face, faceVO);
        return Collections.singletonList(faceVO);
    }

    private List<FaceVO> addFaceForMultiple(FaceAddDTO faceAddDTO) {
        // 当前请求是批量绑定人脸，图片名称作为读者证号
        List<FaceVO> faceVOList = new ArrayList<>();
        List<FaceBO> faceBOList;
        List<MultipartFile> fileList = faceAddDTO.getFileList();
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
            // 查询数据库，所有读者证绑定的 所有人脸数据
            List<FaceVO> faceList = faceDsl.findByBarcodeList(faceAddDTO.getLibraryCode(), barcodeList);
            if (!ObjectUtils.isEmpty(faceList)) {
                Map<String, Long> map = faceList.stream().collect(Collectors.groupingBy(FaceVO::getBarcode, Collectors.counting()));
                for (String barcode : barcodeList) {
                    Long bindFaceCount = map.get(barcode);
                    if (bindFaceCount == null || bindFaceCount == 0) {
                        // 当前读者还未绑定人脸
                        notBindMultipartFileList.add(multipartFileMap.get(barcode));
                    } else {
                        FaceVO faceVO = new FaceVO();
                        faceVO.setLibraryCode(faceVO.getLibraryCode());
                        faceVO.setBarcode(barcode);
                        faceVO.setCode(1);
                        faceVO.setMessage("您已绑定人脸，不可重复绑定");
                        faceVOList.add(faceVO);
                    }
                }
                // 未绑定过人脸的读者证，开始人脸识别绑定
                faceBOList = faceRecognitionService.addFaceList(faceAddDTO.getLibraryCode(), notBindMultipartFileList);
            } else {
                // 数据库不存在所有读者证的人脸信息，直接进行人脸识别绑定
                faceBOList = faceRecognitionService.addFaceList(faceAddDTO.getLibraryCode(), fileList);
            }
        } else {
            // 所有读者证可以多次绑定，直接进行人脸识别绑定
            faceBOList = faceRecognitionService.addFaceList(faceAddDTO.getLibraryCode(), fileList);
        }
        if (!ObjectUtils.isEmpty(faceBOList)) {
            List<Face> faceList = new ArrayList<>();
            for (FaceBO faceBO : faceBOList) {
                faceBO.setDeviceSerialNumber(faceAddDTO.getDeviceSerialNumber());
                faceBO.setScene(faceAddDTO.getScene());
                FaceVO faceVO = new FaceVO();
                BeanUtils.copyProperties(faceBO, faceVO);
                faceVOList.add(faceVO);
                if (faceBO.getCode() == 0 && faceBO.getOriginalImageUrl() != null && faceBO.getCropImageUrl() != null) {
                    Face face = new Face();
                    BeanUtils.copyProperties(faceBO, face);
                    faceList.add(face);
                }
            }
            if (!ObjectUtils.isEmpty(faceList)) {
                // 保存人脸信息
                faceRepository.saveAll(faceList);
            }
        } else {
            throw new BusinessException("绑定人脸失败");
        }
        return faceVOList;
    }

    @Override
    public List<FaceVO> updateFace(FaceUpdateDTO faceUpdateDTO) {
        if (ObjectUtils.isEmpty(faceUpdateDTO.getFileList())) {
            throw new BusinessException("更新人脸图片不能为空");
        }
        if (StringUtils.hasText(faceUpdateDTO.getBarcode())) {
            return updateFaceForSingle(faceUpdateDTO);
        } else {
            return updateFaceForMultiple(faceUpdateDTO);
        }
    }

    private List<FaceVO> updateFaceForSingle(FaceUpdateDTO faceUpdateDTO) {
        // 查询读者所有绑定的人脸
        List<FaceVO> readerFaceList = faceDsl.findByBarcode(faceUpdateDTO.getLibraryCode(), faceUpdateDTO.getBarcode());
        if (ObjectUtils.isEmpty(readerFaceList)) {
            throw new BusinessException("您未绑定人脸");
        }
        // 更新人脸操作，要删除旧的绑定人脸ID集合、图片路径
        List<Long> deleteFaceIdList = readerFaceList.stream().map(FaceVO::getFaceId).toList();
        List<String> deleteOriginalImageUrlList = new ArrayList<>();
        List<String> deleteCropImageUrlList = new ArrayList<>();
        readerFaceList.forEach(t -> {
            deleteOriginalImageUrlList.add(faceProperties.getFilePath() + "/" + t.getOriginalImageUrl());
            deleteCropImageUrlList.add(faceProperties.getFilePath() + "/" + t.getCropImageUrl());
        });
        // 获取第一张图片
        MultipartFile multipartFile = faceUpdateDTO.getFileList().get(0);
        FaceBO faceBO = faceRecognitionService.addFace(faceUpdateDTO.getLibraryCode(), faceUpdateDTO.getBarcode(), multipartFile);
        if (faceBO == null || faceBO.getOriginalImageUrl() == null || faceBO.getCropImageUrl() == null) {
            throw new BusinessException("绑定人脸失败");
        }
        // 保存人脸绑定的数据
        Face face = new Face();
        BeanUtils.copyProperties(faceBO, face);
        face.setBarcode(faceUpdateDTO.getBarcode());
        face.setLibraryCode(faceUpdateDTO.getLibraryCode());
        face.setDeviceSerialNumber(faceUpdateDTO.getDeviceSerialNumber());
        face.setScene(faceUpdateDTO.getScene());
        faceRepository.saveAndFlush(face);
        // 响应数据
        FaceVO faceVO = new FaceVO();
        BeanUtils.copyProperties(face, faceVO);
        // 删除人脸库图片
        faceRecognitionService.deleteFace(faceUpdateDTO.getLibraryCode(), faceUpdateDTO.getBarcode(), deleteOriginalImageUrlList, deleteCropImageUrlList, false);
        // 删除人脸
        faceDsl.deleteByFaceIdList(deleteFaceIdList);
        return Collections.singletonList(faceVO);
    }

    private List<FaceVO> updateFaceForMultiple(FaceUpdateDTO faceUpdateDTO) {
        // 当前请求是批量绑定人脸，图片名称作为读者证号
        List<FaceVO> faceVOList = new ArrayList<>();
        List<FaceBO> faceBOList;
        List<String> barcodeList = new ArrayList<>();
        List<MultipartFile> alreadyBindMultipartFileList = new ArrayList<>();
        List<MultipartFile> fileList = faceUpdateDTO.getFileList();
        Map<String, MultipartFile> multipartFileMap = new HashMap<>(fileList.size());
        for (MultipartFile multipartFile : fileList) {
            String imageName = FileNameUtil.getPrefix(multipartFile.getName());
            if (imageName != null) {
                barcodeList.add(imageName);
                multipartFileMap.put(imageName, multipartFile);
            }
        }
        // 查询数据库，所有读者证的所有人脸信息
        List<FaceVO> readerFaceList = faceDsl.findByBarcodeList(faceUpdateDTO.getLibraryCode(), barcodeList);
        if (!ObjectUtils.isEmpty(readerFaceList)) {
            Map<String, Long> map = readerFaceList.stream().collect(Collectors.groupingBy(FaceVO::getBarcode, Collectors.counting()));
            for (String barcode : barcodeList) {
                Long bindFaceCount = map.get(barcode);
                if (bindFaceCount == null || bindFaceCount == 0) {
                    FaceVO faceVO = new FaceVO();
                    faceVO.setCode(1);
                    faceVO.setMessage("您未绑定人脸");
                    faceVOList.add(faceVO);
                } else {
                    alreadyBindMultipartFileList.add(multipartFileMap.get(barcode));
                }
            }
        } else {
            throw new BusinessException("所有读者均未绑定人脸");
        }
        //绑定人脸信息
        faceBOList = faceRecognitionService.addFaceList(faceUpdateDTO.getLibraryCode(), alreadyBindMultipartFileList);
        if (!ObjectUtils.isEmpty(faceBOList)) {
            List<Face> faceList = new ArrayList<>();
            for (FaceBO faceBO : faceBOList) {
                faceBO.setDeviceSerialNumber(faceUpdateDTO.getDeviceSerialNumber());
                faceBO.setScene(faceUpdateDTO.getScene());
                FaceVO faceVO = new FaceVO();
                BeanUtils.copyProperties(faceBO, faceVO);
                faceVOList.add(faceVO);
                if (faceBO.getCode() == 0 && faceBO.getOriginalImageUrl() != null && faceBO.getCropImageUrl() != null) {
                    Face face = new Face();
                    BeanUtils.copyProperties(faceBO, face);
                    faceList.add(face);
                }
            }
            if (!ObjectUtils.isEmpty(faceList)) {
                // 保存更新的人脸信息
                faceRepository.saveAll(faceList);
                // 删除旧的人脸信息
                List<Long> deleteFaceIdList = new ArrayList<>();
                List<String> deleteImagePathList = new ArrayList<>();
                List<String> imageFullNameList = new ArrayList<>();
                List<String> updateSuccessBarcodeList = faceList.stream().map(Face::getBarcode).toList();
                readerFaceList.forEach(t -> {
                    // 只有更新成功的读者证人脸信息，才将旧的数据和图片删除
                    if (updateSuccessBarcodeList.contains(t.getBarcode())) {
                        deleteFaceIdList.add(t.getFaceId());
                        deleteImagePathList.add(faceProperties.getFilePath() + "/" + t.getOriginalImageUrl());
                        deleteImagePathList.add(faceProperties.getFilePath() + "/" + t.getCropImageUrl());
                        imageFullNameList.add(FileNameUtil.getName(t.getCropImageUrl()));

                    }
                });
                // 删除人脸库图片
                // 删除数据库数据
                faceDsl.deleteByFaceIdList(deleteFaceIdList);
                // 删除本地图片
                FileUtils.deleteFileList(deleteImagePathList);
            }
        } else {
            throw new BusinessException("绑定人脸失败");
        }
        return faceVOList;
    }

    @Override
    public FaceVO recognize(FaceRecognitionDTO faceRecognitionDTO) {
        // 查询数据库所有人脸特征值
        List<FaceVO> faceList = faceDsl.findByBarcode(faceRecognitionDTO.getLibraryCode(), null);
        if (ObjectUtils.isEmpty(faceList)) {
            throw new BusinessException("未绑定人脸");
        }
        Map<Long, byte[]> faceFeatureMap = faceList.stream().collect(Collectors.toMap(FaceVO::getFaceId, FaceVO::getFeatureByte));
        Map<Long, Integer> faceFeatureSizeMap = faceList.stream().collect(Collectors.toMap(FaceVO::getFaceId, FaceVO::getFeatureSize));
        List<FaceBO> faceBOList = faceRecognitionService.recognize(faceFeatureMap, faceFeatureSizeMap, faceRecognitionDTO.getFile());

        return null;
    }

    @Override
    public Float comparisonFace(FaceComparisonFaceDTO faceComparisonFaceDTO) {
        return faceRecognitionService.comparisonFace(faceComparisonFaceDTO.getFileOne(), faceComparisonFaceDTO.getFileTwo());
    }

    @Override
    public void deleteFace(FaceDeleteDTO faceDeleteDTO) {
        List<FaceVO> faceVOList = faceDsl.findByBarcode(faceDeleteDTO.getLibraryCode(), faceDeleteDTO.getBarcode());
        if (ObjectUtils.isEmpty(faceVOList)) {
            throw new BusinessException("读者未绑定过人脸");
        }
        List<Long> faceIdList = new ArrayList<>();
        List<String> originalImageUrlList = new ArrayList<>();
        List<String> cropImageUrlList = new ArrayList<>();
        faceVOList.forEach(t -> {
            originalImageUrlList.add(faceProperties.getFilePath() + "/" + t.getOriginalImageUrl());
            cropImageUrlList.add(faceProperties.getFilePath() + "/" + t.getCropImageUrl());
        });
        boolean deleteFace = faceRecognitionService.deleteFace(faceDeleteDTO.getLibraryCode(), faceDeleteDTO.getBarcode(), originalImageUrlList, cropImageUrlList, true);
        if (!deleteFace) {
            throw new BusinessException("解绑人脸失败");
        }
        faceDsl.deleteByFaceIdList(faceIdList);
    }


}
