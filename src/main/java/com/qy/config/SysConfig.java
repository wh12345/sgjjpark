package com.qy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Because of you on 2019/12/30.
 */
@Data
@Configuration
public class SysConfig {
    @Value("${sgjjpark.imgneturl}")
    private String imgneturl;
}
