package com.jni.struct;

/**
 * 
 * @author  注意力信息类
 *
 */
public class GazeInfo {
    public int leftEyeDirection; // 左眼凝视方向 0:向上看 1：向下看 2：向右看 3：向左看 4：向前看 5：闭眼
    public float leftEyeConf; // 左眼置信度
    public int rightEyeDirection; // 右眼凝视方向 0:向上看 1：向下看 2：向右看 3：向左看 4：向前看 5：闭眼
    public float rightEyeConf; // 右眼置信度
}
