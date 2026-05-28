package com.d0dd.wms.config;

import com.d0dd.wms.common.MultiDateDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JacksonConfig {

    /**
     * 全局配置 Jackson 将 Long 类型序列化为 String，
     * 解决前端 JavaScript 处理大整数（超过 17 位）时的精度丢失问题。
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.deserializerByType(Date.class, new MultiDateDeserializer());
        };
    }
}
