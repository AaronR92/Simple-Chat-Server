package io.github.aaronr92.server;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.writeAndFlush("<Server> " + incoming.remoteAddress() + " has joined the party!\n");
        }
        channels.add(incoming);

        System.out.println(incoming.remoteAddress() + " connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.writeAndFlush("<Server> " + incoming.remoteAddress() + " has left the chat!\n");
        }
        channels.remove(incoming);

        System.out.println(incoming.remoteAddress() + " disconnected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        System.out.println("Message " + message);
        for (Channel c :
                channels) {
            c.writeAndFlush("<" + incoming.remoteAddress() + "> " + message + "\n");
        }
    }
}
