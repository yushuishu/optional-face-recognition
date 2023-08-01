package com.shuishu.face.common.entity.po;


import com.shuishu.face.common.config.base.BasePO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:21
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸po
 * <p></p>
 */
@Setter
@Getter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ss_face")
@Comment("人脸表")
public class Face extends BasePO {
    @Id
    @GeneratedValue(generator = "CustomIdGenerator")
    @GenericGenerator(name = "CustomIdGenerator", strategy = "com.shuishu.face.common.config.id.CustomIdGenerator")
    @Comment("人脸id")
    private Long faceId;

    @Column(nullable = false)
    @Comment("读者条码")
    private String barcode;

    @Comment("年龄")
    private Integer age;

    @Comment("性别")
    private Integer gender;

    @Column(nullable = false, unique = true)
    @Comment("特征值大小")
    private Integer featureSize;

    @Column(nullable = false, unique = true)
    @Comment("特征值byte")
    private byte[] featureByte;

    @Column(nullable = false, unique = true)
    @Comment("特征值string")
    private String featureData;

    @Column(nullable = false, unique = true)
    @Comment("原图片url")
    private String originalImageUrl;

    @Column(nullable = false, unique = true)
    @Comment("剪切图片url")
    private String cropImageUrl;

    @Comment("图片质量")
    private Integer quality;

    @Comment("俯仰角度")
    private Float angelFuYang;

    @Comment("偏左右角度")
    private Float angelLeftRight;

    @Comment("平面角度")
    private Float angelPlane;

    @Comment("场景")
    private String scene;

    @Comment("馆代码")
    private String libraryCode;

    @Comment("采集人脸的设备序列号")
    private String deviceSerialNumber;

    @Column(nullable = false)
    @Comment("采集人脸使用的API")
    private String createApiName;

    @Column(nullable = false)
    @Comment("更新人脸使用的API")
    private String updateApiName;

}
