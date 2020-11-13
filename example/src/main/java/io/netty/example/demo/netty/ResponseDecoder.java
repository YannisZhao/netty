package io.netty.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class ResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception {
        Response response = new Response();
        response.setSuccess(in.readBoolean());
        response.setMessage(in.readBytes(in.readableBytes()).toString());

        out.add(response);
    }
}
