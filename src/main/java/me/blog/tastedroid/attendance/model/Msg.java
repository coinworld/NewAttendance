package me.blog.tastedroid.attendance.model;

public class Msg {

    public static enum Color {
        WARN, INFO;
    }

    private final String msg;
    private final Color color;

    private Msg(String msg, Color color) {
        this.msg = msg;
        this.color = color;
    }

    public static Msg warn(String msg) {
        return new Msg(msg, Color.WARN);
    }

    public static Msg info(String msg) {
        return new Msg(msg, Color.INFO);
    }

    public String getMessage() {
        return msg;
    }

    public Color getColor() {
        return color;
    }
}
