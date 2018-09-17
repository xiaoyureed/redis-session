package io.github.xiaoyureed.redis;

import io.github.xiaoyureed.session.Session;
import io.github.xiaoyureed.session.SessionGlobalConfig;
import io.github.xiaoyureed.session.SessionRepository;
import io.github.xiaoyureed.util.UUIDUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 21:43
 * @Description: SessionRepository 的 redis 实现
 */
public class RedisSessionRepository implements SessionRepository<Session> {
    
    private RedisDataBase redisDataBase;

    private SessionGlobalConfig sessionGlobalConfig = SessionGlobalConfig.me();
    
    public  RedisSessionRepository(RedisDataBase redisDataBase) {
        this.redisDataBase = redisDataBase;
    }

    public Session getSession(HttpServletRequest request, boolean create) {

        Session session = null;
        String jsessionid = getSessionIdByRequest(request);

        if (jsessionid == null) {
            if (create) {
                // create new session
                session = createSession();
            }
        }
        else {
            if (redisDataBase.exist(jsessionid)) {
                // get session & session 续期
                session = redisDataBase.get(jsessionid);
                session.setFirst(false);
                redisDataBase.expire(jsessionid, sessionGlobalConfig.getTimeout());
            }
            else {
                // jsessionid 不为空, redis 中没有
                if (create) {
                    session = createSession();
                }
            }
        }
        session.setLastAccessTime(System.currentTimeMillis());
        return session;
    }

    /**
     * create a new session
     * @return
     */
    private Session createSession() {
        Session session = new Session();
        session.setId(UUIDUtils.UU32());
        session.setCreateTime(System.currentTimeMillis());
        session.setFirst(true);
        return  session;
    }

    private String getSessionIdByRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie c: cookies) {
            if (sessionGlobalConfig.getGlobalKey().equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    public Session getSession(HttpServletRequest request) {
        return this.getSession(request, true);
    }

    public void invalidate(String id) {
        redisDataBase.del(id);
    }

    public void saveSession(Session session) {
        if (session == null) return;
        redisDataBase.set(session.getId(), session, sessionGlobalConfig.getTimeout());
    }
}
