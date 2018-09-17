package io.github.xiaoyureed.session;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 18:20
 * @Description: custom session, 大部分属性在 HttpSessionWrapper 中用到了
 */
@Data
public class Session {

    /**
     * session id (not global id)
     */
    private String id;

    private Map<String, Object> attributeMap = new HashMap<String, Object>(8);

    private long createTime;

    private long lastAccessTime;

    private boolean first;

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    public Set<String> getAttributeKeySet() {
        return attributeMap.keySet();
    }

    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }
}
