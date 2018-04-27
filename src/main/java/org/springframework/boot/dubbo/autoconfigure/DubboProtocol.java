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
 * dubbo 协议配置
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Data
@ConfigurationProperties(prefix = "dubbo.protocol")
public class DubboProtocol {
    /**
     * 接口协议
     */
    private String name = "dubbo";

    /**
     * 暴露服务的端口
     */
    private int port = 20880;

    /**
     * 线程池大小 默认200
     */
    private int threads = 200;

    /**
     * 请求及响应数据包大小限制，单位：字节
     */
    private int payload = DubboConstant.PAYLOAD;

    /**
     * 是否记录接口日志
     */
    private boolean accessLog = true;

}
