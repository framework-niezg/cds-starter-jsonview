package com.zjcds.common.jsonview.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * 封装http状态码的异常对象
 * @author niezhegang
 */
@Getter
@Setter
public class HttpStatusException extends RuntimeException {
	
	private static final long serialVersionUID = 738679544795562430L;
	/**http错误码*/
	private HttpStatus httpStatusCode;
	/**默认客户端错误响应码*/
	public final static HttpStatus DefaultClientErrorCode = HttpStatus.BAD_REQUEST;
	/**默认服务端错误响应码*/
	public final static HttpStatus DefaultServerErrorCode = HttpStatus.SERVICE_UNAVAILABLE;

	private HttpStatusException(Builder builder) {
		super(builder.message, builder.cause);
		setHttpStatusCode(builder.httpStatusCode);
	}

	@Override
	public String getMessage() {
		String msg = super.getMessage();
		return StringUtils.isNotBlank(msg) ? msg : ( httpStatusCode != null ? httpStatusCode.getReasonPhrase() : "");
	}

	/**
	 * HttpStatusException构建器
	 * @author niezhegang
	 */
	public static class Builder{
		/**http错误码*/
		private HttpStatus httpStatusCode;
		/**异常信息*/
		private String message;
		/**异常原因*/
		private Throwable cause;
		
		public Builder httpStatusCode(HttpStatus httpStatusCode){
			this.httpStatusCode = httpStatusCode;
			return this;
		}
		
		public Builder message(String message){
			this.message = message;
			return this;
		}
		
		public Builder cause(Throwable cause){
			this.cause = cause;
			return this;
		}
		/**
		 * 构建一个HttpStatusException对象
		 * 未设定httpstatus的统一为200
		 * @return
		 * 修改说明：
		 * @author niezhegang
		 */
		public HttpStatusException build(){
			if(httpStatusCode == null)
				httpStatusCode = HttpStatus.OK;
			return new HttpStatusException(this);
		}
		/**
		 * 构建一个默认返回http客户端异常状态码（400）的HttpStatusException对象
		 * @return
		 * 修改说明：
		 * @author niezhegang
		 */
		public HttpStatusException buildDefaultClientException(){
			this.httpStatusCode = DefaultClientErrorCode;
			return new HttpStatusException(this);
		}
		/**
		 * 构建一个默认返回http服务端异常状态码（503）的HttpStatusException对象
		 * @return
		 * 修改说明：
		 * @author niezhegang
		 */
		public HttpStatusException buildDefaultServerException(){
			this.httpStatusCode = DefaultServerErrorCode;
			return new HttpStatusException(this);
		}
	}
}
