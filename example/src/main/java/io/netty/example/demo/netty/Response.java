package io.netty.example.demo.netty;

public class Response {

    boolean success;
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
            "success=" + success +
            ", message='" + message + '\'' +
            '}';
    }
}
