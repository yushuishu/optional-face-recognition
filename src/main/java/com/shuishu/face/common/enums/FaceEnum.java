package com.shuishu.face.common.enums;


/**
 * @Author ：谁书-ss
 * @Date ：2023-07-27 23:32
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸枚举
 * <p></p>
 */
public interface FaceEnum {
    /**
     * 性别
     */
    enum Gender {
        /**
         * 性别
         */
        UNKNOWN("UNKNOWN", "未知"),
        MALE("MALE", "男"),
        FEMALE("FEMALE", "女")

        ;

        private final String code;
        private final String des;

        Gender(String code, String des) {
            this.code = code;
            this.des = des;
        }

        public String getCode() {
            return code;
        }

        public String getDes() {
            return des;
        }
    }

    enum BaiduApi {
        /**
         * 百度API
         */
        ACCESS_TOKEN("https://aip.baidubce.com/oauth/2.0/token?", "获取accessToken"),
        URL_DETECT("https://aip.baidubce.com/rest/2.0/face/v3/detect", "人脸检测"),
        URL_USER_ADD("https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add", "人脸注册"),
        URL_GET_USER_INFO("https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/get", "查询用户信息"),
        URL_SEARCH("https://aip.baidubce.com/rest/2.0/face/v3/search", "人脸识别 1:N"),
        URL_MULTI_SEARCH("https://aip.baidubce.com/rest/2.0/face/v3/multi-search", "人脸识别 M:N"),
        URL_MATCH("https://aip.baidubce.com/rest/2.0/face/v3/match", "人脸对比（两张图片对比）"),
        URL_USER_UPDATE("https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update", "人脸更新"),
        URL_USER_FACE_DELETE("https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete", "人脸删除"),
        URL_USER_DELETE("https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete", "用户删除"),
        URL_USER_GROUP_DELETE("https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/delete", "用户组删除"),
        URL_SEARCH_GROUP("https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/getlist", "组查询（馆ID）"),
        ;

        private final String url;
        private final String des;

        BaiduApi(String url, String des) {
            this.url = url;
            this.des = des;
        }

        public String getUrl() {
            return url;
        }

        public String getDes() {
            return des;
        }
    }

}
