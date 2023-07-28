package com.shuishu.face.common.utils;


import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:22
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：图片工具类
 * <p></p>
 */
public class ImageUtils {


    public static void deleteImage(List<String> imagePathList) {
        if (ObjectUtils.isEmpty(imagePathList)) {
            for (String imagePath : imagePathList) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
