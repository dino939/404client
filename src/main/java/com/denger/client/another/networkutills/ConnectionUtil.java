package com.denger.client.another.networkutills;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.IPacket;

import static com.denger.client.MainNative.mc;

public class ConnectionUtil extends ChannelDuplexHandler {
    public enum Side {IN, OUT}

    EventsHandlerUtil eventHandler;
    ChannelHandlerContext priv;
    ChannelPromise promise;

    public ConnectionUtil(EventsHandlerUtil eventHandler) {
        this.eventHandler = eventHandler;
        try {
            ChannelPipeline pipeline = mc.getConnection().getConnection().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (packet instanceof IPacket) {
            priv = ctx;
        }
        if (!eventHandler.onPacket(packet, Side.IN)) return;
        super.channelRead(ctx, packet);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if (!eventHandler.onPacket(packet, Side.OUT)) return;

        super.write(ctx, packet, promise);
    }


}