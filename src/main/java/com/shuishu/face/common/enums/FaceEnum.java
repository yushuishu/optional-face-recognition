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

}
