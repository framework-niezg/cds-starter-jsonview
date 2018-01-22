package com.zjcds.common.jsonview.annotations;

import java.lang.annotation.*;

/**
 * 该注解标记在controller类或类中方法上面
 * 表明该控制器抛出的异常包装成json格式返回
 * created date：2016-12-01
 * @author niezhegang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface JsonViewException {

}
