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
package org.springframework.boot.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import org.springframework.boot.dubbo.bo.InterfaceInfo;
import org.springframework.boot.dubbo.common.DubboConstant;

/**
 * 消费方消费
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Activate(group = Constants.CONSUMER)
public class ConsumerInvokeStatisticsFilter extends AbstractStatisticsFilter {

    @Override
    public Invoker<?> getInvoker(Invoker<?> invoker, Invocation invocation) {
        return invocation.getInvoker();
    }
}
