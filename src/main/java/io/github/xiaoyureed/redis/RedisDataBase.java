package io.github.xiaoyureed.redis;

import io.github.xiaoyureed.DataBase;
import io.github.xiaoyureed.session.Session;
import io.github.xiaoyureed.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 22:14
 * @Description: redis implement of DataBase
 */
public class RedisDataBase implements DataBase {

    Logger LOGGER = LoggerFactory.getLogger(RedisDataBase.class);

    private JedisPool pool;

    public  RedisDataBase(JedisPool pool) {
        if (pool == null) {
            LOGGER.error(">>> Construct RedisDataBase fail, JedisPool is null.");
        }
        this.pool = pool;
    }

    public List<Session> sessions() {
        return null;
    }

    public Session get(String key) {
        Jedis jedis = borrow();
        String value = jedis.get(key);
        revert(jedis);

        if (value != null) {
            return JsonUtils.fromJson(value, Session.class);
        }

        return null;
    }

    /**
     * get jedis
     * @return
     */
    private Jedis borrow() {
        return this.pool.getResource();
    }

    /**
     * revert jedis
     * @param jedis
     */
    private void revert(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void del(String key) {
        Jedis jedis = borrow();
        jedis.del(key);
        revert(jedis);
    }

    /**
     * save
     * @param key
     * @param value
     * @param expire expire time, 若为负数, 则不设置; 单位：s
     */
    public void set(String key, Object value, int expire) {
        Jedis jedis = borrow();
        jedis.set(key, JsonUtils.toJson(value));
        if (expire > 0) {
            jedis.expire(key, expire);
        }
        revert(jedis);
    }

    public boolean exist(String key) {
        Jedis jedis = borrow();
        boolean exists = jedis.exists(key);
        revert(jedis);
        return exists;
    }

    public long expire(String key, int timeout) {
        Jedis jedis = borrow();
        long success = jedis.expire(key, timeout);
        revert(jedis);
        return  success;
    }

    public void set(String key, Object value) {
        this.set(key, value, -1);
    }



}
