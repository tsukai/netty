/**
 * 
 */
package cn.beijing.netty.test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import cn.beijing.netty.serialize.UserInfo;

/**
 * @author zukai 2015-11-26
 */
public class TestUserInfo {
	public static void main(String[] args) throws Exception {
		UserInfo info = new UserInfo();
		info.buildUserId(100).buildUserName("Welcome to Netty");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is: "+b.length);
		bos.close();
		System.out.println("===============================");
		System.out.println("The byte array serializable length is: "+info.codec().length);
	}
}
