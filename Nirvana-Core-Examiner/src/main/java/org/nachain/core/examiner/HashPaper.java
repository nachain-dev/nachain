package org.nachain.core.examiner;

import org.nachain.core.util.RipeMDUtils;


public class HashPaper implements ExamPaper {

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


        String hashText = questionArgs.getText();

        int runTotal = questionArgs.getRunTotal();

        String answer = hashText;


        for (int i = 1; i <= runTotal; i++) {
            answer = RipeMDUtils.encodeRipeMD160Hex(answer.getBytes());
        }


        long useTime = System.currentTimeMillis() - startTime;


        ExamAnswer examAnswer = new ExamAnswer(answer, useTime);

        return examAnswer;
    }

}
