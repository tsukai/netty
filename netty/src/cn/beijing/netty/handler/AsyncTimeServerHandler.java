/**
 * 
 */
package cn.beijing.netty.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author zukai 2015-11-25
 */
public class AsyncTimeServerHandler implements Runnable {
	private int port;
	CountDownLatch cdl;
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;//异步的服务端通道
	public AsyncTimeServerHandler(int port){
		this.port = port;
		try {
			asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("The time server is satrt in port: "+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		cdl = new CountDownLatch(1);
		doAccept();
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doAccept() {
		asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
	}

}
