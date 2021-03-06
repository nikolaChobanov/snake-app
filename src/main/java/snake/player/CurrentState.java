package snake.player;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.gui.GuiElements;
import snake.utils.GenerateFruit;
import snake.utils.SegmentCoordinates;
import snake.utils.SnakeSegment;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CurrentState {

    private static int TURNS_PASSED_FOR_FRUIT_SPAWN = 10;

    private static SwingTerminal swingTerminal;

    private static Screen gameScreen;

    private Snake snake;

    private HashSet<SegmentCoordinates> walls;

    private Logger LOGGER;
    /**
     * Class to handle fruit spawning functionality
     */
    private GenerateFruit generateFruit;


    public CurrentState(SwingTerminal terminal, Screen screen, Snake snake,
                        HashSet<SegmentCoordinates> walls) {

        LOGGER = Logger.getLogger(CurrentState.class.getName());
        swingTerminal = terminal;
        gameScreen = screen;
        this.snake = snake;
        this.walls = walls;
        this.generateFruit = new GenerateFruit();
        playGame();
    }

    private Key readDirectionInput() {
        return swingTerminal.readInput();
    }

    /**
     * Taking in keyboard directions ignoring
     * input directing to the opposite direction
     */
    private void keyboardInput() {
        Key k = readDirectionInput();

        if (k != null) {

            switch (k.getKind()) {
                case ArrowUp:
                    if (!Direction.UP.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.UP);
                    }
                    break;

                case ArrowDown:
                    if (!Direction.DOWN.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.DOWN);
                    }
                    break;

                case ArrowLeft:
                    if (!Direction.LEFT.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.LEFT);
                    }
                    break;

                case ArrowRight:
                    if (!Direction.RIGHT.equals(snake.getHeadNodeDirection().getOppositeDirection())) {
                        snake.setHeadNodeDirection(Direction.RIGHT);
                    }
                    break;


                default:
                    LOGGER.log(Level.WARNING, "User pressed a key different " +
                            "from the arrow keys");
                    break;
            }
        }
    }

    /**
     * Handles the whole cycle of a single game
     * along with counting the turns needed to spawn fruit
     */
    private void playGame() {

        int turnCounter = TURNS_PASSED_FOR_FRUIT_SPAWN;

        while (!(snake.isDead())) {

            turnCounter++;

            if (turnCounter >= TURNS_PASSED_FOR_FRUIT_SPAWN) {
                spawnFruit();
                turnCounter = 0;
            }

            keyboardInput();
            snakeMovement();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Exception was thrown while thread tried " +
                        "to simulate movement", e);
            }
        }
        LOGGER.config("User's game has ended");
    }

    /**
     * Calculates the snake checkIfMoveIsValid and upon death displays a message
     * Clears the snake tail as to simulate movement on the GUI
     * Displaying the Game Over message if death has occurred
     */
    private void snakeMovement() {

        SnakeSegment snakeTail = snake.getSnakeTail();
        clearFieldSegmentPlacement(snakeTail);

        if (snake.checkIfMoveIsValid(generateFruit)) {

            SnakeSegment newHead = snake.getBody().getFirst();
            appendGuiLayer(newHead, GuiElements.SNAKE.getSign(), Terminal.Color.CYAN);
        } else {

            for (SnakeSegment sp : snake.getBody()) {
                clearFieldSegmentPlacement(sp);
            }

            int x = swingTerminal.getTerminalSize().getColumns() / 2;
            int y = swingTerminal.getTerminalSize().getRows() / 2;
            appendGuiLayer(new SegmentCoordinates(x, y), "GAME", Terminal.Color.CYAN);
            appendGuiLayer(new SegmentCoordinates(x, ++y), "OVER", Terminal.Color.CYAN);
        }
        gameScreen.refresh();
        LOGGER.config("User's screen has updated");
    }

    /**
     * Places a fruit on the GUI field
     */
    private void spawnFruit() {

        boolean isItPearTime = generateFruit.pearTime();

        SegmentCoordinates currentFruit = generateFruit.getFruitPlacement();
        if (currentFruit != null) {
            clearFieldSegmentPlacement(currentFruit);
        }

        SegmentCoordinates place = findPlaceForFruit();
        if (!isItPearTime) {
            generateFruit.setCurrentGuiElementsType(GuiElements.APPLE);
            generateFruit.setFruitPlacement(place);
            appendGuiLayer(place, GuiElements.APPLE.getSign(), Terminal.Color.RED);
        } else {
            generateFruit.setCurrentGuiElementsType(GuiElements.PEAR);
            generateFruit.setFruitPlacement(place);
            appendGuiLayer(place, GuiElements.PEAR.getSign(), Terminal.Color.MAGENTA);
        }
    }

    /**
     * Finds a random grass spot to place a fruit
     *
     * @return coordinates to the exact spot
     */
    private SegmentCoordinates findPlaceForFruit() {

        SegmentCoordinates segmentCoordinates;
        do {
            segmentCoordinates = new SegmentCoordinates(1, swingTerminal.getTerminalSize().getRows(), 1, swingTerminal.getTerminalSize().getColumns());
        } while (isWall(segmentCoordinates));

        if (snake.getBody().contains(segmentCoordinates)) {
            return findPlaceForFruit();
        } else {
            return segmentCoordinates;
        }

    }

    /**
     * Places a grass spot on the field clearing anything else
     *
     * @param snakeSegment coordinates to the cleared spot snake or fruit.
     */
    private void clearFieldSegmentPlacement(SegmentCoordinates snakeSegment) {
        appendGuiLayer(snakeSegment, GuiElements.GRASS.getSign(), null);
    }

    private void appendGuiLayer(SegmentCoordinates segmentCoordinates, String string, Terminal.Color color) {
        gameScreen.putString(segmentCoordinates.getX(), segmentCoordinates.getY(), string, color, null);
    }

    private boolean isWall(SegmentCoordinates segmentCoordinates) {
        return walls.contains(segmentCoordinates);
    }


}
