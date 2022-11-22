package com.dictionary.word.slang.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlangQuiz {
    private final String question;
    private final String correctOption;
    private final List<String> options;

    public SlangQuiz(String question, String correctOption, List<String> incorrectOptions) {
        this.question = question;
        this.correctOption = correctOption;

        options = new ArrayList<>();
        options.add(correctOption);
        options.addAll(incorrectOptions);

        Collections.shuffle(options);
    }

    public boolean checkAnswer(int index) {
        return options.get(index).equals(correctOption);
    }

    public List<String> getOptions() {
        return options;
    }

    public String getQuestion() {
        return question;
    }

    public String getShortQuestion(int length) {
        if (question.length() <= length) {
            return question;
        }

        return question.substring(0, length) + "...";
    }
}
