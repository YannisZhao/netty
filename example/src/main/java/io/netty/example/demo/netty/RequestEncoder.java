package io.netty.example.demo.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@Sharable
public class RequestEncoder extends MessageToMessageEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
        Protocol protocol = new Protocol();
        byte[] payload = JSON.toJSONBytes(msg);
        protocol.setLength(payload.length);
        protocol.setCtl(1);
        protocol.setPayload(payload);

        out.add(protocol);
    }
}
