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

import com.alibaba.dubbo.rpc.*;
import org.springframework.boot.dubbo.bo.InterfaceInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author   喝咖啡的囊地鼠
 *
 */
public abstract class AbstractStatisticsFilter implements Filter {


    public static final Map<InterfaceInfo, Map<String, AtomicLong>> INTERFACE_STATISTICS = new ConcurrentHashMap<>();

    private static final long ZERO = 0L;

    /**
     * 获取invoker
     * @param invoker
     * @param invocation
     * @return
     */
    public abstract Invoker<?> getInvoker(Invoker<?> invoker, Invocation invocation);

    /**
     * invoker 调用
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        incrementAndGet(buildInterfaceInfo(getInvoker(invoker,invocation)), invocation.getMethodName());
        return invoker.invoke(invocation);
    }


    /**
     * 获取统计信息
     * @param info
     * @param methodName
     * @return
     */
    public static long get(InterfaceInfo info, String methodName) {
        if (!INTERFACE_STATISTICS.containsKey(info)){
            return ZERO;
        }
        AtomicLong ct = INTERFACE_STATISTICS.get(info).get(methodName);
        return ct == null ? ZERO : ct.get();
    }

    /**
     * 建立接口信息
     * @param invoker
     * @return
     */
    private InterfaceInfo buildInterfaceInfo(Invoker<?> invoker){
        return new InterfaceInfo(invoker);
    }

    /**
     * 计数
     * @param info
     * @param methodName
     */
    private synchronized static void incrementAndGet(InterfaceInfo info, String methodName) {
        if (!INTERFACE_STATISTICS.containsKey(info)){
            INTERFACE_STATISTICS.put(info,new ConcurrentHashMap<>(16));
        }
        AtomicLong ct = INTERFACE_STATISTICS.get(info).get(methodName);
        if (ct==null){
            ct = new AtomicLong(ZERO);
            INTERFACE_STATISTICS.get(info).put(methodName,ct);
        }
        ct.incrementAndGet();
    }
}
