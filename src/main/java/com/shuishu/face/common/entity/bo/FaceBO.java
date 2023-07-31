package com.shuishu.face.common.entity.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-30 21:45
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：每个策略使用人脸识别产品，响应数据转换系统实体
 * <p></p>
 */
@Setter
@Getter
@ToString
public class FaceBO {

    @Schema(description = "人脸id")
    private Long faceId;

    @Schema(description = "读者条码")
    private String barcode;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "特征值大小")
    private Integer featureSize;

    @Schema(description = "特征值byte")
    private byte[] featureByte;

    @Schema(description = "特征值string")
    private String featureData;

    @Schema(description = "原图片url")
    private String originalImageUrl;

    @Schema(description = "剪切图片url")
    private String cropImageUrl;

    @Schema(description = "图片质量")
    private Integer quality;

    @Schema(description = "俯仰角度")
    private Float angelFuYang;

    @Schema(description = "偏左右角度")
    private Float angelLeftRight;

    @Schema(description = "平面角度")
    private Float angelPlane;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "馆代码")
    private String libraryCode;

    @Schema(description = "采集人脸的设备序列号")
    private String deviceSerialNumber;

    @Schema(description = "采集人脸使用的API")
    private String createApiName;

    @Schema(description = "更新人脸使用的API")
    private String updateApiName;

    @Schema(description = "图片比对得分")
    private Float score;

    @Schema(description = "批量操作时，每个对象状态的code")
    private int code = 0;

    @Schema(description = "批量操作时，每个对象状态的信息")
    private String message;

}
