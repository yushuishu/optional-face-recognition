package com.shuishu.face.common.properties;


import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-28 15:29
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸配置信息
 * <p></p>
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "face")
public class FaceProperties {
    /**
     * true：允许多次绑定人脸 false：只能注册绑定一次（一条数据）
     */
    private Boolean allowedMultipleBinding;
    /**
     * API厂商
     */
    private String apiName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 原始图片路径
     */
    private String originalFilePath;
    /**
     * 剪切图片路径
     */
    private String cropFilePath;


    /**
     * 虹软（增值版）
     */
    @Resource
    private ArcSoftProProperties arcSoftProProperties;
    /**
     * 百度
     */
    @Resource
    private BaiduOfflineProperties baiduOfflineProperties;
    /**
     * 百度
     */
    @Resource
    private BaiduOnlineProperties baiduOnlineProperties;
    /**
     * 旷视
     */
    @Resource
    private MegviiProperties megviiProperties;




    public void setFilePath(String filePath) {
        if (filePath != null) {
            this.filePath = filePath;
            this.originalFilePath = filePath + "/original";
            this.cropFilePath = filePath + "/crop";
        }
    }
    public String getOriginalFilePath() {
        if (originalFilePath == null && filePath != null) {
            originalFilePath = filePath + "/original";
        }
        return originalFilePath;
    }
    public String getCropFilePath() {
        if (cropFilePath == null && filePath != null) {
            cropFilePath = filePath + "/crop";
        }
        return cropFilePath;
    }


}
