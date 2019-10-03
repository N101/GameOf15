package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2 extends Application {
    private Btn[] tiles = new Btn[16];
    private int[][] numbers = new int[4][4];
    private final int gridW = 4;
    private boolean gameOver = false;
    private String MoveDir = "";

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Game of 15");
        newGame();
        Scene root = createTheScene();
        primaryStage.setScene(root);
        primaryStage.show();
    }

    private Scene createTheScene() {
        BorderPane bp = new BorderPane();
        bp.getStylesheets().add("/sample/StyleClass.css");
        bp.setTop(new Label("Game of 15"));

        Button btn = new Button("new game");
        btn.setPrefSize(100, 50);
        btn.setId("outside");
        bp.setLeft(btn);
        btn.setOnAction(e -> {
            newGame();
            repaintNumbers();
        });

        bp.setRight(new Label("Right"));
        bp.setCenter(gamePane());
        Scene windowScene = new Scene(bp, 700, 530);
        return windowScene;
    }

    private Node gamePane() {
        GridPane grid = new GridPane();
        grid.setId("borderPane");
        grid.setPrefSize(600, 600);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        int counter = 0;
//        Scene gameScene = new Scene(grid, 400, 400);

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {

                tiles[counter] = new Btn(j, i);
                tiles[counter].setId("tiles");
                tiles[counter].setText(String.valueOf(numbers[i][j]));
                tiles[counter].setMinSize(100, 100);

                int finalI = i;
                int finalJ = j;

                tiles[counter].setOnAction(e -> {
                    if (gameOver == true) {
                        new Alert(Alert.AlertType.ERROR, "Game is done already! Start a new game").showAndWait();
                    } else if (isMoveLegal(finalJ, finalI) == true) {
                        move(finalJ, finalI);
                        repaintNumbers();
                        System.out.println(isGameDone());
                    }
                });

                grid.add(tiles[counter], j, i);

                counter++;
            }
        }
        repaintNumbers();

        return grid;
    }

    public void newGame() {
//        do {  // continue shuffling the board until its a solvable design
        setBoard();
        printGrid();
        shuffleStart();
        printGrid();
        MoveDir = "";

//        } while (!isSolvable());
        gameOver = false;

//        startTimer();

    }

    private void setBoard() {
        int counter = 1;
        for (int i = 0; i <= gridW - 1; i++) {
            for (int j = 0; j <= gridW - 1; j++) {
                numbers[i][j] = counter;
                counter++;

            }
        }
        numbers[3][3] = -1;
    }

    private void printGrid() {
        String word = "";
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                word += numbers[i][j] + ", ";
            }
        }
        System.out.println(word);
    }

    private void shuffleStart() {
        Random ran = new Random();

        for (int i = numbers.length - 1; i > 0; i--) {
            for (int k = numbers[i].length - 1; k > 0; k--) {
                int m = ran.nextInt(i + 1);
                int n = ran.nextInt(k + 1);

                int tmp = numbers[i][k];
                numbers[i][k] = numbers[m][n];
                numbers[m][n] = tmp;
            }
        }
        if(!isSolvable()) {
            shuffleStart();
        }
    }

    private boolean isSolvable() {
        int Inversions = 0;
        int blankPos = 0;
        List<Integer> ary = new ArrayList<>();

        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                ary.add(numbers[i][j]);
                if (numbers[i][j] == -1) {
                    blankPos = 4 - i;
                }
            }
        }

        for (int a = 0; a < ary.size(); a++) {
            for (int b = a; b < ary.size(); b++) {
                if (ary.get(a) == -1 || ary.get(b) == -1) {
                } else if (ary.get(a) > ary.get(b)) {
                    Inversions++;
                }
            }
        }

        if (gridW % 2 == 0) {
            System.out.println((Inversions % 2 == 0) == (blankPos % 2 != 0));
            return (Inversions % 2 == 0) == (blankPos % 2 != 0);
//            if ((Inversions % 2 == 0) == (blankPos % 2 != 0)) {
//                return;
//            } else {
//                shuffleStart();
//            }
        } else {
            return Inversions % 2 == 0;
//            if (Inversions % 2 == 0) {
//                return ;
//            } else {
//                shuffleStart();
//            }
        }
    }

    public boolean isMoveLegal(int x, int y) {
        boolean check = false;

        // Check above
        if (y != 0 && numbers[y - 1][x] == -1) {
            check = true;
            MoveDir = "Up";
        }
        // Below
        if (y != 3 && numbers[y + 1][x] == -1) {
            check = true;
            MoveDir = "Down";
        }
        // Right
        if (x != 3 && numbers[y][x + 1] == -1) {
            check = true;
            MoveDir = "Right";
        }
        // Left
        if (x != 0 && numbers[y][x - 1] == -1) {
            check = true;
            MoveDir = "Left";
        }
        return check;
    }

    private void move(int x, int y) {
        int clickedNr = numbers[y][x];
        switch (MoveDir) {
            case "Up":
                numbers[y][x] = numbers[y - 1][x];
                numbers[y - 1][x] = clickedNr;
                break;
            case "Down":
                numbers[y][x] = numbers[y + 1][x];
                numbers[y + 1][x] = clickedNr;
                break;
            case "Right":
                numbers[y][x] = numbers[y][x + 1];
                numbers[y][x + 1] = clickedNr;
                break;
            case "Left":
                numbers[y][x] = numbers[y][x - 1];
                numbers[y][x - 1] = clickedNr;
                break;
        }
    }

    public boolean isGameDone() {
        List<Integer> tmpary = new ArrayList<>();
        boolean tmp = true;                         //starts as true since it is looking if anything is out of place

        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                tmpary.add(numbers[i][j]);
            }
        }
        for (int x = 0; x < tmpary.size() - 2; x++) {   //not looking at the last field (should be the empty tile)
            if (tmpary.get(x) > tmpary.get(x + 1)) {
                tmp = false;
            }
        }
        if (tmp) {
            gameOver = true;
            Alert a = new Alert(Alert.AlertType.INFORMATION);   //Victory message
            a.setContentText("YOU HAVE WON!!");
            a.setHeaderText(null);
            a.showAndWait();
        }
        return gameOver;
    }

    public void setStyle(String style, Btn btn) {
        if (style.equals("empty")) {
            btn.setStyle(null);
            btn.getStyleClass().clear();
            btn.getStyleClass().add("emptyBtn");
            btn.setStyle("emptyPos");
        } else if (style.equals("normal")) {
            btn.getStyleClass().clear();
            btn.getStyleClass().add("button");
        }
    }

    private void repaintNumbers() {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[counter].setText(String.valueOf(numbers[i][j]));
                if (numbers[i][j] == -1) {
                    tiles[counter].setId("emptyBtn");
                } else {
                    tiles[counter].setId("tiles");
                }
                counter++;
            }
        }
    }


}
