package io.github.xiaoyureed.session;

import lombok.Data;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 11:27
 * @Description: session session 的全局设置， 设计为 singleton， 不会被 GC 回收， 初始化后用的时候可以直接使用 （http://www.importnew.com/15527.html）
 */
@Data
public class SessionGlobalConfig {

    /**
     * global key
     */
    private String globalKey;

    /**
     * default expire time (unit: min)
     */
    private int timeout = 30;
    public int getTimeout() {
        return this.timeout * 60;
    }

    /*---------------following fields are all about cookie----------------------*/

    private String cookieDomain;
    private String cookiePath;
    private boolean httpOnly;

    /* --------singleton------------ */

    private SessionGlobalConfig() {
    }

    private static class Holder {
        private static final SessionGlobalConfig INSTANCE = new SessionGlobalConfig();
    }

    public static SessionGlobalConfig me() {
        return Holder.INSTANCE;
    }

}
