package io.github.xiaoyureed.wrapper;

import io.github.xiaoyureed.HttpCookie;
import io.github.xiaoyureed.session.Session;
import io.github.xiaoyureed.session.SessionGlobalConfig;
import io.github.xiaoyureed.session.SessionRepository;
import io.github.xiaoyureed.util.StringUtils;
import lombok.Data;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Observer;
import java.util.Set;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 17:01
 * @Description:
 */
@Data
public class HttpSessionWrapper implements HttpSession,Observer {

    private SessionRepository sessionRepository;

    private Session session;

    private HttpServletRequest req;

    private SessionGlobalConfig sessionGlobalConfig = SessionGlobalConfig.me();

    public long getCreationTime() {
        return this.session.getCreateTime();
    }

    /**
     * not session global key
     * @return
     */
    public String getId() {
        if (null == session) {
            reCreateSession();
        }
        return session.getId();
    }

    public long getLastAccessedTime() {
        if (null == session) {
            reCreateSession();
        }
        return session.getLastAccessTime();
    }

    public ServletContext getServletContext() {
        if (null != req) {
            return req.getSession().getServletContext();
        }
        return null;
    }

    public void setMaxInactiveInterval(int interval) {
        // todo
    }

    public int getMaxInactiveInterval() {
        return 0;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getAttribute(String name) {
        if (null == session) {
            reCreateSession();
        }
        return session.getAttribute(name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        if (session == null) {
            reCreateSession();
        }
        Set<String> keySet = session.getAttributeKeySet();
        return Collections.enumeration(keySet);
    }

    public String[] getValueNames() {
        if (session == null) {
            reCreateSession();
        }
        return session.getAttributeKeySet().toArray(new String[0]);
    }

    public void setAttribute(String name, Object value) {
        if (session == null) {
            reCreateSession();
        }
        session.setAttribute(name, value);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        if (session == null) {
            reCreateSession();
        }
        session.removeAttribute(name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        if (session == null) {
            return;
        }
        sessionRepository.invalidate(session.getId());
    }

    public boolean isNew() {
        if (session == null) {
            reCreateSession();
        }
        return session.isFirst();
    }

    /**
     *
     * @param o
     * @param arg
     */
    public void update(java.util.Observable o, Object arg) {
        sessionRepository.saveSession(session);
    }

    /**
     * recreate session & setup cookie for the resp
     */
    private void reCreateSession() {
        session = sessionRepository.getSession(req);
    }

    /**
     * write cookie to response
     * @param response
     */
    public void writeCookieToResponse(HttpServletResponse response) {
        String sessionId = this.getId();
        req.setAttribute(sessionGlobalConfig.getGlobalKey(), sessionId);
        HttpCookie cookie = new HttpCookie(sessionGlobalConfig.getGlobalKey(), sessionId);
        String cookiePath = sessionGlobalConfig.getCookiePath();
        if (StringUtils.isNotBlank(cookiePath)) {
            cookie.setPath(cookiePath);
        }
        String cookieDomain = sessionGlobalConfig.getCookieDomain();
        if (StringUtils.isNotBlank(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }
        cookie.setHttpOnly(sessionGlobalConfig.isHttpOnly());

        cookie.writeResponse(response);
    }

}
