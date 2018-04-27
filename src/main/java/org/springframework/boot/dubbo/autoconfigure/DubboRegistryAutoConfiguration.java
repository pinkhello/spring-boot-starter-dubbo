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

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Exporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.dubbo.annotation.EnableDubbo;
import org.springframework.boot.dubbo.bo.RegistryBo;
import org.springframework.boot.dubbo.common.DubboConstant;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 注册中心自动注册
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Slf4j
@Configuration
@ConditionalOnClass(Exporter.class)
@ConditionalOnBean(annotation = EnableDubbo.class)
@AutoConfigureAfter(DubboAutoConfiguration.class)
@EnableConfigurationProperties({
        DubboAnnotaion.class,
        DubboApplication.class,
        DubboProtocol.class,
        DubboRegistry.class,
        DubboProvider.class,
        DubboConsumer.class})
public class DubboRegistryAutoConfiguration implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String REGISTERS_PREFIX = "registries";

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private DubboRegistry dubboRegistry;

//    @Bean
//    public RegistryConfig registryConfig() {
//        log.debug("RegistryConfig:{}", dubboRegistry);
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.setGroup(dubboRegistry.getGroup());
//        registryConfig.setProtocol(dubboRegistry.getProtocol());
//        registryConfig.setAddress(dubboRegistry.getAddress();
//        registryConfig.setRegister(dubboRegistry.isRegister());
//        registryConfig.setSubscribe(dubboRegistry.isSubscribe());
//        return registryConfig;
//    }



    @Override
    public void setEnvironment(Environment environment) {
        initDubboRegistry(environment);
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        registerDubboRegistries(beanDefinitionRegistry);
    }


    private void registerDubboRegistries(BeanDefinitionRegistry beanDefinitionRegistry) {
        String protocol = dubboRegistry.getProtocol();
        boolean isRegister = dubboRegistry.isRegister();
        boolean isSubscribe = dubboRegistry.isSubscribe();
        if (dubboRegistry.getRegistries() == null || dubboRegistry.getRegistries().isEmpty()) {
            throw new IllegalArgumentException("registry config is empty!");
        }

        int isDefault = 1;
        final int[] count = {0};
        dubboRegistry.getRegistries().stream().forEach(registryBo -> {

            count[0]++;
            AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(RegistryConfig.class);

            if (registryBo.getProtocol() == null || registryBo.getProtocol().isEmpty()) {
                registryBo.setProtocol(protocol);
            }

            MutablePropertyValues mpv = abd.getPropertyValues();
            mpv.add("address", registryBo.getAddress());
            mpv.add("group", registryBo.getGroup());
            mpv.add("protocol", registryBo.getProtocol());
            mpv.add("check", registryBo.isCheck());
            mpv.add("subscribe", isSubscribe);
            mpv.add("register", isRegister);
            if (dubboRegistry.getFile() != null && !dubboRegistry.getFile().isEmpty()){
                mpv.add("file", dubboRegistry.getFile()+"/"+registryBo.getName());
            }
             if (isDefault == count[0]) {
                mpv.add("default", true);
            }


            registerBean(beanDefinitionRegistry, registryBo.getName(), abd);


        });
    }

    private void registerBean(BeanDefinitionRegistry registry, String name, AnnotatedGenericBeanDefinition abd) {

        ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        //没有注册名字的话使用生成注册名字
        String beanName = (name != null ? name : beanNameGenerator.generateBeanName(abd, registry));
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        log.info("register bean : {} - {}", beanName, abd.getBeanClassName());
    }


    private void initDubboRegistry(Environment environment) {
        DubboRegistry def = new DubboRegistry();
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "dubbo.registry.");

        Map<String, Object> maps = propertyResolver.getSubProperties("");

        //默认配置中心全局配置
        String protocol = def.getProtocol();
        boolean register = def.isRegister();
        boolean subscribe = def.isSubscribe();
        String file = def.getFile();

        if (maps.containsKey(DubboConstant.FILE)){
            String temp = propertyResolver.getProperty(DubboConstant.FILE, String.class);
            if (temp != null && !temp.isEmpty()) {
                file = temp;
            }
        }
        if (maps.containsKey(DubboConstant.PROTOCOL)) {
            String temp = propertyResolver.getProperty(DubboConstant.PROTOCOL, String.class);
            if (temp != null && !temp.isEmpty()) {
                protocol = temp;
            }
        }
        if (maps.containsKey(DubboConstant.REGISTER)) {
            String temp = propertyResolver.getProperty(DubboConstant.REGISTER, String.class);
            if (temp != null && !temp.isEmpty()) {
                register = Boolean.parseBoolean(temp);
            }
        }
        if (maps.containsKey(DubboConstant.SUBSCRIBE)) {

            String temp = propertyResolver.getProperty(DubboConstant.SUBSCRIBE, String.class);
            if (temp != null && !temp.isEmpty()) {
                subscribe = Boolean.parseBoolean(temp);
            }
        }
        maps = propertyResolver.getSubProperties(REGISTERS_PREFIX);
        if (maps.isEmpty()) {
            throw new RuntimeException("registry config is empty!  dubbo.registry.registries[i]");
        }
        setDubboRegistry(protocol, register, subscribe, file, builderRegistryBos(maps, propertyResolver));
    }

    private List<RegistryBo> builderRegistryBos(Map<String, Object> maps, RelaxedPropertyResolver propertyResolver) {
        List<String> nodes = maps.keySet().stream().map(e -> e.substring(0, 3)).distinct().collect(Collectors.toList());
        int nodeSize = nodes.size();
        List<RegistryBo> registryBos = new ArrayList<>(nodeSize);
        nodes.stream().forEach(k -> {
            String name = propertyResolver.getProperty(REGISTERS_PREFIX + k + ".name", String.class);
            String address = propertyResolver.getProperty(REGISTERS_PREFIX + k + ".address", String.class);
            String group = propertyResolver.getProperty(REGISTERS_PREFIX + k + ".group", String.class);
            String nodeProtocol = propertyResolver.getProperty(REGISTERS_PREFIX + k + ".protocol", String.class);
            RegistryBo bo = new RegistryBo();
            if (name != null && !name.isEmpty()) {
                bo.setName(name);
            }
            if (address != null && !address.isEmpty()) {
                bo.setAddress(address);
            }
            if (group != null && !group.isEmpty()) {
                bo.setGroup(group);
            }
            if (nodeProtocol != null && !nodeProtocol.isEmpty()) {
                bo.setProtocol(nodeProtocol);
            }
            registryBos.add(bo);
        });
        return registryBos;
    }

    private void setDubboRegistry(String protocol, boolean register, boolean subscribe,String file, List<RegistryBo> registryBos) {
        dubboRegistry = new DubboRegistry();
        dubboRegistry.setProtocol(protocol);
        dubboRegistry.setRegister(register);
        dubboRegistry.setSubscribe(subscribe);
        dubboRegistry.setFile(file);
        dubboRegistry.setRegistries(registryBos);
    }


}
