package com.dictionary.word.slang.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlangQuiz {
    private String question;
    private final String correctOption;
    private final List<String> options;

    public SlangQuiz(String question, String correctOption, List<String> incorrectOptions) {
        this.question = question;
        this.correctOption = correctOption;

        options = new ArrayList<String>();
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

    // TODO: delete later
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Question: ");
        sb.append(question);
        sb.append("\n");

        for (int i = 0; i < options.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(options.get(i));
            sb.append("\n");
        }

        sb.append("Correct option: ");
        sb.append(correctOption);

        return sb.toString();
    }
}
