package com.study.rpc.server;


import io.netty.channel.*;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyRpcServer extends RpcServer{

	private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

	private Channel channel;

	public NettyRpcServer(int port, String protocol, RequestHandler handler){
		super(port,protocol,handler);
	}

	@Override
	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioSctpServerChannel.class).option(ChannelOption.SO_BACKLOG,100)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)throws Exception{
							ChannelPipeline p = ch.pipeline();
							p.addLast(new ChannelRequestHandler());
						}
			});
			ChannelFuture f = b.bind(port).sync();
			logger.info("accomplish the server port bind and start");
			channel = f.channel();
			//waiting to close the server
			f.channel().closeFuture().sync();
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void stop() {
		this.channel.close();
	}
	private class ChannelRequestHandler extends ChannelInboundHandlerAdapter{
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception{
			logger.info("activate");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			logger.info("server receive the message:" + msg);
			ByteBuf msgBuf = (ByteBuf)msg;
			byte[] req = new byte[msgBuf.readableBytes()];
			msgBuf.readBytes(req);
			byte[] res = handler.handleReqest(req);
			logger.info("sending message: "+msg);
			ByteBuf respBuf = Unpooled.buffer(res.length);
			respBuf.writeBytes(res);
			ctx.write(respBuf);
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
