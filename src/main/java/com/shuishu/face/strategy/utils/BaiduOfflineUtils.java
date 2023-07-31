package com.shuishu.face.strategy.utils;


import com.alibaba.fastjson2.JSON;
import com.jni.face.Face;
import com.jni.struct.Attribute;
import com.jni.struct.Feature;
import com.jni.struct.FeatureInfo;
import com.jni.struct.Occlusion;
import com.shuishu.face.common.utils.FileUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 14:19
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：百度离线SDK 操作函数
 * <p></p>
 */
public class BaiduOfflineUtils {
    private static final Logger logger = LoggerFactory.getLogger(BaiduOfflineUtils.class);


    /**
     * 初始化 Face SDK
     *
     * @return -
     */
    private static Face faceSdkInit(){
        // face 初始化
        Face api = new Face();
        int res = api.sdkInit("");
        if (res != 0) {
            System.out.printf("sdk init fail and error =%d", res);
            return null;
        }
        return api;
    }

    /**
     * 销毁 Face SDK
     *
     * @param api -
     */
    private static void faceSdkDestroy(Face api){
        // sdk销毁，释放内存防内存泄漏
        if (api != null){
            api.sdkDestroy();
        }
    }


    /**
     * 人脸检测
     * @param file -
     * @return -
     */
    public static String imageDetect(MultipartFile file){
        logger.info("【人脸检测】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return null;
        }
        long matAddr = mat.getNativeObjAddr();
        // type 0: 表示RGB人脸检测 1：表示nir人脸检测
        String json = JSON.toJSONString(Face.detect(matAddr, 0));
        System.out.println(json);
        return json;
    }

    /**
     * 口罩佩戴检测
     * @param file -
     * @return -
     */
    public static String imageMouthMask(MultipartFile file) {
        logger.info("【口罩佩戴检测】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return null;
        }
        long matAddr = mat.getNativeObjAddr();
        String json = JSON.toJSONString(Face.faceMouthMask(matAddr));
        System.out.println(json);
        return json;
    }

    /**
     * 模糊度：取值范围[0~1]，0是最清晰，1是最模糊 (推荐0.7)
     * @param file -
     * @return -
     */
    public static float imageBlur(MultipartFile file){
        logger.info("【模糊度检测】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            System.out.println("1");
            return 1;
        }
        long matAddr = mat.getNativeObjAddr();
        float[] blurList = Face.faceBlur(matAddr);
        if (blurList == null || blurList.length <= 0) {
            System.out.println("1");
            return 1;
        }
        System.out.println(blurList[0]);
        return blurList[0];
    }

    /**
     * 遮挡度:
     * @param file -
     * @return - null代表正常
     */
    public static String imageOcclusion(MultipartFile file){
        logger.info("【遮挡度检测】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return "未检测到人脸！";
        }
        long matAddr = mat.getNativeObjAddr();
        Occlusion[] occlList = Face.faceOcclusion(matAddr);

        if (occlList == null || occlList.length <= 0) {
            System.out.println("detect no face");
            return "未检测到人脸！";
        }

        float occlu = 0.1f;
        // 左眼遮挡置信度分值
        if (occlList[0].leftEye > occlu){
            System.out.println("left_eye occlu score is:" + occlList[0].leftEye);
            return "检测到左眼遮挡置";
        }
        // 右眼遮挡置信度分值
        if (occlList[0].rightEye > occlu){
            System.out.println("right_eye occlu score is:" + occlList[0].rightEye);
            return "检测到右眼遮挡置";
        }
        // 鼻子遮挡置信度分值
        if (occlList[0].nose > occlu){
            System.out.println("nose occlu score is:" + occlList[0].nose);
            return "检测到鼻子遮挡置";
        }
        // 嘴巴遮挡置信度分值
        if (occlList[0].mouth > occlu){
            System.out.println("mouth occlu score is:" + occlList[0].mouth);
            return "检测到嘴巴遮挡置";
        }
        // 左脸遮挡置信度分值
        if (occlList[0].leftCheek > occlu){
            System.out.println("left_cheek occlu score is:" + occlList[0].leftCheek);
            return "检测到左脸遮挡置";
        }
        // 右脸遮挡置信度分值
        if (occlList[0].rightCheek > occlu){
            System.out.println("right_cheek occlu score is:" + occlList[0].rightCheek);
            return "检测到右脸遮挡置";
        }
        // 下巴遮挡置信度分值
        if (occlList[0].chin > occlu){
            System.out.println("chin occlu score is:" + occlList[0].chin);
            return "检测到下巴遮挡置";
        }
        return null;
    }

