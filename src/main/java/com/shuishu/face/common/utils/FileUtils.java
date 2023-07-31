package com.shuishu.face.common.utils;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:22
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：图片工具类
 * <p></p>
 */
public class FileUtils {

    /**
     * 批量删除文件
     *
     * @param filePathList 文件路径
     */
    public static void deleteFileList(List<String> filePathList) {
        if (!ObjectUtils.isEmpty(filePathList)) {
            for (String filePath : filePathList) {
                try {
                    File fileFile = new File(filePath);
                    if (fileFile.exists()) {
                        fileFile.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重新设置文件名称
     *
     * @param multipartFile -文件
     * @param barcode -读者证
     * @param libraryCode -馆code
     * @return -
     */
    public static String generateImageName(MultipartFile multipartFile, String barcode, String libraryCode){
        String suffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        if (!StringUtils.hasText(suffix)) {
            suffix = ".jpg";
        }
        if (!StringUtils.hasText(barcode)) {
            return String.format("FACE-%s-%s", libraryCode, DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)) + suffix;
        }
        return String.format("FACE-%s-%s-%s", libraryCode, barcode, DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)) + suffix;
    }

}
