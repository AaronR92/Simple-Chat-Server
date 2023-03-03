package io.github.aaronr92.server;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.writeAndFlush("<Server> " + incoming.remoteAddress() + " has joined the party!");
        }
        channels.add(incoming);

        System.out.println(incoming.remoteAddress() + " connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.writeAndFlush("<Server> " + incoming.remoteAddress() + " has left the chat!");
        }
        channels.remove(incoming);

        System.out.println(incoming.remoteAddress() + " disconnected");
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        System.out.println("<" + incoming.remoteAddress() + "> " + message);
        String[] msg = message.split("!-!");
        for (Channel c :
                channels) {
            if (c != incoming) {
                c.writeAndFlush("<" + msg[0] + "> " + msg[1]);
            }
        }
    }
}
