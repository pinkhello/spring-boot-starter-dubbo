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

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Exporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.dubbo.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 提供方自动注册
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Slf4j
@Configuration
@ConditionalOnClass(Exporter.class)
@ConditionalOnBean(annotation = EnableDubbo.class)
@AutoConfigureAfter({DubboAutoConfiguration.class, DubboRegistryAutoConfiguration.class})
@EnableConfigurationProperties({
        DubboAnnotaion.class,
        DubboApplication.class,
        DubboProtocol.class,
        DubboRegistry.class,
        DubboProvider.class,
        DubboConsumer.class})
public class DubboProviderAutoConfiguration {


    @Autowired
    private DubboProvider dubboProvider;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ProtocolConfig protocolConfig;

    @Autowired
    private List<RegistryConfig> registries;


    /**
     *  提供方配置
     * @return 提供方配置
     * @see com.alibaba.dubbo.config.ProviderConfig
     */
    @Bean
    public ProviderConfig providerConfig() {
        log.info("ProviderConfig:{}", dubboProvider);
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(dubboProvider.getTimeout());
        providerConfig.setRetries(dubboProvider.getRetries());
        providerConfig.setFilter(dubboProvider.getFilter());
        providerConfig.setDelay(dubboProvider.getDelay());
        providerConfig.setValidation(dubboProvider.getValidation().toString());
        providerConfig.setPayload(dubboProvider.getPayload());
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistries(registries);
        providerConfig.setProtocol(protocolConfig);
        return providerConfig;
    }
}
