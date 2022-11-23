import com.dictionary.word.slang.utils.ErrorLogger;
import com.dictionary.word.slang.views.SlangFrame;

public class Application {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new SlangFrame();
            } catch (Exception exception) {
                ErrorLogger.severe(exception);
            }
        });
    }
}
