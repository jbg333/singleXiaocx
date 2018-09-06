package com.weixin.note.serv.pojo.enm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
    /**
     * 执行动作
     * {@link Action}
     */
    Action value() default Action.Normal;
}
