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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.dubbo.bo.RegistryBo;
import org.springframework.boot.dubbo.common.DubboConstant;

import java.util.List;

/**
 * dubbo 注册中心配置
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Data
@ConfigurationProperties(prefix = "dubbo.registry")
public class DubboRegistry {


    /**
     * 全局默认
     * <p>
     * 接口协议  zookeeper
     */
    private String protocol = DubboConstant.PROTOCOL_ZOOKEEPER;


    /**
     * 全局默认
     * <p>
     * 是否向注册中心注册服务
     */
    private boolean register = true;

    /**
     * 全局默认
     * 是否向注册中心订阅服务
     */
    private boolean subscribe = true;


    private String file = "";

    /**
     * 注册中心  registries[0]
     *          registries[1]
     *
     */
    private List<RegistryBo> registries;

}
