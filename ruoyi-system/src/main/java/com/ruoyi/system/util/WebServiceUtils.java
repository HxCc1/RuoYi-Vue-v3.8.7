package com.ruoyi.system.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WebServiceUtils {
    private static final Logger log = LoggerFactory.getLogger(WebServiceUtils.class);

    public static Map<String, String> getBOMData(Long materialId) {
        String url = "http://10.20.0.8/ws/index.php/Home/Index/getSAPInfo";
        Map<String, Long> params = new HashMap<>();
        params.put("materialId", materialId);
        String result = callByParamStr(url, "getSingleBom", params);

        if (StringUtils.isEmpty(result)) {
            log.warn("Web服务调用返回空结果，materialId: " + materialId);
            return null;
        } else {
            try {
                // 移除JSON值中的空格
                String cleanedJsonResult = removeSpacesFromJsonValues(result);
                log.debug("处理后的JSON: " + cleanedJsonResult);
                
                // 关键修改：使用JSONArray解析数组
                JSONArray array = JSONArray.parseArray(cleanedJsonResult);

                // 转换为Map（假设需要以component为键）
                Map<String, String> resultMap = new HashMap<>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    String component = item.getString("component");
                    resultMap.put(component, item.toJSONString());
                    // 或提取特定字段：resultMap.put(component, item.getString("comp_qty"));
                }
                log.info(url + " - " + "getSingleBom" + " -- 解析成功，返回结果: " + resultMap);
                return resultMap;
            } catch (Exception e) {
                log.error("解析Web服务返回结果失败，result: " + result, e);
                return null;
            }
        }
    }

    public static String callByParamStr(String endpoint, String func, Map<String, Long> params) {
        String result = "";
        if (params.isEmpty()) {
            log.error(endpoint + " - " + func + " - 参数为空!");
            return null;
        }
        log.info(endpoint + " - " + func + " -- 参数:" + params);

        Service service = new Service();
        try {
            log.info("进入try...catch...");
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);
            call.setOperationName(func); // 设置方法名
            call.setReturnType(XMLType.XSD_STRING); // 返回类型

            // 添加参数，注意这里需要将Long类型转换为String类型
            for (Map.Entry<String, Long> entry : params.entrySet()) {
                call.addParameter(entry.getKey(), XMLType.XSD_STRING, ParameterMode.IN);
            }

            // 执行Web服务调用
            Object[] args = params.values().toArray();
            result = (String) call.invoke(args);
            log.info(endpoint + " - " + func + " -- 调用成功，返回结果: " + result);
        } catch (Exception e) {
            log.error(endpoint + " - " + func + " -- 调用失败", e);
        }
        return result;
    }

    public static String callByParamObject(String endpoint, String func, List<Map<String, Object>> list) {
        String result = "";
        if (CollectionUtils.isEmpty(list)) {
            log.error(endpoint + " - " + func + " - param is empty!");
            return null;
        }
        log.info(endpoint + " - " + func + " -- param:" + JSONObject.toJSONString(list));
        Service service = new Service();
        Object[] parameter = new Object[list.size()];
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);
            call.setOperationName(func); //设置方法名
            call.setReturnType(XMLType.XSD_STRING); //返回类型
            for (int i = 0; i < list.size(); i++) {
                call.addParameter(list.get(i).get("key").toString(), XMLType.XSD_STRING, ParameterMode.IN); //接口的参数
                parameter[i] = list.get(i).get("value");
            }
            Object resultObject = call.invoke(parameter); //调用接口
            log.info(endpoint + " - " + func + " -- result:" + resultObject);
            result = StringEscapeUtils.unescapeJava(String.valueOf(resultObject)); // 转化为汉字
            if ("nodata".equals(result)) {
                result = "";
            }
            return result;
        } catch (ServiceException e) {
            log.error(endpoint + " - " + func + " - ServiceException exception!", e);
        } catch (RemoteException e) {
            log.error(endpoint + " - " + func + " - RemoteException exception!", e);
        }
        return null;
    }

    /**
     * 移除JSON字符串中所有值的空格字符
     * @param jsonStr 原始JSON字符串
     * @return 处理后的JSON字符串
     */
    public static String removeSpacesFromJsonValues(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            log.warn("输入JSON字符串为空");
            return jsonStr;
        }

        try {
            // 解析JSON
            Object jsonObj = JSON.parse(jsonStr);

            // 处理JSON对象或数组
            if (jsonObj instanceof JSONObject) {
                JSONObject processedObj = processJSONObject((JSONObject) jsonObj);
                return processedObj.toJSONString();
            } else if (jsonObj instanceof JSONArray) {
                JSONArray processedArray = processJSONArray((JSONArray) jsonObj);
                return processedArray.toJSONString();
            } else {
                log.warn("不支持的JSON类型: " + jsonObj.getClass().getName());
                return jsonStr;
            }
        } catch (Exception e) {
            log.error("处理JSON空格时发生异常", e);
            return jsonStr;
        }
    }

    /**
     * 处理JSONObject，移除所有字符串值的空格
     */
    private static JSONObject processJSONObject(JSONObject obj) {
        JSONObject result = new JSONObject();
        Iterator<String> keys = obj.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = obj.get(key);

            if (value instanceof String) {
                // 移除字符串中的所有空格
                String strValue = (String) value;
                result.put(key, strValue.replaceAll("\\s+", ""));
            } else if (value instanceof JSONObject) {
                // 递归处理嵌套对象
                result.put(key, processJSONObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                // 处理数组
                result.put(key, processJSONArray((JSONArray) value));
            } else {
                // 其他类型直接添加
                result.put(key, value);
            }
        }

        return result;
    }

    /**
     * 处理JSONArray，移除所有字符串值的空格
     */
    private static JSONArray processJSONArray(JSONArray array) {
        JSONArray result = new JSONArray();

        for (int i = 0; i < array.size(); i++) {
            Object item = array.get(i);

            if (item instanceof String) {
                // 移除字符串中的所有空格
                String strItem = (String) item;
                result.add(strItem.replaceAll("\\s+", ""));
            } else if (item instanceof JSONObject) {
                // 递归处理嵌套对象
                result.add(processJSONObject((JSONObject) item));
            } else if (item instanceof JSONArray) {
                // 递归处理嵌套数组
                result.add(processJSONArray((JSONArray) item));
            } else {
                // 其他类型直接添加
                result.add(item);
            }
        }

        return result;
    }

}
