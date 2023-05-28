package org.nachain.core.examiner;

import org.nachain.core.util.RipeMDUtils;


public class FloatPaper implements ExamPaper {

    @Override
    public int version() {
        return 0x01;
    }

    @Override
    public String examInfo() {
        return null;
    }

    @Override
    public int paper(String questions) {
        return 0;
    }


    public ExamAnswer examine(QuestionArgs questionArgs) throws Exception {

        long startTime = System.currentTimeMillis();


        final double number = questionArgs.getNumber();

        final int runTotal = questionArgs.getRunTotal();

        double calc = 47.474;


        for (int i = 0; i < runTotal; i++) {
            calc = (calc * number) / (number * 0.99999999);
        }


        String answer = RipeMDUtils.encodeRipeMD160Hex(String.valueOf(calc).getBytes());


        long useTime = System.currentTimeMillis() - startTime;


        ExamAnswer examAnswer = new ExamAnswer(answer, useTime);

        return examAnswer;
    }

}
