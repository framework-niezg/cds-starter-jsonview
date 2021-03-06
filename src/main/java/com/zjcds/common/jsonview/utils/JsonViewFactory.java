package com.zjcds.common.jsonview.utils;

import com.zjcds.common.jsonview.exception.HttpStatusException;
import com.zjcds.common.jsonview.exception.JsonViewException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * JSON视图工厂类，用于构建控制层响应的JSON视图对象
 *
 * 创建日期：2014-5-19
 * @author niezhegang
 */
public class JsonViewFactory {
	/** 视图名称，使用系统配置的统一名称：jsonView */
	public static final String VIEW_NAME = "jsonView";
	/** 模型数据的Key值，使用系统配置的统一Key值：responseResult */
	public static final String MODEL_KEY = "responseResult";

	/**
	 * 构建JSON视图对象，用于请求成功但不需要响应数据和响应消息的情况
	 *
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static ModelAndView buildJsonView() {
		return buildJsonView(new ModelAndView());
	}

	public static ModelAndView buildJsonView(HttpStatus httpStatus) {
		return buildJsonView(createModelAndViewWithHttpStatus(httpStatus));
	}

	private static ModelAndView createModelAndViewWithHttpStatus(HttpStatus httpStatus) {
		ModelAndView modelAndView = new ModelAndView();
		if(httpStatus != null)
			modelAndView.setStatus(httpStatus);
		return modelAndView;
	}

	/**
	 * 构建JSON视图对象，用于请求成功且需要响应数据的情况
	 *
	 * @param <T>  响应数据类型参数
	 * @param data 响应数据
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static <T> ModelAndView buildJsonView(T data) {
    	return buildJsonView(new ModelAndView(), data);
	}

	public static <T> ModelAndView buildJsonView(HttpStatus httpStatus,T data) {
		return buildJsonView(createModelAndViewWithHttpStatus(httpStatus), data);
	}

	/**
	 * 构建JSON视图对象，需要指定完整的响应结果对象
	 *
	 * @param <T>            响应数据类型参数
	 * @param responseResult 响应结果
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static <T> ModelAndView buildJsonView(ResponseResult<T> responseResult) {
		return buildJsonView(new ModelAndView(), responseResult);
	}

	public static <T> ModelAndView buildJsonView(HttpStatus httpStatus,ResponseResult<T> responseResult) {
		return buildJsonView(createModelAndViewWithHttpStatus(httpStatus), responseResult);
	}

	/**
	 * 在指定的modelAndView对象上构建JSON视图对象，用于设置视图名和添加响应结果，下同
	 *
	 * @param modelAndView 模型视图对象
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static ModelAndView buildJsonView(ModelAndView modelAndView) {
		return buildJsonView(modelAndView, new ResponseResult<Object>());
	}

	/**
	 * 构建JSON视图对象，用法说明见buildJsonView(data)
	 *
	 * @param <T>
	 * @param modelAndView 模型视图对象
	 * @param data         响应数据
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static <T> ModelAndView buildJsonView(ModelAndView modelAndView, T data) {
    	return buildJsonView(modelAndView, new ResponseResult<T>(data));
	}

	/**
	 * 构建JSON视图对象，用法说明见buildJsonView(responseResult)
	 *
	 * @param <T>
	 * @param modelAndView   模型视图对象
	 * @param responseResult 响应结果
	 * @return
	 * 创建日期：2014-5-19
	 * 修改说明：
	 * @author niezhegang
	 */
	public static <T> ModelAndView buildJsonView(ModelAndView modelAndView, ResponseResult<T> responseResult) {
		modelAndView.setViewName(VIEW_NAME);
    	modelAndView.addObject(MODEL_KEY, responseResult);
    	return modelAndView;
	}

	public static ModelAndView buildJsonViewException(Exception e) {
		String msg = e.getMessage();
		if(msg == null) {
			msg = "请求出错！";
		}
        ModelAndView modelAndView;
		if(e instanceof HttpStatusException) {
            HttpStatusException httpStatusException = (HttpStatusException) e;
            modelAndView = buildJsonView(httpStatusException.getHttpStatusCode(),new ResponseResult<Object>(false,msg));
        }
        else {
            modelAndView = buildJsonView(new ResponseResult<Object>(false,msg));
            if(e instanceof JsonViewException) {
                modelAndView.setStatus(((JsonViewException) e).getHttpStatus());
            }
        }
		return modelAndView;
	}

}
