import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.objects.SlangQuiz;

import java.util.List;

public class Application implements Runnable {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Application());
    }

    @Override
    public void run() {
        SlangDictionary slang = SlangDictionary.getInstance();
        String[][] slangList = slang.getAll();
//
//        for (var s : slangList) {
//            System.out.println(Arrays.toString(s));
//        }

        List<SlangQuiz> quizzes = slang.generateRandomQuizzes(5, false);

        for (SlangQuiz quiz : quizzes) {
            System.out.println(quiz);
            System.out.println();
            System.out.println();
        }
    }
}
