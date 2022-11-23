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

    public List<String> getOptions() {
        return options;
    }

    public String getQuestion() {
        return question;
    }

    /**
     * @param index The index of the option
     * @return True if the option at the index is the correct option
     */
    public boolean checkAnswer(int index) {
        return options.get(index).equals(correctOption);
    }

    /**
     * @param length The length of the question to be cut
     * @return The short question with the custom length.
     *         If the given length is greater than the length of the question,
     *         the question will be returned.
     */
    public String getShortQuestion(int length) {
        if (question.length() <= length) {
            return question;
        }

        return question.substring(0, length) + "...";
    }
}
