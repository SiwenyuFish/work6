package com.memo.api.config;

import com.memo.common.util.ThreadLocalUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            public void apply(RequestTemplate template) {
                Long userId=ThreadLocalUtil.get();
                if(userId!=null){
                    template.header("user-info", String.valueOf(userId));

                }
            }
        };
    }

}
