package io.github.xiaoyureed.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/11 22:01
 * @Description: encode filter
 */
public class EncodeFilter implements Filter {

    private String encode;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(encode);
        resp.setCharacterEncoding(encode);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.encode = config.getInitParameter("encode");
    }

}
