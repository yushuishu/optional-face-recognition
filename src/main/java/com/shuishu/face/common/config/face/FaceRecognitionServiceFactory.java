package com.shuishu.face.common.config.face;


import com.shuishu.face.common.config.face.service.FaceRecognitionService;
import com.shuishu.face.common.config.face.service.impl.ArcSoftFaceServiceImpl;
import com.shuishu.face.common.config.face.service.impl.BaiduOfflineFaceServiceImpl;
import com.shuishu.face.common.config.face.service.impl.BaiduOnlineFaceServiceImpl;
import com.shuishu.face.common.config.face.service.impl.MegviiFaceServiceImpl;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:25
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸service工厂
 * <p></p>
 */
public class FaceRecognitionServiceFactory {
    private FaceRecognitionService faceRecognitionService;

    public FaceRecognitionService createFacialRecognitionService(String apiName) {
        switch (apiName) {
            case "BAIDU_OFFLINE":
                faceRecognitionService = new BaiduOfflineFaceServiceImpl();
                break;
            case "BAIDU_ONLINE":
                faceRecognitionService = new BaiduOnlineFaceServiceImpl();
            case "MEGVII":
                faceRecognitionService = new MegviiFaceServiceImpl();
                break;
            case "ARC_SOFT":
                faceRecognitionService = new ArcSoftFaceServiceImpl();
                break;
            default:
                throw new IllegalArgumentException("无效的人脸识别API名称。");
        }

        faceRecognitionService.initialize();
        return faceRecognitionService;
    }
}
