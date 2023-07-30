package com.shuishu.face.common.dsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.shuishu.face.common.config.jdbc.BaseDsl;
import com.shuishu.face.common.entity.dto.FaceQueryDTO;
import com.shuishu.face.common.entity.po.QFace;
import com.shuishu.face.common.entity.vo.FaceVO;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
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


    private final QBean<FaceVO> faceVoQBean = Projections.fields(FaceVO.class,
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
    );

    /**
     * 模糊查询 绑定人脸
     *
     * @param faceQueryDTO -
     * @return -
     */
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
        return jpaQueryFactory.select(faceVoQBean)
                .from(qFace)
                .where(builder)
                .fetch();
    }

    /**
     * 查询读者绑定次数
     *
     * @param libraryCode -馆code
     * @param barcode     -读者证条码
     * @return -
     */
    public int findBindingCount(String libraryCode, String barcode) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(libraryCode)) {
            builder.and(qFace.libraryCode.eq(libraryCode));
        }
        if (StringUtils.hasText(barcode)) {
            builder.and(qFace.barcode.eq(barcode));
        }
        return jpaQueryFactory.select(qFace.faceId).from(qFace).where(builder).fetch().size();
    }

    public List<FaceVO> findByBarcode(String libraryCode, String barcode) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(libraryCode)) {
            builder.and(qFace.libraryCode.eq(libraryCode));
        }
        if (StringUtils.hasText(barcode)) {
            builder.and(qFace.barcode.eq(barcode));
        }
        return jpaQueryFactory.select(faceVoQBean)
                .from(qFace)
                .where(builder)
                .fetch();
    }

    public List<FaceVO> findByBarcodeList(String libraryCode, List<String> barcodeList) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(libraryCode)) {
            builder.and(qFace.libraryCode.eq(libraryCode));
        }
        if (!ObjectUtils.isEmpty(barcodeList)) {
            builder.and(qFace.barcode.in(barcodeList));
        }
        return jpaQueryFactory.select(faceVoQBean)
                .from(qFace)
                .where(builder)
                .fetch();
    }
}
