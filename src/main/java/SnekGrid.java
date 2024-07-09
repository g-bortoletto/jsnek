import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Line2D;

public class SnekGrid extends SnekObject {

    private final boolean shouldDrawGrid = false;
    private Line2D[] gridVerticalLine;
    private Line2D[] gridHorizontalLine;

    public SnekGrid() {
        fillGridLines();
    }

    @Override
    public void render(@NotNull Graphics2D g) {
        drawGrid(g);
    }

    private void drawGrid(@NotNull Graphics2D g) {
        if (!shouldDrawGrid) return;
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < gridVerticalLine.length; i++) {
            g.draw(gridVerticalLine[i]);
            g.draw(gridHorizontalLine[i]);
        }
    }

    private void fillGridLines() {
        gridVerticalLine = new Line2D.Float[(int) (float) SnekGame.TILE_ROW_COUNT + 1];
        gridHorizontalLine = new Line2D.Float[(int) (float) SnekGame.TILE_ROW_COUNT + 1];

        for (int y = 0; y <= (float) SnekGame.TILE_ROW_COUNT; y++) {
            for (int x = 0; x <= (float) SnekGame.TILE_ROW_COUNT; x++) {
                float currentX = SnekGame.TILE_SIZE * x;
                gridVerticalLine[x] = new Line2D.Float(
                    currentX,
                    0.0f,
                    currentX,
                    SnekGame.TILE_SIZE * (float) SnekGame.TILE_ROW_COUNT);
            }
            float currentY = SnekGame.TILE_SIZE * y;
            gridHorizontalLine[y] = new Line2D.Float(
                0.0f,
                currentY,
                SnekGame.TILE_SIZE * (float) SnekGame.TILE_ROW_COUNT,
                currentY);
        }
    }

}
