import com.dictionary.word.slang.services.SlangService;
import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import java.util.Arrays;

public class Application implements Runnable {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Application());
    }

    @Override
    public void run() {
        String keyword = "word";

        SlangService slang = SlangService.getInstance();
        String[][] results = slang.searchByDefinition(keyword);

        for (String[] result : results) {
            System.out.println(Arrays.toString(result));
        }
    }
}
