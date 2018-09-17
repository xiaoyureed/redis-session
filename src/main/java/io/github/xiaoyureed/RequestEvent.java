package io.github.xiaoyureed;

import io.github.xiaoyureed.session.SessionRepository;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Observable;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 19:39
 * @Description: request event, it contains everything we need during a request procedure
 */
@Data
public class RequestEvent extends Observable {

    /**
     * session repository
     */
    private SessionRepository sessionRepository;

    private HttpServletRequest request;

    private HttpServletResponse response;

    /**
     * observer design pattern offered by jdk.
     * change state & notify observers
     */
    public void commit() {
        this.setChanged();
        this.notifyObservers();
    }

}
