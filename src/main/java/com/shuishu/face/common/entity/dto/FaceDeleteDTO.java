package com.shuishu.face.common.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-30 22:49
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：删除人脸绑定
 * <p></p>
 */
@Setter
@Getter
@ToString
public class FaceDeleteDTO {
    @NotBlank(message = "馆代码不能为空")
    @Schema(description = "馆代码")
    private String libraryCode;

    @NotBlank(message = "读者条码不能为空")
    @Schema(description = "读者条码")
    private String barcode;
}
