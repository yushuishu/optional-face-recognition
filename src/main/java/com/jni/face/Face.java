package com.jni.face;
import com.jni.struct.TrackFaceInfo;
import com.jni.struct.Attribute;
import com.jni.struct.RgbDepthInfo;
import com.jni.struct.EyeClose;
import com.jni.struct.FaceBox;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import com.jni.struct.GazeInfo;
import com.jni.struct.HeadPose;
import com.jni.struct.Landmarks;
import com.jni.struct.LiveFeatureInfo;
import com.jni.struct.LivenessInfo;
import com.jni.struct.Occlusion;


public class Face {
    // *******以下为人脸sdk api接口*********
    /* sdk初始化 */
    public native int sdkInit(String modelPath);
    /* sdk销毁（资源释放) */
    public native void sdkDestroy();
    /* 判断是否授权 */
    public native boolean isAuth();   
    /* 获取设备指纹 */
    public static native String getDeviceId();
    /* 获取版本号 */
    public static native String sdkVersion();
    /* 人脸检测 */
    public static native FaceBox[] detect(long matAddr, int type);  
    /* 人脸跟踪 */
    public static native FaceBox[] track(long matAddr, int type);
    /* 清除跟踪历史 */
    public static native int clearTrackHistory();
    /* 获取人脸属性 */
    public static native Attribute[] faceAttr(long matAddr);
    /* 眼睛闭合状态检测 */
    public static native EyeClose[] faceEyeClose(long matAddr);
    /* 注意力检测 */
    public static native GazeInfo[] faceGaze(long matAddr);
    /*  姿态角检测 */
    public static native HeadPose[] faceHeadPose(long matAddr);  
    /*  光照检测 */
    public static native float[] faceIllumination(long matAddr);
    /* 模糊度检测 */
    public static native float[] faceBlur(long matAddr);
    /*  嘴巴闭合检测 */
    public static native float[] faceMouthClose(long matAddr);
    /*  口罩佩戴检测 */
    public static native float[] faceMouthMask(long matAddr);
    /*  遮挡度检测 */
    public static native Occlusion[] faceOcclusion(long matAddr);
    /* 暗光恢复 */
    public static native int darkEnhance(long matAddr, long outAddr);
    /*  人脸抠图 */
    public static native int faceCrop(long matAddr, long outAddr);
    /*  最佳人脸检测 */
    public static native float[] faceBest(long matAddr);
    /*  人脸关键点 */
    public static native Landmarks[] faceLandmark(long matAddr, int type);
    /*  动作活体 */
    public static native FaceBox[] faceActionLive(long matAddr, int actionType, int[] actionResult); 
    /*  清除动作活体历史 */
    public static native int clearActionLiveHistory();
    /*  人脸特征值 (type 0: rgb可见光特征值 1：nir近红外特征值）*/
    public static native FeatureInfo[] faceFeature(long matAddr, int type);
   
    /* 人脸特征值 + 活体 */
    public static native LiveFeatureInfo[] livenessFeature(long matAddr, int type);
    /* 人脸1:1比对 */
    public static native int match(long matAddr1, long matAddr2, int type);
    /* 特征值比较 */
    public static native float compareFeature(Feature f1, Feature f2, int type);
    /*  rgb可见光静默活体 */
    public static native LivenessInfo[] rgbLiveness(long matAddr);
    /*  nir近红外静默活体 */
    public static native LivenessInfo[] nirLiveness(long matAddr);
    /*  rgb+depth 可见光和深度双目静默活体  */
    public static native RgbDepthInfo[] rgbAndDepthLiveness(long rgbAddr, long depthAddr);
    
    // ********以下为人脸库接口********
    /*  人脸注册(传图片) */
    public static native String userAddByMat(long matAddr, String userId, String groupId, String userInfo);
    /*  人脸注册(传图片特征值) */
    public static native String userAdd(Feature fea, String userId, String groupId, String userInfo);
    /* 人脸更新(传图片特征值) */
    public static native String userUpdate(long matAddr, String userId, String groupId, String userInfo);
    /*  用户删除 */
    public static native String userDelete(String userId, String groupId);
    /*  组添加 */
    public static native String groupAdd(String groupId);
    /*  组删除 */
    public static native String groupDelete(String groupId);
    /*  查询用户信息 */
    public static native String getUserInfo(String userId, String groupId);
    /*  查用户图片信息 */
    public static native int getUserImage(long outAddr, String userId, String groupId);
    /*  用户组列表查询 */
    public static native String getUserList(String groupId, int start, int length);
    /*  组列表查询 */
    public static native String getGroupList(int start, int length);
    /* 查数据库人脸数量 */
    public static native int dbFaceCount(String groupId);
    /*  1:N人脸识别(传图片) */
    public static native String identifyByMat(long matAddr, String groupIdList, 
        String userId, int type);
    /*  1:N人脸识别（传特征值） */
    public static native String identify(Feature fea, String groupIdList, 
        String userId, int type);
    /*  1:N人脸识别(传图片和整个库比较,需要提前调loadDbFace（）) */
    public static native String identifyWithAllByMat(long matAddr, int type);
    /*  1:N人脸识别(传特征值和整个库比较,需要提前调loadDbFace（）) */
    public static native String identifyWithAll(Feature fea, int type);
    /*  加载数据库人脸库到内存（数据库数据通过userAdd等注册入库）*/
    public static native boolean loadDbFace();  
    
    // 以下接口为多实例多线程新增接口
    /* 多线程sdk初始化2 */ 
    public native int sdkInit2(String modelPath);
    /* 多线程sdk销毁2（资源释放) */
    public native void sdkDestroy2();
    
    /*  人脸特征值 (type 0: rgb可见光特征值 1：nir近红外特征值）*/
    public static native FeatureInfo[] faceFeature2(long matAddr, int type);
    
    
    /* 多线程sdk初始化3 */ 
    public native int sdkInit3(String modelPath);
    /* 多线程sdk销毁3（资源释放) */
    public native void sdkDestroy3();
    
    /*  rgb可见光静默活体 */
    public static native LivenessInfo[] rgbLiveness3(long matAddr);
      
    // ********* 以下为系统加载库文件及opencv **********
    static {
        /*  加载dll */
        System.loadLibrary("BaiduFaceApi");
        System.loadLibrary("opencv_java320");
    }

    public static void main(String[] args) {
        /*  sdk初始化 */
        Face api = new Face();
        // model_path为模型文件夹路径，即models文件夹（里面存的是人脸识别的模型文件）
        // 传空为采用默认路径，若想定置化路径，请填写全局路径如：d:\\face （models模型文件夹目录放置后为d:\\face\\models）
        // 若模型文件夹采用定置化路径，则激活文件(license.ini, license.key)也可采用定制化路径放置到该目录如d:\\face\\license
        // 亦可在激活文件默认生成的路径
       // String modelPath ="";
        String modelPath ="d:\\face";
        int res = api.sdkInit(modelPath);
        if (res != 0) {
            System.out.printf("sdk init fail and error =%d\n", res);
            return;
        }
      
        // 获取设备指纹
        String deviceId = Face.getDeviceId();
        System.out.println("device id:" + deviceId);
        // 获取版本号
        String ver = Face.sdkVersion();
        System.out.println("sdk version:" + ver);
        // 人脸示例demo
        long begin = TimeUtil.getTimeStamp();
        FaceDemo demo = new FaceDemo();
        demo.faceSample();
        long end = TimeUtil.getTimeStamp();
        System.out.println("time cost is:" + (end - begin));
        // sdk销毁，释放内存防内存泄漏
        api.sdkDestroy();         
    }
}
