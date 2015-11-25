/**
 * 
 */
package cn.beijing.netty.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author zukai 2015-11-25
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
	public void completed(AsynchronousSocketChannel result,AsyncTimeServerHandler attachment){
		//当调用AsynchronousServerSocketChannel的accept方法后，如果有新的客户端接入，系统将回调传入的CompletionHandler实例的completed方法
		//表示新的客户端已经接入成功，继续调用accept方法，接受其他的客户端连接
		attachment.asynchronousServerSocketChannel.accept(attachment, this);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		//1.接收缓冲区
		//2.异步channel携带的附件，通知回调的时候作为入参使用
		
		result.read(buffer,buffer,new ReadCompletionHandler(result));
	}
	
	public void failed(Throwable exc,AsyncTimeServerHandler attachment){
		exc.printStackTrace();
		attachment.cdl.countDown();
	}
}
