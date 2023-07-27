package com.shuishu.face.controller;


import com.shuishu.face.service.FaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
