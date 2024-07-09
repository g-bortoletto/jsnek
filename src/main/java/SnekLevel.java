import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SnekLevel extends SnekObject {

    private final LevelElement[] level;
    private final Random random;
    private final Point fruit;
    private long fruitLife = 0L;
    private long elapsedFruitLife = 0L;
    private long lastFruitLife = System.nanoTime();

    public SnekLevel(@NotNull SnekLogicMediator mediator) {
        mediator.setLevel(this);

        level = initLevel();
        random = new Random(System.nanoTime());
        fruit = new Point();
        getNextFruit();
    }

    public Point getFruit() {
        return fruit;
    }

    public void setLevelElementAt(int x, int y, LevelElement e) {
        level[y * SnekGame.TILE_ROW_COUNT + x] = e;
    }

    public LevelElement getElementAt(int x, int y) {
        return level[y * SnekGame.TILE_ROW_COUNT + x];
    }

    @Override
    public void tick(float deltaTime) {
        fruitTick();
    }

    private void fruitTick() {
        long currentTime = System.nanoTime();
        elapsedFruitLife += currentTime - lastFruitLife;
        lastFruitLife = currentTime;

        if (elapsedFruitLife > fruitLife) {
            getNextFruit();
        }
    }

    @Override
    public void render(Graphics2D g) {
        for (int y = 0; y < SnekGame.TILE_ROW_COUNT; y++) {
            for (int x = 0; x < SnekGame.TILE_ROW_COUNT; x++) {
                switch (level[y * SnekGame.TILE_ROW_COUNT + x]) {
                    case WALL -> drawWall(g, x, y);
                    case FRUIT -> drawFruit(g, x, y);
                    default -> {
                    }
                }
            }
        }
    }

    private void drawWall(@NotNull Graphics2D g, int x, int y) {
        Rectangle2D wallTile = new Rectangle2D.Float(
            x * SnekGame.TILE_SIZE,
            y * SnekGame.TILE_SIZE,
            SnekGame.TILE_SIZE,
            SnekGame.TILE_SIZE);
        g.setColor(Color.BLACK);
        g.fill(wallTile);
    }

    private void drawFruit(@NotNull Graphics2D g, int x, int y) {
        Ellipse2D fruit = new Ellipse2D.Float(
            x * SnekGame.TILE_SIZE,
            y * SnekGame.TILE_SIZE,
            SnekGame.TILE_SIZE,
            SnekGame.TILE_SIZE);
        g.setColor(Color.RED);
        g.fill(fruit);
    }

    public void getNextFruit() {
        int x, y;
        do {
            x = random.nextInt(1, SnekGame.TILE_ROW_COUNT - 2);
            y = random.nextInt(1, SnekGame.TILE_ROW_COUNT - 2);
        } while (level[y * SnekGame.TILE_ROW_COUNT + x] != LevelElement.EMPTY);

        fruitLife = TimeUnit.SECONDS.toNanos(random.nextInt(10, 21));
        elapsedFruitLife = 0L;

        fruit.setLocation(x, y);
    }

    @Contract(pure = true)
    private LevelElement @NotNull [] initLevel() {
        LevelElement[] level = new LevelElement[SnekGame.TILE_ROW_COUNT * SnekGame.TILE_ROW_COUNT];
        for (int y = 0; y < SnekGame.TILE_ROW_COUNT; y++) {
            for (int x = 0; x < SnekGame.TILE_ROW_COUNT; x++) {
                level[y * SnekGame.TILE_ROW_COUNT + x] =
                    (x == 0 || y == 0 || x == SnekGame.TILE_ROW_COUNT - 1 || y == SnekGame.TILE_ROW_COUNT - 1)
                        ? LevelElement.WALL
                        : LevelElement.EMPTY;
            }
        }
        return level;
    }

    public enum LevelElement {
        EMPTY, WALL, SNAKE, FRUIT
    }

}
