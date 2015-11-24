/**
 * 
 */
package cn.beijing.netty.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.beijing.netty.handler.TimeServerHandler;

/**
 * 
 * @author zukai 2015-11-24
 */
public class TimeServer {
	public static void main(String[] args) throws IOException {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		ServerSocket server = null;
		try{
			server = new ServerSocket(port);
			System.out.println("The time server is start in port : "+port);
			Socket socket = null;
			while(true){
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		}finally{
			if(server != null){
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}
	}
}
