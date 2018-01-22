package com.zjcds.common.jsonview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjcds.common.jsonview.exception.JsonViewException;
import com.zjcds.common.jsonview.utils.JsonViewFactory;
import com.zjcds.common.jsonview.utils.ResponseResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

/**
 * json视图自动配置
 * Created by niezhegang on 2016/11/22.
 */
@Configuration
@ConditionalOnClass(value={ObjectMapper.class})
public class JsonViewAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({ObjectMapper.class})
    public ObjectMapperFactoryBean objectMapper(){
        return new ObjectMapperFactoryBean();
    }

    @Configuration
    @ConditionalOnClass({ObjectMapper.class,MappingJackson2JsonView.class,BeanNameViewResolver.class})
    @ConditionalOnMissingBean(MappingJackson2JsonView.class)
    public static class JsonViewCreater{
        @Autowired
        private ObjectMapper objectMapper;

        final static private String modelKey = "responseResult";

        @Bean(name="jsonView")
        public MappingJackson2JsonView jsonView(){
            MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
            jsonView.setExtractValueFromSingleKeyModel(true);
            jsonView.setObjectMapper(this.objectMapper);
            jsonView.setModelKey(modelKey);
            return jsonView;
        }
        /**
         * 配置一个BeanNameViewResolver
         */
        @Bean
        public BeanNameViewResolver beanNameViewResolver(){
            BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
            beanNameViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return beanNameViewResolver;
        }
    }

    /**
     * 配置一个统一的异常处理器，处理json形式的错误信息
     * created date：2016-11-29
     * @author niezhegang
     */
    @ControllerAdvice
    public static class ExceptionResolverConfigretion {
        /**日志记录器*/
        Logger logger = LoggerFactory.getLogger(this.getClass());

        @ExceptionHandler(JsonViewException.class)
        public ModelAndView resolveJsonViewException(HttpServletRequest req, JsonViewException exception) {
            //首先记录错误日志
            String msg = exception.getMessage();
            if(msg == null) {
                msg = "请求出错！";
            }
            logger.error(msg, exception);
            return JsonViewFactory.buildJsonView(new ResponseResult<Object>(false,msg));
        }

        @ExceptionHandler({BindException.class, TypeMismatchException.class, MethodArgumentNotValidException.class, ServletRequestBindingException.class})
        public ModelAndView resolveBindException(HttpServletRequest req, Exception exception) {
            //首先记录错误日志
            String msg = exception.getMessage();
            if(msg == null) {
                msg = "参数绑定出错！";
            }
            logger.error(msg, exception);
            return JsonViewFactory.buildJsonView(new ResponseResult<Object>(false,msg));
        }

    }

    /**
     * The type Auto proxy json view configretion.
     * niezhegang
     */
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class AutoProxyJsonViewConfigretion {
        @Component
        @Aspect
        public static class JsonViewAspect {

            @Pointcut("@target(org.springframework.stereotype.Controller) && @annotation(com.zjcds.common.jsonview.annotations.JsonView)")
            public void jsonView() {}

            @Around("jsonView()")
            public ModelAndView doJsonViewProcess(ProceedingJoinPoint pjp) {
                try {
                    Object ret = pjp.proceed();
                    return JsonViewFactory.buildJsonView(ret);
                }
                catch (Throwable t){
                    throw new JsonViewException(t.getMessage(),t);
                }
            }
        }

        @Component
        @Aspect
        public static class JsonViewExceptionAspect {

            @Pointcut("execution(* com.zjcds..*(..)) "+
                    "(@target(org.springframework.stereotype.Controller) || @target(org.springframework.web.bind.annotation.RestController))" +
                    "&& (@annotation(com.zjcds.common.jsonview.annotations.JsonViewException) || @target(com.zjcds.common.jsonview.annotations.JsonViewException))")
            public void jsonViewException() {}

            @AfterThrowing(pointcut = "jsonViewException()",throwing = "ex")
            public void doJsonViewExceptionProcess(Exception ex) {
                if( !(ex instanceof JsonViewException)){
                    throw new JsonViewException(ex);
                }
            }
        }


        @Component
        @Aspect
        public static class JsonFailureBindResultAspect {
            @Pointcut("execution(* com.zjcds..*( .. , org.springframework.validation.BindingResult)) " +
                    "&& @annotation(com.zjcds.common.jsonview.annotations.JsonFailureBindResult) " +
                    "&& (@target(org.springframework.stereotype.Controller) || @target(org.springframework.web.bind.annotation.RestController))" +
                    "&& args(..,bindingResult)")
            public void jsonFailureBindResult(BindingResult bindingResult) {}

            @Before("jsonFailureBindResult(bindingResult)")
            public void doJsonFailureBindResult(JoinPoint joinPoint ,BindingResult bindingResult) throws Throwable{
                if(bindingResult.hasErrors()){
                    throw new JsonViewException(bindingResult.getAllErrors().toString());
                }
            }

//            @Before("jsonFailureBindResult()")
//            @Order(Ordered.HIGHEST_PRECEDENCE + 5)
//            public void doJsonFailureBindResult(JoinPoint joinPoint) throws Throwable{
//                Object[] args = joinPoint.getArgs();
//                BindingResult matchParam ;
//                if (!ArrayUtils.isEmpty(args)) {
//                    for(Object obj : args) {
//                        if(obj instanceof BindingResult){
//                            matchParam = (BindingResult) obj;
//                            if(matchParam.hasErrors()){
//                                throw new JsonViewException(matchParam.getAllErrors().toString());
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

}
