package io.netty.example.demo.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@Sharable
public class RequestDecoder extends MessageToMessageDecoder<Protocol> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Protocol msg, List<Object> out) throws Exception {
        int ctl = msg.getCtl();
        if ((ctl & 1) == 0) {
            return;
        }

        Request request = JSON.parseObject(msg.getPayload(), Request.class);

        out.add(request);
    }
}
