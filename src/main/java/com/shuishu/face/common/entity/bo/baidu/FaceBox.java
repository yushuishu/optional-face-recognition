package com.shuishu.face.common.entity.bo.baidu;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-30 17:15
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：
 * <p></p>
 */
@Setter
@Getter
@ToString
public class FaceBox {
    /**
     * 人脸框索引
     */
    public int index;
    /**
     * 人脸框中心x坐标
     */
    public float centerx;
    /**
     * 人脸框中心y坐标
     */
    public float centery;
    /**
     * 人脸框宽度
     */
    public float width;
    /**
     * 人脸框高度
     */
    public float height;
    /**
     * 人脸角度
     */
    public float angle;
    /**
     * 人脸置信度分值
     */
    public float score;
}
