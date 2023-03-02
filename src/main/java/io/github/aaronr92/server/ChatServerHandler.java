package io.github.aaronr92.server;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

public class ChatServerHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup();



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.write("<Server> " + incoming.remoteAddress() + " has joined the party!\n");
        }
        channels.add(incoming);

        System.out.println(incoming.remoteAddress() + " connected");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel :
                channels) {
            channel.write("<Server> " + incoming.remoteAddress() + " has left the chat!\n");
        }
        channels.remove(incoming);

        System.out.println(incoming.remoteAddress() + " disconnected");
    }

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        System.out.println("Message " + message);
        for (Channel c :
                channels) {
            if (c != incoming) {
                c.write("<" + incoming.remoteAddress() + "> " + message + "\n");
            }
        }
    }
}
