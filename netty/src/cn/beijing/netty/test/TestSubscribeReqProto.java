/**
 * 
 */
package cn.beijing.netty.test;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

import cn.beijing.netty.model.SubscribeReqProto;

/**
 * protobuf 使用
 * @author zukai 2015-11-27
 * require jdk 1.7+
 */
public class TestSubscribeReqProto {
	private static byte[] encode(SubscribeReqProto.SubscribeReq req){
		return req.toByteArray();
	}
	
	private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException{
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	
	private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqId(1);
		builder.setUserName("netty");
		builder.setPhoneNumber("138********");
		List<String> address = new ArrayList<>();
		address.add("haidian beijing");
		address.add("chaoyang beijing");
		builder.addAllAddress(address);
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req = createSubscribeReq();
		System.out.println("Before encode: "+req.toString());
		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
		System.out.println("After decode: "+req2.toString());
		System.err.println("Assert equals: "+req.equals(req2));
	}
}
