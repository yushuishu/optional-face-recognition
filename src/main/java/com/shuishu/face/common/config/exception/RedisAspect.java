package com.shuishu.face.common.config.exception;


import com.shuishu.blog.common.config.base.ApiResponse;
import com.shuishu.blog.common.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author ：谁书-ss
 * @date ：2023-04-15 15:40
 * @IDE ：IntelliJ IDEA
 * @Motto ：ABC(Always Be Coding)
 * <p></p>
 * @description ：Redis异常AOP
 * <p></p>
 * 在Filter中使用Redis时，无法通过全局异常处理器来处理，
 * 或者在每个使用Redis的过滤器(Filter)中重复编写处理Redis连接异常的逻辑(try cache)，不够优雅和规范。
 */
@Aspect
@Component
public class RedisAspect {

    @AfterThrowing(pointcut = "execution(* com.shuishu.blog.common.utils.RedisUtils.*(..))", throwing = "ex")
    public void handleRedisConnectionFailureException(RedisConnectionFailureException ex) {
        // 处理Redis连接异常
        System.out.println("Redis异常链接");
        System.out.println(ex.getMessage());
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (response != null) {
            ResponseUtils.responseJson(response, ApiResponse.error("缓存服务异常：" + ex.getMessage()));
        }
    }

}
