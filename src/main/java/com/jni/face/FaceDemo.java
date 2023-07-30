package com.jni.face;

/**
 * 
 * @DEMO示例类
 *
 */
public class FaceDemo {
    // 人脸sdk示例总入口
    public int faceSample() {
        // 人脸检测示例
        faceDetectDemo();
        // 人脸跟踪示例
        // faceTrackDemo();
        // 人脸属性示例
        // faceAttribute();
        // 眼睛闭合检测示例
        // faceEyeClose();
        // 注意力检测示例
       // faceGaze();
        // 姿态角检测示例
       // faceHeadPose();
        // 光照检测示例
        // faceIllumination();
        // 模糊度检测示例
        // faceBlur();
        // 嘴巴闭合检测示例
        // faceMouthClose();
        // 口罩佩戴检测示例
        // faceMouthMask();
        // 遮挡检测示例
        // faceOcclusion();
        // 暗光恢复示例
        // darkEnhance();
        // 人脸抠图示例
        // faceCrop();
        // 最佳人脸检测示例
        // faceBest();
        // 人脸关键点示例
        // faceLandmarks();
        // 动作活体示例
        // faceActionLive();
        // 特征值提取示例
        // faceFeature();
        // 人脸库管理示例
        // faceManager();
        // 人脸比对示例
        // faceCompare();
         // 静默活体示例
        // faceLiveness();
        // 活体分值+特征值+人脸框的场景化示例
        // faceScene();
        // 多线程示例
        // multiThread();
        return 0;
    }
    
    // 人脸检测demo
    public int faceDetectDemo() {
        FaceDetect detect = new FaceDetect();
        // 图片人脸检测
         detect.imageDetect();
         System.out.println("before the second img detect");
         detect.imageDetect();
         System.out.println("after the second img detect");
         detect.imageDetect();
         System.out.println("after the third img detect");
        // usb摄像头视频检测
        // detect.usbVideoDetect();
        return 0;
    }
    
    // 人脸跟踪demo
    public int faceTrackDemo() {
        FaceTrack track = new FaceTrack();
        // 图片人脸跟踪
        // track.imageTrack();
        // usb摄像头视频跟踪
        track.usbVideoTrack();
        return 0;
    }
    
    // 人脸属性
    public int faceAttribute() {
        FaceAttr attr = new FaceAttr();
        // 人脸属性（从图片提取)
        attr.imageFaceAttribute();
        return 0;
    }
    
    // 眼睛闭合检测
    public int faceEyeClose() {
        FaceEyeClose eye = new FaceEyeClose();
        // 人脸眼部闭合检测（从图片提取)
        eye.imageFaceEyeClose();
        return 0;
    }
    
    // 注意力检测
    public int faceGaze() {
        FaceGaze gaze = new FaceGaze();
        // 注意力检测（从图片提取)
        gaze.imageFaceGaze();
        return 0;
    }
    
    // 姿态角检测
    public int faceHeadPose() {
        FaceHeadPose pose = new FaceHeadPose();
        // 姿态角检测（从图片提取)
        pose.imageFaceHeadPose();
        return 0;
    }
    
    // 光照检测
    public int faceIllumination() {
        FaceIllumination illu = new FaceIllumination();
        // 光照检测（从图片提取)
        illu.imageFaceIllumination();
        return 0;
    }
    
    // 模糊度检测
    public int faceBlur() {
        FaceBlur blur = new FaceBlur();
        // 模糊度检测（从图片提取)
        blur.imageFaceBlur();
        return 0;
    }
    
    // 嘴巴闭合检测
    public int faceMouthClose() {
        FaceMouthClose mouth = new FaceMouthClose();
        // 嘴巴闭合检测（从图片提取)
        mouth.imageFaceMouthClose();
        return 0;
    }
    
    // 口罩佩戴检测
    public int faceMouthMask() {
        FaceMouthMask mask = new FaceMouthMask();
        // 口罩佩戴检测（从图片提取)
        mask.imageFaceMouthMask();
        return 0;
    }
    
