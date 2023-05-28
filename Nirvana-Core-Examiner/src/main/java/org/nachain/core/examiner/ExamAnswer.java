package org.nachain.core.examiner;

public class ExamAnswer {


    private String answer;


    private long useTime;

    public ExamAnswer(String answer, long useTime) {
        this.answer = answer;
        this.useTime = useTime;
    }

    public String getAnswer() {
        return answer;
    }

    public ExamAnswer setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public long getUseTime() {
        return useTime;
    }

    public ExamAnswer setUseTime(long useTime) {
        this.useTime = useTime;
        return this;
    }

    @Override
    public String toString() {
        return "ExamAnswer{" +
                "answer='" + answer + '\'' +
                ", useTime=" + useTime +
                '}';
    }

}
