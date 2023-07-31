package com.shuishu.face.strategy;


import com.shuishu.face.common.properties.FaceProperties;
import com.shuishu.face.strategy.service.FaceRecognitionService;
import com.shuishu.face.strategy.service.impl.ArcSoftFaceServiceImpl;
import com.shuishu.face.strategy.service.impl.BaiduOfflineFaceServiceImpl;
import com.shuishu.face.strategy.service.impl.BaiduOnlineFaceServiceImpl;
import com.shuishu.face.strategy.service.impl.MegviiFaceServiceImpl;
import org.slf4j.Logger;
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
            case "ARC_SOFT":
                faceRecognitionService = new ArcSoftFaceServiceImpl(faceProperties, LoggerFactory.getLogger(ArcSoftFaceServiceImpl.class));
                break;
            default:
                throw new IllegalArgumentException("无效的人脸识别API名称。");
        }

        faceRecognitionService.initialize();
        return faceRecognitionService;
    }
}