    /**
     * 光照检测
     * @param file -
     * @return -null代表正常
     */
    public static String imageIllumination(MultipartFile file){
        logger.info("【光照检测】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return "未检测到人脸！";
        }
        long matAddr = mat.getNativeObjAddr();
        float[] illuList = Face.faceIllumination(matAddr);
        if (illuList == null || illuList.length <= 0) {
            System.out.println("detect no face");
            return "未检测到人脸！";
        }
        if (illuList[0] < 50){
            System.out.println("illumination score is:" + illuList[0]);
            return "光线过暗";
        }
        return null;
    }

    /**
     * 人脸属性
     * @param file -
     * @return -
     */
    public static String imageAttr(MultipartFile file){
        logger.info("【人脸属性】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return null;
        }
        long matAddr = mat.getNativeObjAddr();
        Attribute[] attrList = Face.faceAttr(matAddr);
        if (attrList == null || attrList.length <= 0) {
            System.out.println("detect no face");
            return null;
        }
        String json = JSON.toJSONString(attrList);
        System.out.println(json);
        return json;
    }

    /**
     * rgb+depth深度特征值提取示例 (rgbd的特征值大小为1024个byte)
     * @param file -
     */
    public static String imageRgbdFeature(MultipartFile file){
        logger.info("【rgb+depth深度特征值提取示例 (rgbd的特征值大小为1024个byte）】");
        Mat rgbMat = FileUtils.convertMultipartFileToMat(file);
        if (rgbMat == null){
            return null;
        }
        long matAddr = rgbMat.getNativeObjAddr();
        // type 0： 表示rgb 生活照特征值 1: 表示rgb证件照特征值 2：表示nir近红外特征值
        int type = 0;
        FeatureInfo[] feaList = Face.faceFeature(matAddr, type);
        if (feaList != null && feaList.length > 0) {
            String json = JSON.toJSONString(feaList[0].feature);
            System.out.println(json);
            return json;
        }
        return null;
    }

    /**
     * 特征值比较得分
     * @param str1 -
     * @param str2 -
     * @param type -
     * @return -
     */
    public static float compareFeature(String str1, String str2, int type){
        logger.info("【特征值比较得分】");
        Feature f1 = JSON.parseObject(str1, Feature.class);
        Feature f2 = JSON.parseObject(str2, Feature.class);
        float v = Face.compareFeature(f1, f2, type);
        return v;
    }

    /**
     * 抠图
     * @param file     -文件
     * @param fileName -图片路径(包含名称)
     */
    public static String imageCrop(MultipartFile file, String fileName){
        logger.info("【人脸抠图】");
        Mat mat = FileUtils.convertMultipartFileToMat(file);
        if (mat == null){
            return "人脸图片转换异常";
        }
        long matAddr = mat.getNativeObjAddr();
        // 定义输出mat
        Mat outMat = new Mat();
        long outAddr = outMat.getNativeObjAddr();

        int res = Face.faceCrop(matAddr, outAddr);
        if (res != 0) {
            System.out.println("crop error and res is:" + res);
            return "人脸检测失败！";
        }
        try {
            if (outMat.empty()) {
                System.out.println("face crop fail");
                return "人脸检测失败！";
            }
        }catch (Exception e) {
            // 未检测到人脸或其他原因导致sdk无图片返回
            System.out.println("outMat empty exception");
            return "人脸检测失败！";
        }
        File fileTemp = new File(fileName);
        File parentFile = fileTemp.getParentFile();
        if (!parentFile.exists()){
            if(!parentFile.mkdirs()){
                System.out.println("创建文件夹异常！");
                return "人脸检测异常！";
            }
        }
        // 抠图完毕可保存到本地
        Imgcodecs.imwrite(fileName, outMat);
        return null;
    }

}
