import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lucasmfredmark on 19/07/2016.
 */
public class Houdini {
    public static void main(String[] args) {
        String gameFilesDir = "resources/gameFiles/";
        /*ExecutorService pool = Executors.newCachedThreadPool();

        try {
            Files.walk(Paths.get(gameFilesDir)).filter(Files::isRegularFile).forEach(file -> pool.submit(() -> {
                Game game = new Game();
                game.loadGameFile(gameFilesDir + file.getFileName().toString());
                game.solve();
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pool.shutdown();*/

        Game game = new Game();
        game.loadGameFile(gameFilesDir + "0a034bd4-9363-4edd-9d3d-0ed56418aa4e");
        game.solve();
    }
}
