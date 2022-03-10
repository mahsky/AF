package com.af.framework.net;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by mah on 2022/3/9.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface CropEnvelope {
}