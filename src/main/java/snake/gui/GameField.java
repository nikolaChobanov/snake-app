package snake.gui;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.player.CurrentState;
import snake.player.Direction;
import snake.player.SegmentPlacement;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameField {

    //30
    private static int fieldWidth;

    //10
    private static int fieldHeight;

    private static SwingTerminal terminal;

    private static Screen screen;

    private CurrentState currentState;

    private Snake snake;

    private Elements elements;

    private ArrayList<String> field;

    private HashSet<SegmentPlacement> walls;


    private GameField(String fieldFileName) {

        walls = new HashSet<>();
        field = new ArrayList<>();
        constructField(fieldFileName);

    }

    private void createGuiScreen() {

        elements = new Elements();
        terminal = new SwingTerminal(fieldWidth, fieldHeight);
        screen = new Screen(terminal);
        //   screen.setCursorPosition(null);
        screen.startScreen();
    }

    private void keyboardInput() {
        Key k = readInput();

        if (k != null) {
            switch (k.getKind()) {
                case ArrowUp:
                    currentState.setDirection(Direction.UP);
                    break;

                case ArrowDown:
                    currentState.setDirection(Direction.DOWN);

            }
        }
    }

    private Key readInput() {
        return terminal.readInput();
    }


    private File accessFile(String fileName) {
        return new File(

                Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile()

        );

    }

    private void markWalls(String line, int currHeight) {
        int widthCounter = 0;
        String[] splitStr = line.split("");
        for (String ch : splitStr) {

            if (ch.equals(Elements.wallElement)) {
                walls.add(new SegmentPlacement(widthCounter, currHeight));
            }
            widthCounter++;
        }

    }


    private void constructField(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(accessFile(fileName)))) {

            //StringBuffer sb = new StringBuffer();

            fieldWidth = Integer.parseInt(br.readLine());
            fieldHeight = Integer.parseInt(br.readLine());

            createGuiScreen();

            String line;
            int heightCounter = 0;

            while ((line = br.readLine()) != null) {
                markWalls(line, heightCounter);
                field.add(line);
                drawString(0, heightCounter++, line, Terminal.Color.CYAN);
            }

            for (String str : field) {
                System.out.println(str);
            }

            //   screen.readInput();
            findSnakePlacement();
            screen.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawString(int x, int y, String string, Terminal.Color color) {
        screen.putString(x, y, string, color, null);
    }

    private boolean isWall(int x, int y) {
        return walls.contains(new SegmentPlacement(x, y));
    }

    private void findSnakePlacement() {
        //SegmentPlacement start = snake.getBody().getLast();
        int snakeHeight;
        int snakeWidth;

        do {
            snakeHeight = ThreadLocalRandom.current().nextInt(0, fieldHeight);
            snakeWidth = ThreadLocalRandom.current().nextInt(0, fieldWidth);
        } while (isWall(snakeWidth, snakeHeight) || isWall(snakeWidth + 1, snakeHeight) || isWall(snakeWidth + 2, snakeHeight));

        createSnake(snakeHeight, snakeWidth);

    }

    private void createSnake(int snakeHeight, int snakeWidth) {

        LinkedList<SegmentPlacement> body = new LinkedList<>();

        for (int i = 0; i < Snake.getSnakeInitialSize(); i++) {
            body.add(new SegmentPlacement(i + snakeWidth, snakeHeight));
        }

        snake = Snake.builder().fieldWidth(fieldWidth).fieldHeight(fieldHeight).body(body).dead(false)
                .direction(Direction.values()[new Random().nextInt(Direction.values().length)]).build();

        for (SegmentPlacement sp : snake.getBody()) {
            drawString(sp.getX(), sp.getY(), elements.getSnakeElement(), Terminal.Color.BLUE);
        }

    }

    public static void main(String[] args) {

        //String filename = "Field1.txt";
        String filename2 = "FieldWalls.txt";

        GameField field = new GameField(filename2);


    }
}
