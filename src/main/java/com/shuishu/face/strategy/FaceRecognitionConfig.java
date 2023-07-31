package com.shuishu.face.strategy;


import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import jakarta.annotation.Resource;
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
    @Resource
    private FaceProperties faceProperties;

    @Bean
    public FaceRecognitionService facialRecognitionService(FaceRecognitionServiceFactory faceRecognitionServiceFactory, @Value("${face.recognition.api}") String apiName) {
        return faceRecognitionServiceFactory.createFacialRecognitionService(apiName, faceProperties);
    }
}
