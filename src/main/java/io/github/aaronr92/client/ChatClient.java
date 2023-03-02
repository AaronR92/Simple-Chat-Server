package io.github.aaronr92.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        new ChatClient("localhost", 8000).start();
    }

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());

            Channel channel = bootstrap.connect(host, port).sync().channel();
            System.out.println("connected to " + channel.remoteAddress() + " as " + channel.localAddress());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message = scanner.nextLine() + "\r\n";
                channel.write(message);
                System.out.println(message);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}