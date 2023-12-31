package com.shuishu.face.strategy;


import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.strategy.service.impl.*;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:25
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸service工厂
 * <p></p>
 */
@Configuration
public class FaceRecognitionServiceFactory {
    private FaceRecognitionService faceRecognitionService;

    public FaceRecognitionService createFacialRecognitionService(String apiName, FaceProperties faceProperties) {
        switch (apiName) {
            case "BAIDU_OFFLINE":
                faceRecognitionService = new BaiduOfflineFaceServiceImpl(faceProperties, LoggerFactory.getLogger(BaiduOfflineFaceServiceImpl.class));
                break;
            case "BAIDU_ONLINE":
                faceRecognitionService = new BaiduOnlineFaceServiceImpl(faceProperties, LoggerFactory.getLogger(BaiduOnlineFaceServiceImpl.class));
                break;
            case "MEGVII":
                faceRecognitionService = new MegviiFaceServiceImpl(faceProperties, LoggerFactory.getLogger(MegviiFaceServiceImpl.class));
                break;
            case "ARC_SOFT_PRO":
                faceRecognitionService = new ArcSoftProFaceServiceImpl(faceProperties, LoggerFactory.getLogger(ArcSoftProFaceServiceImpl.class));
                break;
            default:
                throw new IllegalArgumentException("无效的人脸识别API名称。");
        }
        // 为了尽量保持构造函数简洁，以及SDK可能失败的初始化步骤，放在外部方法initialize()中
        faceRecognitionService.initialize();
        return faceRecognitionService;
    }
}
