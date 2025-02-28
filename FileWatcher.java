package Backend;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher implements Runnable {
    private final Path path;
    private final Runnable onChange;

    public FileWatcher(Path path, Runnable onChange) {
        this.path = path;
        this.onChange = onChange;
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        onChange.run();
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
