import com.dictionary.word.slang.views.SlangFrame;

public class Application implements Runnable {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Application());
    }

    @Override
    public void run() {
        new SlangFrame();
    }
}
