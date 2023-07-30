package com.shuishu.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;

/**
 * @Author ：谁书-ss
 * @Date   ： 2023-07-27 22:23
 * @IDE    ：IntelliJ IDEA
 * @Motto  ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：
 */
@SpringBootApplication
public class OptionalFaceRecognitionApplication {
    public static void main(String[] args) {
        // 加载opencv.dll
        URL opencvUrl = OptionalFaceRecognitionApplication.class.getClassLoader().getResource("lib/opencv/opencv_java320.dll");
        if (opencvUrl == null) {
            System.err.println("加载opencv_java320.dll失败");
            return;
        }
        System.load(opencvUrl.getPath());
        SpringApplication.run(OptionalFaceRecognitionApplication.class, args);
    }

}
