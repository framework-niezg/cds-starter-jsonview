package com.zjcds.common.jsonview.annotations;

import java.lang.annotation.*;

/**
 * 在控制器方法使用validate验证的场景中，该注解注解在控制器类方法上面，并且BindingResult为方法的最后一个参数。
 * 当有验证结果失败时，抛出json格式的验证失败信息
 * @see org.springframework.validation.BindingResult
 * created date：2016-12-01
 * @author niezhegang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonFailureBindResult {

}
