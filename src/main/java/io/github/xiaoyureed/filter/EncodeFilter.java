package io.github.xiaoyureed.filter;

import io.github.xiaoyureed.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/11 22:01
 * @Description: encode filter
 */
public class EncodeFilter implements Filter {

    private String encode;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //req.setCharacterEncoding(encode);
        //resp.setCharacterEncoding(encode);
        //chain.doFilter(req, resp);

        // 转型，
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding(encode);
        resp.setContentType("text/html;charset=" + encode);

        HttpServletRequest proxyReq = (HttpServletRequest) Proxy.newProxyInstance(
                req.getClass().getClassLoader(),
                req.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        // 当时GET提交方式时，出现中文乱码，是因为在request.getParameter方法内部没有进行提交方式判断and处理
                        if ("getParameter".equals(method.getName())) {

                            String paramToConvert = req.getParameter(args[0].toString());

                            if ("GET".equals(req.getMethod())
                                    && StringUtils.isNotBlank(paramToConvert)) {
                                return new String(paramToConvert.getBytes("ISO8859-1"), "UTF-8");
                            }
                        }
                        return method.invoke(req, args);
                    }
                }
        );

        chain.doFilter(proxyReq, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.encode = config.getInitParameter("encode");
    }

}
