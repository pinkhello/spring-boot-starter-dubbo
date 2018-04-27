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
package org.springframework.boot.dubbo.common;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.common.utils.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author   喝咖啡的囊地鼠
 *
 */
public final class SubUtil {
    private static final String DEFAULT_SUFFIX = "......content truncated! real length is";

    public SubUtil() {
    }

    public static void subJsonArray(JSONArray jsonArray) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObj = jsonArray.getObject(i);
                subJson(jsonObj);
            }
        }
    }

    public static Object[] subJsonArrayWithArray(String json) throws ParseException {
        Object obj = JSON.parse(json);
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            Object[] objects = new Object[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); ++i) {
                Object valueObj = jsonArray.get(i);
                if (valueObj instanceof JSONObject) {
                    objects[i] = subJsonWithMap((JSONObject) valueObj);
                } else if (valueObj instanceof JSONArray) {
                    objects[i] = subJsonArrayWithList((JSONArray) valueObj);
                } else {
                    objects[i] = valueObj;
                }
            }

            return objects;
        } else {
            return obj instanceof JSONObject ? new Object[]{subJsonWithMap((JSONObject) obj)} : new Object[]{obj};
        }
    }

    public static Object subJsonWithObject(String json) throws ParseException {
        Object obj = JSON.parse(json);
        if (obj instanceof JSONObject) {
            return subJsonWithMap((JSONObject) obj);
        } else {
            return obj instanceof JSONArray ? subJsonArrayWithList((JSONArray) obj) : Collections.emptyMap();
        }
    }

    public static List<Object> subJsonArrayWithList(JSONArray jsonArray) {
        List<Object> resultList = new ArrayList();
        if (jsonArray != null && jsonArray.length() > 0) {
            Object obj = jsonArray.get(0);
            if (obj instanceof JSONObject) {
                resultList.add(subJsonWithMap((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                resultList.add(subJsonArrayWithList((JSONArray) obj));
            } else {
                resultList.add(obj);
            }

            if (jsonArray.length() > 1) {
                resultList.add("*** content truncated! array.length = " + jsonArray.length() + " ***");
            }
        }

        return resultList;
    }

    public static Map<String, Object> subJsonWithMap(JSONObject jsonObject) {
        Map<String, Object> resultMap = new HashMap(0);
        if (jsonObject != null) {
            Iterator jsonKeys = jsonObject.keys();

            while (jsonKeys.hasNext()) {
                String jsonKey = (String) jsonKeys.next();
                Object valueObj = jsonObject.get(jsonKey);
                if (valueObj instanceof JSONObject) {
                    Map<String, Object> subMap = subJsonWithMap((JSONObject) valueObj);
                    resultMap.put(jsonKey, subMap);
                } else if (valueObj instanceof JSONArray) {
                    JSONArray propArrary = (JSONArray) valueObj;
                    resultMap.put(jsonKey, subJsonArrayWithList(propArrary));
                } else {
                    resultMap.put(jsonKey, valueObj);
                }
            }
        }

        return resultMap;
    }

    public static void subJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            Iterator jsonKeys = jsonObject.keys();

            while (jsonKeys.hasNext()) {
                String jsonKey = (String) jsonKeys.next();
                JSONArray propArray = jsonObject.getArray(jsonKey);
                String propStr = jsonObject.getString(jsonKey);
                JSONObject propObject = jsonObject.getObject(jsonKey);
                if (StringUtils.isNotEmpty(propStr) && propStr.length() > 512) {
                    jsonObject.put(jsonKey, subStr(propStr, 512));
                }

                if (propObject != null) {
                    subJson(propObject);
                }

                if (propArray != null && propArray.length() > 1) {
                    JSONArray newPropArr = new JSONArray();
                    newPropArr.add(propArray.get(0));
                    newPropArr.add("*** content truncated! array.length = " + propArray.length() + " ***");
                    jsonObject.put(jsonKey, newPropArr);
                }
            }

        }
    }

    public static JSONArray subJsonArray(String json) throws ParseException {
        Object obj = JSON.parse(json);
        if (obj instanceof JSONArray) {
            JSONArray jsonArr = (JSONArray) obj;
            subJsonArray(jsonArr);
            return jsonArr;
        } else {
            return null;
        }
    }

    public static JSONObject subJsonObject(String json) throws ParseException {
        Object obj = JSON.parse(json);
        if (obj instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) obj;
            subJson(jsonObj);
            return jsonObj;
        } else {
            return null;
        }
    }

    public static String subJson(String json) throws IOException, ParseException {
        Object obj = JSON.parse(json);
        String result = json;
        if (obj instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) obj;
            subJson(jsonObj);
            result = JSON.json(jsonObj);
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArr = (JSONArray) obj;
            subJsonArray(jsonArr);
            result = JSON.json(jsonArr);
        }

        return result;
    }

    public static String subStr(String orginStr, int maxLength) {
        String result = orginStr == null ? "" : orginStr;
        int len = result.length();
        if (len > maxLength) {
            maxLength -= ("......content truncated! real length is" + len).length();
            result = result.substring(0, maxLength) + "......content truncated! real length is" + len;
        }

        return result;
    }
}
