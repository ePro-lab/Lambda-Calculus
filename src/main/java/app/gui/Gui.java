package app.gui;

import app.Main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Gui extends Application {

    private Accordion tmp;

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
        TextField textField;
        Button button;
        Accordion accordion = new Accordion();
        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "all", "1", "2"));

        //row 1
        label = new Label("Input");
        gridPane.add(label,0,0);

        textField = new TextField();
        textField.setOnAction( e -> {
            accordion.getPanes().clear();
            if(choiceBox.getSelectionModel().getSelectedItem().equals("all"))
                accordion.getPanes().addAll(Main.compute(textField.getText()).getPanes());
            else
                compute(choiceBox,accordion,textField);

        });
        gridPane.add(textField,1,0);
        GridPane.setColumnSpan(textField,3);

        button = new Button("start");
        button.setOnAction(e -> {
            accordion.getPanes().clear();
            if(choiceBox.getSelectionModel().getSelectedItem().equals("all"))
                accordion.getPanes().addAll(Main.compute(textField.getText()).getPanes());
            else
                compute(choiceBox,accordion,textField);
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
        GridPane.setRowSpan(scrollPane, 3);

        button = new Button("next");
        button.setOnAction(e -> {
            if(choiceBox.getSelectionModel().getSelectedItem().equals("all"))
                accordion.getPanes().addAll(tmp.getPanes());
            else {
                int next = Integer.parseInt(choiceBox.getSelectionModel().getSelectedItem());
                System.out.println("##########################" + tmp.getPanes().size() + " ###### " + next);
                for (int i=0; i<tmp.getPanes().size() && i<next; i++)
                    accordion.getPanes().add(tmp.getPanes().remove(0));
            }
        });
        gridPane.add(button,4,1);

        choiceBox.setValue("all");
        gridPane.add(choiceBox,5,1);



        Scene scene = new Scene(gridPane,600,250);
        stage.setScene(scene);
    }

    private void compute(ChoiceBox<String> choiceBox, Accordion accordion, TextField textField){
            int next = Integer.parseInt(choiceBox.getSelectionModel().getSelectedItem());
            Accordion tmp = Main.compute(textField.getText());
            for(int i=0; i<tmp.getPanes().size() && i<next; i++)
                    accordion.getPanes().add(tmp.getPanes().remove(0));
            this.tmp = tmp;
    }

}
