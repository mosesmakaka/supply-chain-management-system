package GUI.util;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class SwingWorkers {
    public static <T> void run(Callable<T> background, Consumer<T> onDone, Consumer<Exception> onError) {
        SwingWorker<T, Void> worker = new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return background.call();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    if (onDone != null) onDone.accept(result);
                } catch (Exception ex) {
                    if (onError != null) onError.accept(ex);
                    else ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
