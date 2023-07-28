package com.shuishu.face.common.dsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.shuishu.face.common.config.jdbc.BaseDsl;
import com.shuishu.face.common.entity.dto.FaceQueryDTO;
import com.shuishu.face.common.entity.po.QFace;
import com.shuishu.face.common.entity.vo.FaceVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author ：谁书-ss
 * @Date ：2023-07-28 9:13
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：人脸Dsl
 * <p></p>
 */
@Component
public class FaceDsl extends BaseDsl {
    private final QFace qFace = QFace.face;


    public List<FaceVO> findFaceList(FaceQueryDTO faceQueryDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(faceQueryDTO.getBarcode())) {
            builder.and(qFace.barcode.eq(faceQueryDTO.getBarcode()));
        }
        if (StringUtils.hasText(faceQueryDTO.getLibraryCode())) {
            builder.and(qFace.libraryCode.eq(faceQueryDTO.getLibraryCode()));
        }
        if (StringUtils.hasText(faceQueryDTO.getDeviceSerialNumber())) {
            builder.and(qFace.deviceSerialNumber.eq(faceQueryDTO.getDeviceSerialNumber()));
        }
        return jpaQueryFactory.select(Projections.fields(FaceVO.class,
                qFace.faceId,
                qFace.barcode,
                qFace.age,
                qFace.gender,
                qFace.featureSize,
                qFace.featureByte,
                qFace.featureData,
                qFace.originalImageUrl,
                qFace.cropImageUrl,
                qFace.quality,
                qFace.angelFuYang,
                qFace.angelLeftRight,
                qFace.angelPlane,
                qFace.scene,
                qFace.libraryCode,
                qFace.deviceSerialNumber,
                qFace.createApiName,
                qFace.updateApiName
        ))
                .from(qFace)
                .where(builder)
                .fetch();
    }
}
