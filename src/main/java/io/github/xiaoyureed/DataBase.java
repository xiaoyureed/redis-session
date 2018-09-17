package io.github.xiaoyureed;

import io.github.xiaoyureed.session.Session;

import java.util.List;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 16:06
 * @Description: database interface
 */
public interface DataBase {

    List<Session> sessions();

    Session get(String key);

    void set(String key, Object value);

    void set(String key, Object value, int expires);

    /**
     * test specific item whether exist
     * @param key
     * @return
     */
    boolean exist(String key);

    /**
     * setup expire time
     * @param key
     * @param timeout
     * @return 1: setup ok; 0: fail
     */
    long expire(String key, int timeout);

    /**
     * delete item by key
     * @param key
     */
    void del(String key);
}
