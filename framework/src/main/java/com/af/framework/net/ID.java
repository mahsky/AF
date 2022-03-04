package com.af.framework.net;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by mah on 2022/3/3.
 */

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ID {
    /**
     * 请求时携带的id, 和 http 无关，只是请求结束后回调回去，方便统计分析错误。
     */
    String value() default "";
}
