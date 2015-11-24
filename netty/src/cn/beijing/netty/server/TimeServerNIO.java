/**
 * 
 */
package cn.beijing.netty.server;

/**
 * @author zukai 2015-11-24
 */
public class TimeServerNIO {
	public static void main(String[] args) {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
	}
}
