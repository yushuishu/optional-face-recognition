package com.jni.face;

/**
 * 
 * 华捷艾米镜头模组接入类(支持A200)
 *
 */
public class Aimi {
    /*  初始化华杰艾米摄像头 */
    public static native int openHjimi();
    /*  打开华杰艾米摄像头 */
    public static native int cameraHjimi(long rgbAddr, long depthAddr);
    /*  释放华杰艾米摄像头 */
    public static native int closeHjimi();
    static {
        /*  加载dll */
        System.loadLibrary("AimiCamera");
    }
}
