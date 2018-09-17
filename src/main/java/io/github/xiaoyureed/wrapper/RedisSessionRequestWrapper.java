package io.github.xiaoyureed.wrapper;

import io.github.xiaoyureed.RequestEvent;
import io.github.xiaoyureed.session.Session;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 20:57
 * @Description: reimplement getSession(), get session from redis instead of the request
 */
public class RedisSessionRequestWrapper extends HttpServletRequestWrapper {

    private RequestEvent requestEvent;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param event request event, 容纳了 SessionRepository， HttpServletRequest 等等
     * @throws IllegalArgumentException if the request is null
     */
    public RedisSessionRequestWrapper(RequestEvent event) {
        super(event.getRequest());
        this.requestEvent = event;
    }

    @Override
    public HttpSession getSession(boolean create) {
        //return super.getSession(create);
        Session session = this.requestEvent.getSessionRepository().getSession(requestEvent.getRequest());

        if (session == null) {
            return null;
        }

        HttpSessionWrapper sessionWrapper = new HttpSessionWrapper();
        sessionWrapper.setSession(session);
        sessionWrapper.setReq(requestEvent.getRequest());
        sessionWrapper.setSessionRepository(this.requestEvent.getSessionRepository());

        requestEvent.addObserver(sessionWrapper);

        sessionWrapper.writeCookieToResponse(requestEvent.getResponse());

        return sessionWrapper;
    }
}
