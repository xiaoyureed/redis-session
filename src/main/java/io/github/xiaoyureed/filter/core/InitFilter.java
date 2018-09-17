package io.github.xiaoyureed.filter.core;

import io.github.xiaoyureed.RedisSessionConfig;
import io.github.xiaoyureed.RequestEvent;
import io.github.xiaoyureed.redis.RedisDataBase;
import io.github.xiaoyureed.redis.RedisSessionRepository;
import io.github.xiaoyureed.session.SessionGlobalConfig;
import io.github.xiaoyureed.session.SessionRepository;
import io.github.xiaoyureed.util.PropertiesUtils;
import io.github.xiaoyureed.util.StringUtils;
import io.github.xiaoyureed.wrapper.RedisSessionRequestWrapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 20:10
 * @Description: init filter for whole system. replace ServletRequest with my own request wrapper
 */
public class InitFilter implements Filter {

    /**
     * request(its uri end up with string inside IGNORE_SUFFIX) will not be filtered
     */
    private static final Set<String> IGNORE_SUFFIX = new HashSet<String>();

    private static final String CONFIG_PATH_PARAM_NAME = "redisSessionPath";

    private static final Logger LOGGER = LoggerFactory.getLogger(InitFilter.class);

    private SessionRepository sessionRepository;

    static {
        IGNORE_SUFFIX.addAll(Arrays.asList("gif,jpg,jpeg,png,bmp,swf,js,css,html,htm".split(",")));
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // don't filter
        if ((request instanceof RedisSessionRequestWrapper)
                || !shouldFilter(req)) {
                chain.doFilter(req, resp);
                return;
        }

        // filter
        RequestEvent event = new RequestEvent();
        event.setSessionRepository(sessionRepository);
        event.setRequest(req);
        event.setResponse(resp);


        try {
            chain.doFilter(new RedisSessionRequestWrapper(event), response);
        } finally {
            // save session.
            event.commit();

        }
    }

    /**
     * should I filter this request ?
     * 如果 uri 在 IGNORE_SUFFIX 中出现， 那么不过滤
     * @param req
     * @return
     */
    private boolean shouldFilter(HttpServletRequest req) {
        String uri = req.getRequestURI().toLowerCase();
        int index = uri.lastIndexOf(".");
        if (index > 0) {
            String suffix = uri.substring(index + 1);
            if (IGNORE_SUFFIX.contains(suffix)
                    && suffix.length() < 8) {
                return false;
            }
        }
        return true;
    }

    /**
     * initialize sessionRepository;
     * setup redis, sessio
     * @param fc
     * @throws ServletException
     */
    public void init(FilterConfig fc) throws ServletException {
        if (null == sessionRepository) {

            String redisSessionPath = fc.getInitParameter(CONFIG_PATH_PARAM_NAME);
            if (StringUtils.isNotBlank(redisSessionPath)) {
                RedisSessionConfig config = PropertiesUtils.load(redisSessionPath);
                LOGGER.info(">>> load config success, file: [{}]", redisSessionPath);

                // properties about redis

                String redisHost = config.getString("redis.host", "127.0.0.1");
                int redisPort = config.getInt("redis.port", 6379);
                int redisTimeout = config.getInt("redis.timeout", 60); // unit: ms
                String redisPass = config.getString("redis.pass", "");


                JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                jedisPoolConfig.setMaxIdle(config.getInt("redis.max_idle", GenericObjectPoolConfig.DEFAULT_MAX_IDLE));
                jedisPoolConfig.setMinIdle(config.getInt("redis.min_idle", GenericObjectPoolConfig.DEFAULT_MIN_IDLE));
                jedisPoolConfig.setMaxTotal(config.getInt("redis.max_total",  GenericObjectPoolConfig.DEFAULT_MAX_TOTAL));
                jedisPoolConfig.setMaxWaitMillis(config.getLong("redis.max_wait_millis", GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS));

                // properties about session

                String globalKey = config.getString("session.global_key", "jsessionid");
                int sessionTimeout = config.getInt("session.timeout", 30);
                String cookieDomain = config.getString("session.cookie_domain", "");
                String cookiePath = config.getString("session.cookie_path", "/");
                Boolean httpOnly = Boolean.valueOf(config.getString("session.http_only", "true"));

                SessionGlobalConfig sessionGlobalConfig = SessionGlobalConfig.me();
                sessionGlobalConfig.setGlobalKey(globalKey);
                sessionGlobalConfig.setTimeout(sessionTimeout);
                if (StringUtils.isNotBlank(cookieDomain)) {
                    sessionGlobalConfig.setCookieDomain(cookieDomain);
                }
                sessionGlobalConfig.setCookiePath(cookiePath);
                sessionGlobalConfig.setHttpOnly(httpOnly);

                JedisPool pool = null;
                if (redisPass.length() > 0) {
                    pool = new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeout, redisPass);
                } else {
                    pool = new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeout);
                }

                this.sessionRepository = new RedisSessionRepository(new RedisDataBase(pool));
            } else {
                // todo
                LOGGER.info(">>> config file path is invalid, use default config instead");
            }
        }
    }

}
