package snake.gui;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.player.CurrentState;
import snake.player.Direction;
import snake.player.Snake;
import snake.utils.SegmentCoordinates;
import snake.utils.SnakeSegment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

public class CreateGameField {

    //30
    private static int fieldWidth;
    //10
    private static int fieldHeight;

    private static SwingTerminal terminal;

    private static Screen screen;
    /**
     * HashSet containing all coordinates where a wall segment exists
     */
    private HashSet<SegmentCoordinates> walls;

    private Logger LOGGER;

    /**
     * Setting-up game session
     */
    private CreateGameField(String fieldFileName) {

        LOGGER = Logger.getLogger(CreateGameField.class.getName());
        //  LOGGER = LoggerFactory.getLogger(CreateGameField.class);
        walls = new HashSet<>();
        constructField(fieldFileName);
        screen.refresh();
        new CurrentState(terminal, screen, findSnakePlacement(), walls);
    }


    private File accessFile(String fileName) {
        return new File(
                Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile()
        );

    }

    /**
     * Constructing the structures needed for creation of the GUI
     */
    private void createGuiScreen() {

        terminal = new SwingTerminal(fieldWidth, fieldHeight);
        screen = new Screen(terminal);
        screen.startScreen();
    }
    /**
     * Creating lanterna GUI with the width and height parameters
     * Reading the game field from the file and applying the walls
     */
    private void constructField(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(accessFile(fileName)))) {

            fieldWidth = Integer.parseInt(br.readLine());
            fieldHeight = Integer.parseInt(br.readLine());


            createGuiScreen();

            String line;
            int heightCounter = 0;

            while ((line = br.readLine()) != null) {
                markWalls(line, heightCounter);
                appendGuiLayer(0, heightCounter++, line, Terminal.Color.YELLOW);
            }

        } catch (IOException e) {
            // LOGGER.log("Error occurred while attempting to read: " + fileName, e);
        }
    }

    /**
     * Helper function to draw items to the GUI
     *
     * @param x      - Width
     * @param y      - Height
     * @param string - String to be drawn
     * @param color  - Desired color
     */
    private void appendGuiLayer(int x, int y, String string, Terminal.Color color) {
        screen.putString(x, y, string, color, null);
    }

    private boolean isWall(int x, int y) {
        return walls.contains(new SegmentCoordinates(x, y));
    }

    /**
     * Creating the HashSet containing wall coordinates
     *
     * @param line       - Current line read from the file
     * @param currHeight - Line number of the read file being the field current height
     */
    private void markWalls(String line, int currHeight) {

        int widthCounter = 0;
        String[] splitStr = line.split("");
        for (String ch : splitStr) {
            if (ch.equals(GuiElements.WALL.getSign())) {
                walls.add(new SegmentCoordinates(widthCounter, currHeight));
            }
            widthCounter++;
        }
    }

    /**
     * Initial placing of the snake making sure the player is not spawned directly ahead of a wall
     *
     * @return - Initial snake object
     */
    private Snake findSnakePlacement() {

        int snakeHeight;
        int snakeWidth;

        do {
            SegmentCoordinates segmentCoordinates = new SegmentCoordinates(0, fieldHeight, 0, fieldWidth);
            snakeHeight = segmentCoordinates.getY();
            snakeWidth = segmentCoordinates.getX();
        } while (isWall(snakeWidth, snakeHeight) || isWall(snakeWidth + 1, snakeHeight)
                || isWall(snakeWidth + 2, snakeHeight)
                || isWall(snakeWidth - 1, snakeHeight)
                || isWall(snakeWidth - 2, snakeHeight));


        return createSnake(snakeHeight, snakeWidth);
    }

    /**
     * Constructing the snake object + drawing it in the GUI
     * and placing it heading the correct direction
     */
    private Snake createSnake(int snakeHeight, int snakeWidth) {

        LinkedList<SnakeSegment> body = new LinkedList<>();

        Direction direction = Direction.values()[new Random().nextInt(Direction.values().length - 2) + 2];

        if (direction.equals(Direction.LEFT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(i + snakeWidth, snakeHeight, direction));
            }
        } else if (direction.equals(Direction.RIGHT)) {
            for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
                body.add(new SnakeSegment(snakeWidth - i, snakeHeight, direction));
            }
        }

        Snake snake = Snake.builder().body(body).dead(false)
                .walls(walls).build();
        snake.setHeadNodeDirection(direction);

        for (SnakeSegment sp : snake.getBody()) {
            appendGuiLayer(sp.getX(), sp.getY(), GuiElements.SNAKE.getSign(), Terminal.Color.CYAN);
        }

        return snake;
    }

    public static void main(String[] args) {

        String filename = "Field1.txt";
        String filename2 = "FieldWalls.txt";
        new CreateGameField(filename);

    }
}
