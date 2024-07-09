public class SnekLogicMediator extends SnekObject {

    private Snek snek = null;
    private SnekLevel level = null;

    public void setLevel(SnekLevel level) {
        this.level = level;
    }

    public void setSnek(Snek snek) {
        this.snek = snek;
    }

    public void handleCollision(int x, int y) {
        SnekLevel.LevelElement element = level.getElementAt(x, y);

        switch (element) {
            case EMPTY -> snek.move(x, y);
            case WALL, SNAKE -> snek.reset();
            case FRUIT -> {
                level.getNextFruit();
                snek.grow(x, y);
            }
        }
    }

    private void updateLevel() {
        for (int y = 1; y < (SnekGame.TILE_ROW_COUNT - 1); y++) {
            for (int x = 1; x < (SnekGame.TILE_ROW_COUNT - 1); x++) {
                level.setLevelElementAt(x, y, SnekLevel.LevelElement.EMPTY);
            }
        }

        for (SnekNode node : snek.getBody()) {
            level.setLevelElementAt(node.logicalX, node.logicalY, SnekLevel.LevelElement.SNAKE);
        }

        level.setLevelElementAt(level.getFruit().x, level.getFruit().y, SnekLevel.LevelElement.FRUIT);
    }

    @Override
    public void tick(float deltaTime) {
        updateLevel();
    }
}
