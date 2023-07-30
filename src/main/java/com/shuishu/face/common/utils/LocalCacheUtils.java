package com.shuishu.face.common.utils;


import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.shuishu.face.common.enums.FaceEnum;
import com.shuishu.face.common.properties.FaceProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-30 16:34
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：本地缓存
 * <p></p>
 */
@RequiredArgsConstructor
@Component
public class LocalCacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(LocalCacheUtils.class);

    private final FaceProperties faceProperties;

    /**
     * 缓存百度 在线人脸识别，AccessToken
     */
    private final Cache<String, String> cacheBaiduAccessToken = CacheBuilder.newBuilder()
            //设置cache的初始大小为5
            .initialCapacity(5)
            //设置并发数为cpu核心数，即同一时间最多只能有几个线程往cache执行写入操作
            .concurrencyLevel(5)
            //设置cache中的数据在写入之后的存活时间为28 天 (百度的存活时间为30天)
            .expireAfterWrite(28, TimeUnit.DAYS)
            //构建cache实例
            .build();

    /**
     * 获取权限token
     *
     * @return 返回示例：
     * {
     * "access_token": "24.f11c014b6cdf002a9d0a328ae2223b06.2592000.1640156468.282335-25209037",
     * "expires_in": 2592000
     * }
     */
    public String getAuth() {
        String accessToken = cacheBaiduAccessToken.getIfPresent("baidu_face_token");
        if (StringUtils.hasText(accessToken)) {
            return accessToken;
        }
        FaceProperties.BaiduProperties baiduProperties = faceProperties.getBaiduProperties();
        // 官网获取的 API Key 更新为你注册的
        String clientId = baiduProperties.getAppId();
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = baiduProperties.getSecretKey();
        String newestAccessToken = getAuth(clientId, clientSecret);
        if (StringUtils.hasText(newestAccessToken)) {
            cacheBaiduAccessToken.put("baidu_face_token", newestAccessToken);
        }
        return newestAccessToken;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Secret Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public String getAuth(String ak, String sk) {
        // 获取token地址
        String getAccessTokenUrl = FaceEnum.BaiduApi.ACCESS_TOKEN.getUrl()
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                logger.info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            //返回结果示例
            JSONObject jsonObject = JSONObject.parseObject(result.toString());
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            logger.error("获取access_token失败：\n{}", (Object) e.getStackTrace());
        }
        return null;
    }


}
