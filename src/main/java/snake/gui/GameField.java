package snake.gui;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import snake.player.CurrentState;
import snake.player.Direction;

import java.io.*;

public class GameField {

    private static int fieldWidth;

    private static int fieldHeight;

    private static SwingTerminal terminal;

    private static Screen screen;

    private CurrentState currentState;

    public GameField(int width, int height) {
        this.fieldWidth = width;
        this.fieldHeight = height;
        createField();
    }

    private void createField() {

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

    public void constructField(String fileName) {
        File file = new File(

                getClass().getClassLoader().getResource("Field1.txt").getFile()

        );


        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();


            String yMax = br.readLine();
            String xMax = br.readLine();
            String line;
            int x = 20;
            int y = 1;
            while ((line = br.readLine()) != null) {


                // String line=br.readLine();
                System.out.println(line);
                //screen.putString(10,30," " , null,null);

                drawString(x, y++, line, Terminal.Color.CYAN);
            }

            screen.refresh();
            screen.readInput();
            screen.refresh();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawString(int x, int y, String string, Terminal.Color color) {
        screen.putString(x, y, string, color, null);
    }


    public static void main(String args[]) {

        String filename = "Field1.txt";

        GameField field = new GameField(80, 30);
        field.constructField(filename);

    }
}
