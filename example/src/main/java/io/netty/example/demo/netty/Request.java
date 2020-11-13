package io.netty.example.demo.netty;

public class Request {

    long id;
    String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Request{" +
            "id=" + id +
            ", message='" + message + '\'' +
            '}';
    }
}
