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
package org.springframework.boot.dubbo.annotation;

import org.springframework.boot.dubbo.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.dubbo.autoconfigure.DubboConsumerAutoConfiguration;
import org.springframework.boot.dubbo.autoconfigure.DubboProviderAutoConfiguration;
import org.springframework.boot.dubbo.autoconfigure.DubboRegistryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable* 替代 spring.factories
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        DubboAutoConfiguration.class,
        DubboRegistryAutoConfiguration.class,
        DubboProviderAutoConfiguration.class,
        DubboConsumerAutoConfiguration.class})
public @interface EnableDubbo {
}
