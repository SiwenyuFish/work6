package com.memo.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "memo.auth")
public class AuthProperties {
    private List<String> excludePaths;
    private List<String> includePaths;
}
