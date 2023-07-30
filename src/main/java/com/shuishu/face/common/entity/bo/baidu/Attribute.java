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
public class Attribute {
    /**
     * 年龄
     */
    public int age;
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
     * 性别  0：女性 1：男性
     */
    public int gender;
}
