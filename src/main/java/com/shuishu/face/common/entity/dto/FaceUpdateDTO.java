package com.shuishu.face.common.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-28 13:56
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸查询dto
 * <p></p>
 */
@Setter
@Getter
@ToString
@Schema(description = "人脸绑定dto")
public class FaceUpdateDTO {
    @Schema(description = "人脸图片（可选多张）")
    private List<MultipartFile> fileList;

    @Schema(description = "读者条码")
    private String barcode;

    @NotBlank(message = "馆代码不能为空")
    @Schema(description = "馆代码")
    private String libraryCode;

    @Schema(description = "采集人脸设备的序列号")
    private String deviceSerialNumber;

    @Schema(description = "场景")
    private String scene;

}
