package com.shuishu.face.common.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuZhenFeng
 * @date 2023/7/28 15:29
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "face")
public class FaceProperties {
    /**
     * true：允许多次绑定人脸 false：只能注册一次
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
     * 虹软
     */
    private ArcSoftProperties arcSoftProperties;
    @Setter
    @Getter
    @ToString
    public static class ArcSoftProperties {
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
    }

    /**
     * 百度
     */
    private BaiduProperties baiduProperties;
    @Setter
    @Getter
    @ToString
    public static class BaiduProperties {
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
    }




}
