package io.github.xiaoyureed.session;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 18:35
 * @Description: session repository
 */
public interface SessionRepository<S extends Session> {

    /**
     * get session or create session
     * @param request
     * @param create create == true, create a session if request doesn't contain one; create == false, return null if request...
     * @return
     */
    S getSession(HttpServletRequest request, boolean create);

    /**
     * get session , create == true
     * @param request
     * @return
     */
    S getSession(HttpServletRequest request);

    /**
     * save session
     * @param session
     */
    void saveSession(Session session);

    /**
     * invalidate session by id
     * @param id
     */
    void invalidate(String id);
}
