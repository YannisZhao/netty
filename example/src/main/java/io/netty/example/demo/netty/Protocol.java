package io.netty.example.demo.netty;

public class Protocol {
    private int length;
    private int ctl;
    private byte[] payload;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCtl() {
        return ctl;
    }

    public void setCtl(int ctl) {
        this.ctl = ctl;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
