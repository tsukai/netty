/**
 * 
 */
package cn.beijing.netty.server;

import cn.beijing.netty.handler.AsyncTimeServerHandler;

/**
 * @author zukai 2015-11-25
 */
public class TimeServerAIO {
	public static void main(String[] args) {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
		new Thread(timeServer,"AIO-AsyncTimeServerHandler-001").start();;
	}
}
