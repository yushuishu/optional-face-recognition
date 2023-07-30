package com.jni.face;

/**
 * 
 * 奥比中光镜头模组接入类(支持如海燕等)
 *
 */
public class Orbe {
    /*  ********以下为镜头模组接口********* 
    /*  初始化奥比摄像头 */
    public static native int openOrbe();
    /*  打开奥比摄像头 */
    public static native int cameraOrbe(long rgbAddr, long depthAddr);
    /*  释放奥比摄像头 */
    public static native int closeOrbe();
    static {
        /*  加载dll */
        System.loadLibrary("OrbeCamera");
    }
}
