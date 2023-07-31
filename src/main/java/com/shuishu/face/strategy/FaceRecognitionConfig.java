package com.shuishu.face.strategy;


import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:27
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸识别配置
 * <p></p>
 */
@Configuration
public class FaceRecognitionConfig {
    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionConfig.class);

    @Resource
    private FaceProperties faceProperties;

    @Bean
    public FaceRecognitionService facialRecognitionService(FaceRecognitionServiceFactory faceRecognitionServiceFactory, @Value("${face.api-name}") String apiName) {
        logger.info("当前服务使用的人脸API：{}", apiName);
        return faceRecognitionServiceFactory.createFacialRecognitionService(apiName, faceProperties);
    }
}
