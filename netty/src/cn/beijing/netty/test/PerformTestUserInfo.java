package cn.beijing.netty.test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.junit.Test;

import cn.beijing.netty.serialize.UserInfo;

public class PerformTestUserInfo {

	@SuppressWarnings("unused")
	@Test
	public void testPerformance() throws Exception {
		UserInfo info = new UserInfo();
		info.buildUserId(100).buildUserName("Welcome to Netty");
		int loop = 1000000;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(info);
			os.flush();
			os.close();
			byte[] b = bos.toByteArray();
			bos.close();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("The jdk serializable cost time is: "+(endTime-startTime)+"ms");
		System.out.println("============");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			byte[] b = info.codec(buffer);
		}
		endTime = System.currentTimeMillis();
		System.out.println("The byte array serializable cost time is: "+(endTime-startTime)+"ms");
	}
}
