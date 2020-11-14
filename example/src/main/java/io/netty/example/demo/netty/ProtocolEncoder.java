package io.netty.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class ProtocolEncoder extends MessageToByteEncoder<Protocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, ByteBuf out) throws Exception {
        byte[] payload = msg.getPayload();
        System.out.println(">>>length====" + payload.length);
        out.writeInt(payload.length);
        out.writeInt(msg.getCtl());
        out.writeBytes(payload);
    }
}
