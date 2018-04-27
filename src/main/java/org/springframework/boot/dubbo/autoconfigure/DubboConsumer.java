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
import org.springframework.boot.dubbo.common.DubboConstant;

/**
 * dubbo 消费方配置
 *
 * @author   喝咖啡的囊地鼠
 *
 */

@Data
@ConfigurationProperties(prefix = "dubbo.consumer")
public class DubboConsumer {

    /**
     * 服务的超时时间,单位毫秒
     */
    private int timeout = 10000;

    /**
     * 调用失败重试次数
     */
    private int retries = 0;

    /**
     * 过滤器
     */
    private String filter = DubboConstant.INTERFACE_LOG_FILTER;

    /**
     * 是否校验参数
     */
    private Boolean validation = DubboConstant.VALIDATION;

    /**
     * 消费端是否娇艳
     */
    private Boolean check = DubboConstant.CHECK;

}
