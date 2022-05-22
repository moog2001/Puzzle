package com.example.puzzle;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Cell extends Pane {
    private int x;
    private int y;
    private Image image;
    private ImageView imageView;
    private GridPane gridPane;
    private boolean hasImage;
    Image temp = generateImage(200, 200, 0, 0, 0, 1);


    public Cell(int x, int y, Image image, GridPane gridPaneInput) {
        this.x = x;
        this.y = y;
        this.gridPane = gridPaneInput;

        if (image != null) {
            hasImage = true;
            this.image = image;
            imageView = new ImageView(image);
        } else {
            imageView = new ImageView();
            imageView.setImage(temp);
            hasImage = false;
        }
        imageView.prefHeight(200);
        imageView.prefWidth(200);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);


        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (hasImage) {
                    /* drag was detected, start drag-and-drop gesture*/
                    /* allow any transfer mode */
                    Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                    /* put a string on dragboard */
                    ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(x) + String.valueOf(y));
                    content.putImage(imageView.getImage());
                    db.setContent(content);
                }
                event.consume();
            }
        });

        //target
        imageView.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (!hasImage) {
                    /* data is dragged over the target */
                    /* accept it only if it is  not dragged from the same node
                     * and if it has a string data */
                    if (event.getGestureSource() != imageView &&
                            event.getDragboard().hasImage()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                event.consume();

            }
        });

        imageView.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (!hasImage) {

                    /* data dropped */
                    /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    String location = db.getString();
                    int xSource = Character.getNumericValue(location.charAt(0));
                    int ySource = Character.getNumericValue(location.charAt(1));
                    boolean success = false;
                    if (isAdjacent(x, y, xSource, ySource)) {
                        if (db.hasImage()) {
                            imageView.setImage(db.getImage());
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        hasImage = true;
                        event.setDropCompleted(success);
                    }
                }
                event.consume();
            }
        });

        imageView.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    imageView.setImage(temp);
                    hasImage = false;
                }
                event.consume();
            }
        });

//        //target
//        imageView.setOnDragEntered(new EventHandler<DragEvent>() {
//            public void handle(DragEvent event) {
//                if (!hasImage) {
//                    /* the drag-and-drop gesture entered the target */
//                    System.out.println("onDragEntered");
//                    /* show to the user that it is an actual gesture target */
//                    if (event.getGestureSource() != imageView &&
//                            event.getDragboard().hasImage()) {
//                        imageView.setImage(event.getDragboard().getImage());
//                    }
//                }
//                event.consume();
//            }
//        });


        this.getChildren().add(imageView);
        imageView.setStyle("-fx-background-color: RED");
        this.setStyle("-fx-background-color: GRAY");

        this.gridPane.add(this, x, y);
    }

//    public boolean assign(){
//    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public Image generateImage(int width, int height, double red, double green, double blue, double opacity) {
        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();

        // Should really verify 0.0 <= red, green, blue, opacity <= 1.0
        int alpha = (int) (opacity * 255);
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);

        int pixel = (alpha << 24) | (r << 16) | (g << 8) | b;
        int[] pixels = new int[width * height];
        Arrays.fill(pixels, pixel);

        pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
        return img;
    }

    private boolean isAdjacent(int x1Input, int y1Input, int x2Input, int y2Input) {
        int x1 = x1Input;
        int y1 = y1Input;
        int x2 = x2Input;
        int y2 = y2Input;
        boolean checkOne = false;
        if ((x1 == x2) && (y1 - 1 == y2) || (x1 == x2) && (y1 + 1 == y2) || (x1 - 1 == x2) && (y1 == y2) || (x1 + 1 == x2) && (y1 == y2)) {
            checkOne = true;
        }
        x1 = x2Input;
        x2 = x1Input;
        y1 = y2Input;
        y2 = y1Input;
        boolean checkTwo = false;
        if ((x1 == x2) && (y1 - 1 == y2) || (x1 == x2) && (y1 + 1 == y2) || (x1 - 1 == x2) && (y1 == y2) || (x1 + 1 == x2) && (y1 == y2)) {
            checkTwo = true;
        }
        if (!checkOne && !checkTwo) {
            return false;
        }
        return true;

    }
}
