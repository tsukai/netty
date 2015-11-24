/**
 * 
 */
package cn.beijing.netty.client;

import cn.beijing.netty.handler.TimeClientHandlerNIO;

/**
 * @author zukai 2015-11-24
 */
public class TimeClientNIO {
	public static void main(String[] args) {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new Thread(new TimeClientHandlerNIO("127.0.0.1",port),"TimeClient-001").start();
	}
}
