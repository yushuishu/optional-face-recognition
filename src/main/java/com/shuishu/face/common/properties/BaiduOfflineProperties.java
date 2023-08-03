package com.shuishu.face.common.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ：谁书-ss
 * @Date ：2023-08-03 21:52
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：百度离线
 * <p></p>
 */
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix = "face.baidu-offline")
public class BaiduOfflineProperties {
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