    // 遮挡检测
    public int faceOcclusion() {
        FaceOcclusion occlu = new FaceOcclusion();
        // 遮挡检测（从图片提取)
        occlu.imageFaceOcclusion();
        return 0;
    }
    
    // 暗光恢复
    public int darkEnhance() {
        DarkEnhance dark = new DarkEnhance();
        // 暗光恢复
        dark.imageDarkEnhance();
        return 0;
    }
    
    // 人脸抠图
    public int faceCrop() {
        FaceCrop crop = new FaceCrop();
        // 人脸抠图
        crop.imageFaceCrop();
        return 0;
    }
    
    // 最佳人脸检测
    public int faceBest() {
        FaceBest best = new FaceBest();
        // 最佳人脸检测
        best.imageFaceBest();
        return 0;
    }
    
    // 人脸关键点
    public int faceLandmarks() {
        FaceLandmarks land = new FaceLandmarks();
        // 人脸关键点
        land.imageFaceLandmarks();
        return 0;
    }
    
    // 动作活体
    public int faceActionLive() {
        FaceActionLive action = new FaceActionLive();
        // usb视频动作活体示例
        action.usbVideoActionLive();
        return 0;
    }
    
    // 特征值提取
    public int faceFeature() {
        FaceFeature fea = new FaceFeature();
        // 提取特征值示例
        fea.imageFaceFeature();
       
        return 0;
    }
    
    // 人脸特征值和比对
    public int faceCompare() {
        FaceCompare comp = new FaceCompare();
        // 通过图片进行1:1比对
        comp.imageMatch();
        // 提取特征值及比对示例
        // comp.imageFaceMatch();
        // 图片和视频帧特征值比对
        // comp.videoFaceMatch();
        // 测试人脸1：N识别(传图片帧和预加载的库比较)
        comp.faceIdentifyWithDbByMat();
        // 测试人脸1：N识别(传特征值和预加载的库比较)
        comp.faceIdentifyWithDb();
        // 测试人脸1：N识别(传图片帧和实时查找的库比较)
        // comp.faceIdentifyByMat();
        // 测试人脸1：N识别(和实时查找的库比较)
        // comp.faceIdentify();
        return 0;
    }
    
    // 静默活体示例
    public int faceLiveness() {
        FaceLiveness live = new FaceLiveness();
        // rgb或nir单目活体示例（通过图片)
      //  live.faceLivenessByImage();
        // rgb单目静默视频活体检测
         live.usbVideoRgbLiveness();
        // rgb+nir双目活体示例
        // live.rgbNirLiveness();
        // 奥比中光rgb+depth双目活体示例
     //   live.rgbDepthLivenessByOrbe();
        // 华捷艾米rgb+depth双目活体示例
        // live.rgbDepthLivenessByHjimi();
        return 0;
    }
    
    // 活体分值+特征值+人脸框的场景化示例
    public int faceScene() {
        FaceScene scene = new FaceScene();
        scene.videoLivenessFeature();
        return 0;
    }
    // 多线程示例
    public int multiThread() {
        MultiThread multi = new MultiThread();
        multi.multiThreadDemo();
        return 0;
    }
    
    // 人脸库管理示例
    public int faceManager() {
        FaceManager manager = new FaceManager();
        // 人脸注册(传图片帧)
      //  manager.userAddByMat();
        // 人脸注册(传特征值)
         manager.userAdd();
        // 人脸更新
       // manager.userUpdate();
       
        // 组操作
        manager.groupManager();
        // 查询用户信息
       // manager.getUserinfo();
        // 查询用户图片
        // manager.getUserImage();
        // 用户组列表查询
        // manager.getUserlist();
        // 组列表查询
        // manager.getGrouplist();
        // 数据库人脸数量查询
        manager.getDbFaceCount();
        return 0;
    }
}
