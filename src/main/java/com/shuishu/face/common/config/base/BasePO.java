package com.shuishu.face.common.config.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：谁书-ss
 * @date ：2022-12-24 18:29
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：持久化对象(Persistent Object)
 */
@Setter
@Getter
@ToString
@Schema(description = "持久化对象BasePO")
@MappedSuperclass
public class BasePO implements Serializable {
    @CreatedDate
    @Schema(description = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("创建时间")
    private Date createDate;

    @CreatedBy
    @Schema(description = "创建人id", hidden = true)
    @Comment("创建人id")
    private Long createUserId;

    @LastModifiedDate
    @Schema(description = "修改时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("修改时间")
    private Date updateDate;

    @LastModifiedBy
    @Schema(description = "修改人id", hidden = true)
    @Comment("修改人id")
    private Long updateUserId;


    public <T extends BaseVO<?>> T toVo(Class<T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
