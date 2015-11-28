/**
 * 
 */
package cn.beijing.netty.factory;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * @author zukai 2015-11-28
 */
public final class MarshallingCodecFactory {

	/**
	 * 创建Jboss Marshalling解码器MarshallingDecoder
	 * @return
	 */
	public static ChannelHandler buildMarshallingDecoder() {
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");//serial表示创建Java序列化工厂
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
		return new MarshallingDecoder(provider, 1024);//单个消息序列化后的最大长度
	}

	/**
	 * 创建encoder
	 * @return
	 */
	public static ChannelHandler buildMarshallingEncoder() {
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
		return new MarshallingEncoder(provider);
	}
	
}
