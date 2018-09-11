package io.github.xiaoyureed.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * login filter
 */
public class LoginFilter implements Filter {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    }

    /**
     * login filter
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String reqUrl = req.getRequestURL().toString();
        String reqUri = req.getRequestURI();
        String jspUri = reqUrl.substring(0, reqUrl.lastIndexOf(reqUri));
        // eg: /login
        req.setAttribute("uri", jspUri.endsWith("/") ?
                jspUri.substring(0, jspUri.length() - 1) : jspUri);

        HttpSession session = req.getSession(false);
        // already login, let her go
        if (session != null
                && session.getAttribute("name") != null) {
            chain.doFilter(req, resp);
        }
        // comes from "login" page, let her go
        else if (reqUri.contains("login")) {
            chain.doFilter(req, resp);
        }
        // haven't login yet, forward to "login" page
        else {
            LOGGER.debug(">>>>>> filter success, request url: [{}]", reqUrl);
            forwardToLogin(req, resp);
        }

    }

    private void forwardToLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    public void destroy() {

    }

    /**
     * initialization
     * @param config filter设置
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {
    }

}
