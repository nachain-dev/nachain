package org.nachain.core.examiner;

import org.nachain.core.util.RipeMDUtils;


public class StringPaper implements ExamPaper {

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


        String adding = questionArgs.getText();

        int runTotal = questionArgs.getRunTotal();

        StringBuffer content = new StringBuffer("Nirvana examiner system...");


        for (int i = 1; i <= runTotal; i++) {
            content.append(adding);
        }


        String answer = RipeMDUtils.encodeRipeMD160Hex(content.toString().getBytes());


        long useTime = System.currentTimeMillis() - startTime;

        ExamAnswer examAnswer = new ExamAnswer(answer, useTime);

        return examAnswer;
    }

}
