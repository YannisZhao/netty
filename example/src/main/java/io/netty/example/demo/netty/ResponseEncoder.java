package io.netty.example.demo.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ResponseEncoder extends MessageToMessageEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, List<Object> out) throws Exception {
        Protocol protocol = new Protocol();
        byte[] payload = JSON.toJSONBytes(msg);
        protocol.setLength(payload.length);
        protocol.setCtl(0);
        protocol.setPayload(payload);

        out.add(protocol);
    }
}
