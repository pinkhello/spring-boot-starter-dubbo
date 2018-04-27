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
package org.springframework.boot.dubbo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.dubbo.common.DubboConstant;

import java.io.Serializable;

/**
 * 注册中心 具体配置
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryBo implements Serializable{


    /**
     * 注册中心名称 * 在 Spring Bean 的名称
     */
    private String name = "default";

    /**
     * 注册根节点组  dubbo
     */
    private String group = "dubbo";


    /**
     * 注册中心地址 127.0.0.1:2181
     */
    private String address = "127.0.0.1:2181";


    /**
     * 接口协议  zookeeper
     */
    private String protocol = DubboConstant.PROTOCOL_ZOOKEEPER;


    /**
     * 注册中心检查
     */
    private boolean check = false;


}
