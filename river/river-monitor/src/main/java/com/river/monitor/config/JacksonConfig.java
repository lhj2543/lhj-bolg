package com.river.monitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.boot.admin.server.config.AdminServerWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * JacksonConfig 扩展增加 spring boot 默认配置
 * <p>
 * 自身序列化机制 {@link de.codecentric.boot.admin.server.utils.jackson.AdminServerModule}
 *
 * @author river
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureAfter(AdminServerWebConfiguration.class)
public class JacksonConfig {

	/**
	 * 覆盖core 模块提供的序列方式，增加springboot admin 序列化
	 * @param adminJacksonModule spring boot admin 提供的序列化
	 * @return Jackson customizer
	 */
	/*@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer(SimpleModule adminJacksonModule) {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
			builder.modules(new PigJavaTimeModule(), adminJacksonModule);
		};
	}*/

}
