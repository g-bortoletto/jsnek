import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SnekNode extends SnekObject {

    public final int logicalX, logicalY;

    public float visualX, visualY;
    public float size;
    public Color color;

    public SnekNode(int x, int y, float size, Color color) {
        this.logicalX = x;
        this.logicalY = y;
        this.visualX = x * SnekGame.TILE_SIZE;
        this.visualY = y * SnekGame.TILE_SIZE;
        this.size = size;
        this.color = color;
    }

    @Override
    public void render(@NotNull Graphics2D g) {
        RoundRectangle2D visual = new RoundRectangle2D.Float(
            visualX,
            visualY,
            size,
            size, 10.0f, 10.0f);

        g.setColor(color);
        g.fill(visual);
    }
}
