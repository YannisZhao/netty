package io.netty.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Protocol protocol = new Protocol();
        protocol.setLength(in.readShort());
        protocol.setCtl(in.readInt());
        System.out.println("<<<length====" + protocol.getLength());
        protocol.setPayload(ByteBufUtil.getBytes(in.readBytes(protocol.getLength())));
    }
}
