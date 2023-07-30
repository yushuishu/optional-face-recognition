package com.shuishu.face.common.entity.bo.baidu;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-30 17:16
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：
 * <p></p>
 */
@Setter
@Getter
@ToString
public class Feature {
    /**
     * 特征值大小,通常为512个byte
     */
    public int size;
    /**
     * 特征值数据
     */
    public byte[] data;

}
