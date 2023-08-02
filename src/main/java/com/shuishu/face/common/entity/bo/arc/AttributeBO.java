package com.shuishu.face.common.entity.bo.arc;


import lombok.ToString;

/**
 * @Author ：谁书-ss
 * @Date ：2023-08-02 21:16
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸属性
 * <p></p>
 */
@ToString
public class AttributeBO {
    /**
     * 年龄
     */
    public int age = 0;
    /**
     * 种族  0：黄种人 1：白种人 2：黑种人 3：印第安人
     */
    public int race;
    /**
     * 表情属性 0：皱眉 1：笑 2：平静
     */
    public int emotion;
    /**
     * 眼镜佩戴属性  0：无眼镜 1：有眼镜 2：墨镜
     */
    public int glasses;
    /**
     * 性别 -1：未知  0：女性 1：男性
     */
    public int gender = -1;
    /**
     * RGB活体值，未知=-1 、非活体=0 、活体=1、超出人脸=-2
     */
    public int liveness = -1;
    /**
     * "0" 代表没有戴口罩，"1"代表戴口罩 ,"-1"表不确定
     */
    public int mask = -1;
}
