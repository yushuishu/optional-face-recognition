package com.shuishu.face.common.config.base;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * @author ：谁书-ss
 * @date ：2022-12-24 19:03
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @Description ：
 */
@Schema(description = "分页参数DTO")
public class PageDTO {
    @Schema(description = "每页数目从1开始")
    @Min(value = 1, message = "每页数目从1开始")
    private long pageSize = 5;

    @Schema(description = "页码从1开始")
    @Min(value = 1, message = "页码从1开始")
    private long pageNumber = 1;

    public <T> PageVO<T> toPageVO(Class<T> cl) {
        PageVO<T> pageVo = new PageVO<T>();
        pageVo.setPageNumber(pageNumber);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }
}
