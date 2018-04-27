/**
 * Copyright 2016-2018 lee123lee123.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.boot.dubbo.autoconfigure;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.dubbo.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基础配置 自动注册
 *
 * @author   喝咖啡的囊地鼠
 *
 */

@Slf4j
@Configuration
@ConditionalOnClass(Exporter.class)
@ConditionalOnBean(annotation = EnableDubbo.class)
@EnableConfigurationProperties({
        DubboAnnotaion.class,
        DubboApplication.class,
        DubboProtocol.class,
        DubboRegistry.class,
        DubboProvider.class,
        DubboConsumer.class})
public class DubboAutoConfiguration {

    @Autowired
    private DubboApplication dubboApplication;

    @Autowired
    private DubboProtocol dubboProtocol;


    /**
     * 注解bean
     *
     * @param packageName  要扫描的包名
     * @return 注解bean
     * @see com.alibaba.dubbo.config.spring.AnnotationBean
     */
    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package-name}") String packageName) {
        if (StringUtils.isBlank(packageName)) {
            throw new IllegalArgumentException("annotation package is empty!");
        }
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(packageName);
        return annotationBean;
    }

    /**
     * 应用 配置
     *
     * @return dubbo应用配置
     * @see com.alibaba.dubbo.config.ApplicationConfig
     *
     *
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        if (StringUtils.isBlank(dubboApplication.getName())) {
            throw new IllegalArgumentException("application name is empty");
        }
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setOwner(dubboApplication.getOwner());
        applicationConfig.setVersion(dubboApplication.getVersion());
        applicationConfig.setName(dubboApplication.getName());
        applicationConfig.setLogger(dubboApplication.getLogger());
        return applicationConfig;
    }


    /**
     * 协议配置
     * @return 协议配置
     * @see com.alibaba.dubbo.config.ProtocolConfig
     */
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(dubboProtocol.getName());
        protocolConfig.setPort(dubboProtocol.getPort());
        protocolConfig.setThreads(dubboProtocol.getThreads());
        protocolConfig.setAccesslog(String.valueOf(dubboProtocol.isAccessLog()));
        return protocolConfig;
    }

}
