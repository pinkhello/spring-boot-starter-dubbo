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
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.dubbo.annotation.ExcludeDubboLog;
import org.springframework.boot.dubbo.bo.InterfaceLog;
import org.springframework.boot.dubbo.common.SubUtil;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 服务提供方提供的接口日志输出
 *
 * @author   喝咖啡的囊地鼠
 *
 */
public class InterfaceLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(InterfaceLogFilter.class);
    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\r\n");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        StopWatch sw = new StopWatch();
        Throwable exception = null;
        Result result = null;
        try {
            sw.start();
            result = invoker.invoke(invocation);
            if (result != null) {
                exception = result.getException();
            }
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            sw.stop();
            boolean print = false;
            // todo 还可以再优化,缓存已经反射过的方法是否需要过滤

            if (!invoker.getInterface().isAnnotationPresent(ExcludeDubboLog.class)){
                try {
                    //只需要公开的方法并且接口中声明的，否则暴露不出去
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(),invocation.getParameterTypes());
                    method.setAccessible(true);
                    if (!method.isAnnotationPresent(ExcludeDubboLog.class)){
                        print = true;
                    }
                }
                catch (Exception e) {
                    exception = e;
                    print = true;
                }
            }

            if (print){
                InterfaceLog interfaceLog = buildInterfaceLog(invoker,invocation);
                interfaceLog.setCostTime(sw.getLastTaskTimeMillis());
                if (result != null) {
                    try {
                        interfaceLog.setResultValue(SubUtil.subJson(OBJECT_MAPPER.writeValueAsString(result.getValue())));
                    } catch (Exception e) {
                        interfaceLog.setResultValue("result value can't to json, " + result);
                    }
                }

                if (exception != null) {
                    StringBuilder sb = new StringBuilder();
                    Arrays.stream(exception.getStackTrace())
                            .forEach(ste -> sb.append(
                                    ste.getClassName()).append(".")
                                    .append(ste.getMethodName()).append(":")
                                    .append(ste.getLineNumber())
                                    .append(LINE_SEPARATOR));
                    interfaceLog.setStackTraces(sb.toString());
                    interfaceLog.setThrowableClass(exception.getClass().getName());
                    interfaceLog.setExceptionMsg(exception.getMessage());
                    log.error(Markers.appendFields(interfaceLog), interfaceLog.toString());
                } else {
                    log.info(Markers.appendFields(interfaceLog), interfaceLog.toString());
                }
            }
        }
        return result;
    }



    private InterfaceLog buildInterfaceLog(Invoker<?> invoker, Invocation invocation){
        InterfaceLog interfaceLog= new InterfaceLog();
        interfaceLog.setMethodName(invoker.getInterface().getName() + "." + invocation.getMethodName());
        interfaceLog.setSenderName(RpcContext.getContext().getAttachment("SENDER_APP_NAME"));
        interfaceLog.setReceiverName(invoker.getUrl().getParameter("application"));
        interfaceLog.setSrvGroup(invoker.getUrl().getParameter("group"));
        interfaceLog.setVersion(invoker.getUrl().getParameter("version"));

        interfaceLog.setSenderHost(RpcContext.getContext().getRemoteHost() + ":" + RpcContext.getContext().getRemotePort());
        interfaceLog.setReceiverHost(RpcContext.getContext().getLocalHost() + ":" + RpcContext.getContext().getLocalPort());
        Class[] types = invocation.getParameterTypes();
        if (types != null && types.length > 0) {
            interfaceLog.setParamTypes(
                    Arrays.stream(types)
                            .map(Class::getName)
                            .collect(Collectors.toList())
                            .toArray(new String[types.length]));
        }

        interfaceLog.setParamValues(
                Arrays.stream(invocation.getArguments())
                        .map(arg -> {
                            try {
                                return SubUtil.subJson(OBJECT_MAPPER.writeValueAsString(arg));
                            } catch (Exception e) {
                                return "paramValue can't to json, " + arg;
                            }
                        }).collect(Collectors.toList()).toArray());


        return interfaceLog;
    }

}
