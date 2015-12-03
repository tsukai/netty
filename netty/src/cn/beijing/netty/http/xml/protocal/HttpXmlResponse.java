/**
 * 
 */
package cn.beijing.netty.http.xml.protocal;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author zukai 2015-12-01
 */
public class HttpXmlResponse {
	private FullHttpResponse response;
	private Object result;
	public HttpXmlResponse(FullHttpResponse response, Object result) {
		this.response = response;
		this.result = result;
	}
	public final FullHttpResponse getResponse() {
		return response;
	}
	public final void setResponse(FullHttpResponse response) {
		this.response = response;
	}
	public final Object getResult() {
		return result;
	}
	public final void setResult(Object result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "HttpXmlResponse [response=" + response + ", result=" + result
				+ "]";
	}
	
	
	
}
