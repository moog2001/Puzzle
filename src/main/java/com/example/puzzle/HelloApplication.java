package com.example.puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HelloApplication extends Application {

    static final int x = 4;
    static final int y = 4;
    static final int xMax = 800;
    static final int yMax = 800;
    Cell[][] cells = new Cell[x][y];


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        stage.setTitle("Puzzle!");

        VBox root = new VBox();
        Scene scene = new Scene(root, yMax, xMax);
        Image imgMario = new Image("mario.jpg", xMax, yMax, true, true);
        GridPane gridPane = returnGrid(imgMario);
        root.getChildren().add(gridPane);
//        root.getChildren().add(imageView);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private GridPane returnGrid(Image imageInput) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        ArrayList<Image> listImage = new ArrayList<Image>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                PixelReader reader = imageInput.getPixelReader();
                int size = xMax / x;
                WritableImage newImage = new WritableImage(reader, i * size, j * size, size, size);
                listImage.add(newImage);
            }
        }

        Collections.shuffle(listImage);

        int index = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (i == x - 1 && j == y - 1) {
                    cells[i][j] = new Cell(i, j, null, gridPane);

                } else {
                    cells[i][j] = new Cell(i, j, listImage.get(index), gridPane);
                    index++;
                }
            }
        }


        return gridPane;
    }

    ;

    public static void main(String[] args) {
        launch();
    }
}