package com.shuishu.face.common.properties;


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
    private ArcSoftProProperties arcSoftProProperties;
    @Setter
    @Getter
    @ToString
    public static class ArcSoftProProperties {
        /**
         * sdk 库
         */
        private String libPath;
        /**
         * 识别阙值
         */
        private Float recognitionMinThreshold;
        /**
         * 绑定阙值
         */
        private Float bindingMinThreshold;
        /**
         * 激活文件路径
         */
        private String activeFilePath;
        /**
         * activeKey
         */
        private String activeKey;
        /**
         * appId
         */
        private String appId;
        /**
         * sdkKey
         */
        private String sdkKey;
        /**
         * 模糊度
         */
        private Float blur;
    }

    /**
     * 百度
     */
    private BaiduOfflineProperties baiduOfflineProperties;
    @Setter
    @Getter
    @ToString
    public static class BaiduOfflineProperties {
        /**
         * sdk 库
         */
        private String libPath;
        /**
         * 识别阙值
         */
        private Float recognitionMinThreshold;
        /**
         * 绑定阙值
         */
        private Float bindingMinThreshold;
        /**
         * appId
         */
        private String appId;
        /**
         * apiKey
         */
        private String apiKey;
        /**
         * secretKey
         */
        private String secretKey;
        /**
         * 模糊度
         */
        private Float blur;
    }

    /**
     * 百度
     */
    private BaiduOnlineProperties baiduOnlineProperties;
    @Setter
    @Getter
    @ToString
    public static class BaiduOnlineProperties {
        /**
         * 识别阙值
         */
        private Float recognitionMinThreshold;
        /**
         * 绑定阙值
         */
        private Float bindingMinThreshold;
        /**
         * appId
         */
        private String appId;
        /**
         * apiKey
         */
        private String apiKey;
        /**
         * secretKey
         */
        private String secretKey;
        /**
         * 模糊度
         */
        private Float blur;
    }

    /**
     * 旷视
     */
    private MegviiProperties megviiProperties;
    @Setter
    @Getter
    @ToString
    public static class MegviiProperties {
        /**
         * 识别阙值
         */
        private Float recognitionMinThreshold;
        /**
         * 绑定阙值
         */
        private Float bindingMinThreshold;
        /**
         * appId
         */
        private String appId;
        /**
         * apiKey
         */
        private String apiKey;
        /**
         * secretKey
         */
        private String secretKey;
        /**
         * 模糊度
         */
        private Float blur;
    }


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
