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

import com.alibaba.dubbo.rpc.Invoker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.dubbo.common.DubboConstant;

import java.io.Serializable;

/**
 * 标注服务接口唯一性
 *
 * @author   喝咖啡的囊地鼠
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceInfo implements Serializable {
    private Class<?> clazz;
    private String group;
    private String version;
    public InterfaceInfo(Invoker<?> invoker){
        this.clazz = invoker.getInterface();
        this.group = invoker.getUrl().getParameter(DubboConstant.GROUP);
        this.version = invoker.getUrl().getParameter(DubboConstant.VERSION);
    }
}
