package com.shuishu.face.controller;


import com.shuishu.face.common.config.base.ApiResponse;
import com.shuishu.face.common.entity.dto.*;
import com.shuishu.face.common.entity.vo.FaceVO;
import com.shuishu.face.service.FaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 22:37
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别接口
 * <p></p>
 */
@Tag(name = "人脸识别")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/shuishu/face")
public class FaceController {
    private final FaceService faceService;


    @Operation(summary = "查询", description = "主要通过读者证号和馆code来查询读者的人脸信息")
    @GetMapping("list")
    public ApiResponse<List<FaceVO>> findFaceList(FaceQueryDTO faceQueryDTO) {
        return ApiResponse.of(faceService.findFaceList(faceQueryDTO));
    }
    @Operation(summary = "绑定", description = "可以一次性的添加多个读者的人脸绑定，如果barcode为NULL，就将上传的每个图片名作为人脸图片的读者证")
    @PostMapping("add")
    public ApiResponse<List<FaceVO>> addFace(FaceAddDTO faceAddDTO) {
        return ApiResponse.of(faceService.addFace(faceAddDTO));
    }

    @Operation(summary = "更新", description = "可以一次性的更新多个读者的人脸绑定，，如果barcode为NULL，就将上传的每个图片名作为人脸图片的读者证")
    @PostMapping("update")
    public ApiResponse<List<FaceVO>> updateFace(FaceUpdateDTO faceUpdateDTO) {
        return ApiResponse.of(faceService.updateFace(faceUpdateDTO));
    }

    @Operation(summary = "识别", description = "人脸图片识别，返回读者证等信息")
    @PostMapping("recognize")
    public ApiResponse<FaceVO> recognize(FaceRecognitionDTO faceRecognitionDTO) {
        return ApiResponse.of(faceService.recognize(faceRecognitionDTO));
    }

    @Operation(summary = "比对", description = "两张人脸图片比对是否一致，返回比对得分，满分100分，一般认为80分以上认为是同一个人脸")
    @PostMapping("comparison")
    public ApiResponse<Float> comparisonFace(FaceComparisonFaceDTO faceComparisonFaceDTO) {
        return ApiResponse.of(faceService.comparisonFace(faceComparisonFaceDTO));
    }

    @Operation(summary = "解绑", description = "删除读者证所有的人脸绑定数据")
    @PostMapping("delete")
    public ApiResponse<String> deleteFace(FaceDeleteDTO faceDeleteDTO) {
        faceService.deleteFace(faceDeleteDTO);
        return ApiResponse.success();
    }

}
