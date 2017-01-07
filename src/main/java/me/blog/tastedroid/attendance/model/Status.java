package me.blog.tastedroid.attendance.model;

public enum Status {

    OTHER_DAY(Msg.warn("접속일과 다릅니다. 나갔다 들어와주세요.")),
    ALREADY_CHECKED(Msg.warn("오늘 이미 성공했습니다.")),
    TWO_DAYS(Msg.warn("2일 연속 인증은 안 됩니다. 나갔다 들어와주세요.")),
    SUCCESS(Msg.warn("출석 인증에 성공했습니다.")),
    FAIL(Msg.warn("아직 출석 인증이 되지 않았습니다."));

    private Msg msg;

    Status(Msg msg) {
        this.msg = msg;
    }

    public Msg getMessage() {
        return msg;
    }
}
