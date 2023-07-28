package com.shuishu.face.common.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-28 15:13
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别dto
 * <p></p>
 */
@Setter
@Getter
@ToString
@Schema(description = "人脸识别dto")
public class FaceRecognitionDTO {
    @Schema(description = "人脸图片")
    private MultipartFile file;

    @NotBlank(message = "馆代码不能为空")
    @Schema(description = "馆代码")
    private String libraryCode;

}
