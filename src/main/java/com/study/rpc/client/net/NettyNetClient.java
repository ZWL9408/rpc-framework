package com.study.rpc.client.net;

import com.study.rpc.discovery.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;

public class NettyNetClient implements NetClient{

	private static Logger logger= LoggerFactory.getLogger(NettyNetClient.class);
//	@Override
	public byte[] sendRequest(byte[] data, ServiceInfo sinfo) throws Throwable {
		String[] addInfoArray = sinfo.getAddress().split(":");

		final SendHandler sendHandler = new SendHandler(data);

		byte[] respData = null;

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(sendHandler);
						}
					});
			b.connect(addInfoArray[0], Integer.valueOf(addInfoArray[1])).sync();
			respData = (byte[])sendHandler.rspData();
			logger.info("sendRequest get reply: " + respData);
		}finally {
			group.shutdownGracefully();
		}

		return respData;
	}

	private class SendHandler extends ChannelInboundHandlerAdapter{
		private CountDownLatch cdl = null;
		private Object readMsg = null;
		private byte[] data;
		public SendHandler(byte[] data){
			cdl = new CountDownLatch(1);
			this.data = data;
		}
		public Object rspData(){
			try {
				cdl.await();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			return readMsg;
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			logger.info("client read msg: "+msg);
			ByteBuf msgBuf = (ByteBuf)msg;
			byte[] resp = new byte[msgBuf.readableBytes()];
			msgBuf.readBytes(resp);
			readMsg = resp;
			cdl.countDown();
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			logger.error("happen exception:" + cause.getMessage());
			ctx.close();
		}
	}

}
