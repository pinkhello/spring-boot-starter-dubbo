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

import com.alibaba.dubbo.common.json.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口日志类
 *
 * @author   喝咖啡的囊地鼠
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceLog implements Serializable {
    /**
     * 调用时间
     */
    private String invokeTime = String.valueOf(System.currentTimeMillis());
    /**
     * 调用方法名
     */
    private String methodName;
    /**
     * 来源方
     */
    private String senderName;
    /**
     * 来源地址
     */
    private String senderHost;
    /**
     * 接收方
     */
    private String receiverName;

    /**
     * 接收地址
     */
    private String receiverHost;
    /**
     * 服务组
     */
    private String srvGroup;
    /**
     * 服务版本
     */
    private String version;
    /**
     * 参数类型
     */
    private String[] paramTypes;
    /**
     * 参数值
     */
    private Object[] paramValues;
    /**
     * 返回
     */
    private Object resultValue;
    /**
     * 接口服务调用花费时间
     */
    private long costTime = 0L;
    /**
     * 异常信息
     */
    private String exceptionMsg;
    /**
     * 异常类信息
     */
    private String throwableClass;
    /**
     * 异常栈信息
     */
    private String stackTraces;

    @Override
    public String toString() {
        String slfStr;
        try {
            slfStr = JSON.json(this);
        } catch (Exception e) {
            slfStr = toSimpleString();
        }
        return slfStr;
    }

    public String toSimpleString() {
        return String.format("{\"invokeTime\": \"%s\" , \"methodName\": \"%s\", \"senderName\": \"%s\", \"senderHost\": \"%s\", \"receiverName\": \"%s\", \"receiverHost\": \"%s\", \"srvGroup\": \"%s\", \"version\": \"%s\", \"paramTypes\": [], \"paramValues\": [], \"resultValue\": {}, \"costTime\": %s, \"throwableClass\": \"%s\", \"exceptionMsg\": \"%s\", \"stackTraces\": \"%s\"}", this.invokeTime, this.methodName, this.senderName, this.senderHost, this.receiverName, this.receiverHost, this.srvGroup, this.version, this.costTime, this.throwableClass, this.exceptionMsg, this.stackTraces);
    }

}
