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

/**
 * @author 喝咖啡的囊地鼠
 */
@Data
@ConfigurationProperties(prefix = "zipkin.annotation")
public class ZipkinTraceConfig {
    private boolean enabled = true;
    private int connectTimeout;
    private int readTimeout;
    private int flushInterval = 0;
    private boolean compressionEnabled = true;
    private String url;

}
