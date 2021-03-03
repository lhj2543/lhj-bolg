
package com.river.common.security.annotation;

import com.river.common.security.component.RiverResourceServerAutoConfiguration;
import com.river.common.security.component.RiverSecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author river
 * @date 2019/03/08
 * <p>
 * 资源服务注解
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ RiverResourceServerAutoConfiguration.class, RiverSecurityBeanDefinitionRegistrar.class })
public @interface EnableRiverResourceServer {

}
