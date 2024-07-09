import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;

public class SnekGame extends Canvas implements Runnable {

    public final static int TILE_ROW_COUNT = 32;
    public final static int SCREEN_SIZE = 800;
    public final static float TILE_SIZE = (float) SCREEN_SIZE / TILE_ROW_COUNT;

    private final static long SECOND_NANOS = TimeUnit.SECONDS.toNanos(1);
    private final JFrame window;
    private final SnekGrid grid;
    private final SnekLevel level;
    private final SnekLogicMediator logic;
    private final Snek snek;
    private boolean isRunning = true;

    public SnekGame() {
        super();
        window = createWindow();
        grid = new SnekGrid();
        logic = new SnekLogicMediator();
        level = new SnekLevel(logic);
        snek = new Snek(logic);
        addKeyListener(snek);
    }

    private static void endFrame(@NotNull Frame frame) {
        frame.g().dispose();
        frame.bufferStrategy().show();
    }

    private static void clearBackground(@NotNull Frame frame) {
        frame.g().setColor(Color.DARK_GRAY);
        frame.g().fillRect(0, 0, frame.width(), frame.height());
    }

    private static SnekGame.@Nullable Frame initFrame(SnekGame snekGame) {
        if (snekGame == null) {
            return null;
        }

        BufferStrategy bufferStrategy = snekGame.getBufferStrategy();
        if (bufferStrategy == null) {
            snekGame.createBufferStrategy(2);
            return null;
        }

        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        if (g == null) {
            return null;
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        return new Frame(bufferStrategy, g, snekGame.window.getWidth(), snekGame.window.getHeight());
    }

    public static void main(String[] args) {
        SnekGame game = new SnekGame();
        game.run();
    }

    public void run() {
        requestFocus();

        long lastTime = System.nanoTime();
        float deltaTime = 0f;
        final long nsPerTick = SECOND_NANOS / 8L;
        long timer = System.currentTimeMillis();
        int frameCount = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();
            long frameTime = currentTime - lastTime;

            if (frameTime > (SECOND_NANOS / 2)) {
                lastTime = currentTime;
                continue;
            }

            deltaTime += (float) frameTime / nsPerTick;
            lastTime = currentTime;

            while (deltaTime >= 1) {
                tick(deltaTime);
                deltaTime -= 1;
            }

            render();
            frameCount++;

            if (System.currentTimeMillis() - timer >= 1000L) {
                window.setTitle("Snek [FPS: %d]".formatted(frameCount));
                timer = System.currentTimeMillis();
                frameCount = 0;
            }
        }
    }

    private @NotNull JFrame createWindow() {
        setPreferredSize(new Dimension(800, 800));
        final JFrame window;
        window = new JFrame("Snek");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRunning = false;
                super.windowClosing(e);
            }
        });
        return window;
    }

    private void tick(float deltaTime) {
        grid.tick(deltaTime);
        level.tick(deltaTime);
        snek.tick(deltaTime);
        logic.tick(deltaTime);
    }

    private void render() {
        Frame frame = initFrame(this);
        if (frame == null) return;
        if (frame.g() == null) return;

        clearBackground(frame);
        grid.render(frame.g());
        level.render(frame.g());
        snek.render(frame.g());

        endFrame(frame);
    }

    private record Frame(BufferStrategy bufferStrategy, Graphics2D g, int width, int height) {
    }

}
