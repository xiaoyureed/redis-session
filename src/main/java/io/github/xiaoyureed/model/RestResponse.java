package io.github.xiaoyureed.model;

import lombok.Data;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/11 19:37
 * @Description: response for restful request
 */
@Data
public class RestResponse<T> {

    private String code;
    private String msg;
    private T payload;

}
