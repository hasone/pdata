package com.cmcc.vrp.boss.shyc.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 签名时使用的别名, 默认情况下为属性的名称
 *
 * Created by sunyiwei on 2017/3/14.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
public @interface SignAlias {
    String value() default "";
}
