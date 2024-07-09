import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Deque;
import java.util.LinkedList;

public class Snek extends KeyAdapter implements SnekBehavior {

    private final Deque<SnekNode> body;
    private final SnekLogicMediator mediator;

    private Direction direction = Direction.NONE;

    public Snek(@NotNull SnekLogicMediator mediator) {
        this.mediator = mediator;
        body = new LinkedList<>();
        initBody();
        initMediator();
    }

    private void initBody() {
        int halfRow = SnekGame.TILE_ROW_COUNT / 2;
        Point initialPosition = new Point(halfRow, halfRow - 3);
        for (int i = 6; i > 0; i--) {
            addNode(initialPosition.x, initialPosition.y + i, 25.0f, Color.WHITE);
        }
    }

    public void reset() {
        body.clear();
        initBody();
        direction = Direction.NONE;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Deque<SnekNode> getBody() {
        return this.body;
    }

    public void advance() {
        if (direction == Direction.NONE) {
            return;
        }

        SnekNode head = body.getFirst();
        int x = head.logicalX;
        int y = head.logicalY;

        switch (direction) {
            case LEFT -> x -= 1;
            case UP -> y -= 1;
            case RIGHT -> x += 1;
            case DOWN -> y += 1;
        }

        checkCollision(x, y);
    }

    public void addNode(int x, int y, float size, Color color) {
        SnekNode newNode = new SnekNode(x, y, size, color);
        body.addFirst(newNode);
    }

    public void removeNode() {
        body.removeLast();
    }

    public void move(int x, int y) {
        addNode(x, y, 25.0f, Color.WHITE);
        removeNode();
    }

    public void grow(int x, int y) {
        SnekNode last = body.getLast();
        move(x, y);
        body.addLast(last);
    }

    private void checkCollision(int x, int y) {
        mediator.handleCollision(x, y);
    }

    private void initMediator() {
        mediator.setSnek(this);
    }

    @Override
    public void tick(float deltaTime) {
        advance();
    }

    @Override
    public void render(@NotNull Graphics2D g) {
        for (SnekNode node : body) {
            node.render(g);
        }
    }

    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> setDirection(direction.changeTo(Direction.LEFT));
            case KeyEvent.VK_UP -> setDirection(direction.changeTo(Direction.UP));
            case KeyEvent.VK_RIGHT -> setDirection(direction.changeTo(Direction.RIGHT));
            case KeyEvent.VK_DOWN -> setDirection(direction.changeTo(Direction.DOWN));
        }
    }

    public enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN,
        NONE;

        public Direction changeTo(Direction newDirection) {
            if ((this == LEFT && newDirection != RIGHT) ||
                (this == RIGHT && newDirection != LEFT) ||
                (this == UP && newDirection != DOWN) ||
                (this == DOWN && newDirection != UP) ||
                (this == NONE)) {
                return newDirection;
            }
            return this;
        }
    }

}
