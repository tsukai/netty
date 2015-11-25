/**
 * 
 */
package cn.beijing.netty.client;

import cn.beijing.netty.handler.AsyncTimeClientHandler;

/**
 * @author zukai 2015-11-25
 */
public class TimeClientAIO {
	public static void main(String[] args) {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-AsyncTimeClientHandler-001").start();
	}
}
