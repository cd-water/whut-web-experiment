package com.cdwater.interceptor;

import com.cdwater.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        String token = request.getHeader("token");

        //判断token是否存在，如果不存在，说明用户没有登录，返回错误信息（响应401状态码）
        if (token == null || token.isEmpty()) {
            log.info("令牌为空，响应401状态码");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //如果token存在，校验令牌，如果校验失败->返回401
        try {
            Claims claims = JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌失效，响应401状态码");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //校验通过，放行
        log.info("令牌合法，放行");
        return true;
    }
}
