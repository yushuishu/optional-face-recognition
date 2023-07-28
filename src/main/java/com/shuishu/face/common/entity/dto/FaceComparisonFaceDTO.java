package com.shuishu.face.common.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 15:22
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸比对dto
 * <p></p>
 */
@Setter
@Getter
@ToString
public class FaceComparisonFaceDTO {
    @Schema(description = "人脸图片1")
    private MultipartFile fileOne;

    @Schema(description = "人脸图片2")
    private MultipartFile fileTwo;

}
