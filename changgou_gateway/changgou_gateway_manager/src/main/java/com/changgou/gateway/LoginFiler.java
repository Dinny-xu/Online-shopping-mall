package com.changgou.gateway;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class LoginFiler implements GlobalFilter, Ordered {

    //请求头中  令牌的key值
    private final String TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /**
         * 1：获取请求头  或是cookie中的令牌
         * 2：判断是否是登录  如果是登录可以放行  如果不是登录，必须校验JWT 令牌
         * 3：Jwt令牌解析正确，证明：已经登录  如果解析错误，设置响应值为401 没有权限，并响应返回
         *
         */

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        String path = request.getURI().getPath();
        if (!request.getURI().getPath().equals("/admin/login")) {

            HttpHeaders headers = request.getHeaders();
            String token = headers.getFirst(TOKEN);
            if (!StringUtils.isEmpty(token)) {

                try {
                    Claims claims = JwtUtil.parseJWT(token);
                } catch (Exception e) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            }else {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
