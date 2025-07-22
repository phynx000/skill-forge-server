package com.skillforge.skillforge_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bunny.stream")
@Data
public class BunnyStreamConfig {
    private String libraryId;
    private String accessKey;
    private String baseUrl = "https://video.bunnycdn.com";
}