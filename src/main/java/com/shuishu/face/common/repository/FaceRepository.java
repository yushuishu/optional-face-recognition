package com.shuishu.face.common.repository;


import com.shuishu.face.common.config.jdbc.BaseRepository;
import com.shuishu.face.common.entity.po.Face;
import org.springframework.stereotype.Repository;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-28 9:12
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸Repository
 * <p></p>
 */
@Repository
public interface FaceRepository extends BaseRepository<Face, Long> {
}
