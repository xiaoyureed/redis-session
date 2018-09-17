package io.github.xiaoyureed;

import io.github.xiaoyureed.util.StringUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 18:16
 * @Description: redis session config
 */
public class RedisSessionConfig {

    private Map<String, String> configMap;

    public RedisSessionConfig(Properties props) {
        configMap = new HashMap<String, String>(32);
        Enumeration<?> propertyNames = props.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            String value = props.getProperty(key);
            configMap.put(key, value);
        }
    }

    public String getString(String key) {
        return this.configMap.get(key);
    }

    public String getString(String key, String defaultValue) {
        String value = this.getString(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Integer getInt(String key) {
        String str = this.getString(key);
        if (StringUtils.isNumeric(str)) {
            return Integer.parseInt(str);
        }
        return null;
    }

    public int getInt(String key, int defaultValue) {
        Integer value = getInt(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Long getLong(String key) {
        String value = this.getString(key);
        if (StringUtils.isNumeric(value)) {
            return Long.parseLong(value);
        }
        return null;
    }

    public long getLong(String key, long defaultValue) {
        Long value = getLong(key);
        if (value == null) {
            return defaultValue;
        }
        return  value;
    }
}
