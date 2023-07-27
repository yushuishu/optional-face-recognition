package com.shuishu.face.common.config.id;


import com.alibaba.fastjson2.JSONObject;

/**
 * @author ：谁书-ss
 * @date ：2022-12-25 11:51
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：
 */
public class IdGenerateUtil {
    private IdGenerateUtil() {
    }

    private final IdGenerate idGenerate = new IdGenerate(0, 0);

    private static class SingletonInstance {
        private static final IdGenerateUtil INSTANCE = new IdGenerateUtil();
    }

    public static IdGenerateUtil getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public long getId() {
        return idGenerate.nextId();
    }

    public long getId(long workerId) {
        return idGenerate.nextId(workerId);
    }

    public long getIdWithTimestamp(long timestamp) {
        return idGenerate.nextIdWithTimestamp(timestamp);
    }

    public JSONObject getIdInfo(long id) {
        return idGenerate.parseInfo(id);
    }
}
