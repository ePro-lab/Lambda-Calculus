package app.gui;

import app.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import operation.BetaReduction;

import java.io.*;

public class Gui extends Application {

    private Accordion tmp;
    private int helpCounter = 0;

    public static void startGui(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
        stage.setTitle("Lambda Calculus");
        createWindow(stage);
        stage.show();
    }

    private void createWindow(Stage stage){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.TOP_CENTER);

        //types used
        Label label;
        Label time = new Label("time:");
        TextField textField;
        Button button;
        Accordion accordion = new Accordion();

        //row 1
        label = new Label("Input");
        gridPane.add(label,0,0);

        textField = new TextField();
        textField.setOnAction( e -> {
            long start = System.currentTimeMillis();
            accordion.getPanes().clear();
            BetaReduction.resetCount();
            try {
                tmp = Main.compute(textField.getText());
            }catch (Exception ignored) {}
            if(!tmp.getPanes().isEmpty())
                accordion.getPanes().add(tmp.getPanes().remove(0));
            long elapsedTime = System.currentTimeMillis() - start;
            time.setText("time: "+elapsedTime +"ms");
        });
        gridPane.add(textField,1,0);
        GridPane.setColumnSpan(textField,3);

        button = new Button("start");
        button.setOnAction(e -> {
            long start = System.currentTimeMillis();
            accordion.getPanes().clear();
            BetaReduction.resetCount();
            try {
                tmp = Main.compute(textField.getText());
            }catch (Exception ignored) {}
            if(!tmp.getPanes().isEmpty())
                accordion.getPanes().add(tmp.getPanes().remove(0));
            long elapsedTime = System.currentTimeMillis() - start;
            time.setText("time: "+elapsedTime +"ms");
        });
        gridPane.add(button,4,0);

        //row 2
        label = new Label("output");
        gridPane.add(label,0,1);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(accordion);
        scrollPane.setFitToWidth(true);
        gridPane.add(scrollPane,1,1);
        GridPane.setColumnSpan(scrollPane,3);
        GridPane.setRowSpan(scrollPane, 4);

        button = new Button("next");
        button.setOnAction(e -> {
            if(tmp != null && !tmp.getPanes().isEmpty())
                accordion.getPanes().add(tmp.getPanes().remove(0));
        });
        gridPane.add(button,4,1);

        button = new Button("end");
        button.setOnAction(e -> {
            if(tmp != null && !tmp.getPanes().isEmpty()) {
                accordion.getPanes().addAll(tmp.getPanes());
                tmp.getPanes().clear();
            }
        });
        gridPane.add(button,4,2);

        button = new Button("help");
        button.setOnAction(e-> {
            if(helpCounter == 0) {
                helpCounter++;
                StringBuilder help = new StringBuilder();
                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("help.dat");
                    assert is != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null)
                        help.append(line).append("\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Text text = new Text(help.toString());

                ScrollPane scrollPane2 = new ScrollPane();
                scrollPane2.setContent(text);
                Scene secondScene = new Scene(scrollPane2, 500, 300);
                Stage secondStage = new Stage();
                secondStage.setTitle("Help");
                secondStage.setScene(secondScene);
                secondStage.setOnCloseRequest(e2-> helpCounter--);

                secondStage.show();
            }
        });
        gridPane.add(button,4,3);

        //row 3
        //add label time
        gridPane.add(time,0,2);

        Scene scene = new Scene(gridPane,600,250);
        stage.setScene(scene);
    }
}
