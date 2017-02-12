package be.kdg.thegame_2048.views.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Bryan de Ridder
 * @version 1.0 08-02-17 17:03
 */
public class NewPlayerView extends BorderPane {
    private static final double OVERALL_PADDING = 50;
    private ImageView logo;
    private Label lblNewPlayer;
    private TextField tfNewPlayer;
    private Label nameExistsError;
    private Button goBack;

    public NewPlayerView() {
        initialiseNodes();
        layoutNodes();
    }

    private void initialiseNodes() {
        //Same as in the StartView class, maybe put inside an interface
        this.logo = new ImageView("be/kdg/thegame_2048/views/views/img/logo.png");

        //NewPlayerView
        this.lblNewPlayer = new Label("What's your name?");
        lblNewPlayer.setPadding(new Insets(0,0,OVERALL_PADDING/2,0));

        //Textfield is set up
        this.tfNewPlayer = new TextField();
        tfNewPlayer.setPrefHeight(40);
        tfNewPlayer.setMaxWidth(210);
        tfNewPlayer.getStyleClass().add("tfPlayer");

        //Optional error message, should be invisible by default
        this.nameExistsError = new Label("Sorry, this name already exists :(");
        nameExistsError.setPadding(new Insets(OVERALL_PADDING/10,0,-OVERALL_PADDING/10,0));
        nameExistsError.setVisible(false);
        nameExistsError.getStyleClass().add("inputError");

        //back button
        goBack = new Button();
        goBack.setGraphic(new ImageView("be/kdg/thegame_2048/views/views/img/left-arrow.png"));
        goBack.getStyleClass().add("backButton");
    }

    private void layoutNodes() {
        //Same as in the StartView class, maybe put inside an interface
        BorderPane top = new BorderPane();
        top.setCenter(logo);
        top.setPadding(new Insets(0, 0, OVERALL_PADDING*2, 0));
        this.setTop(top);
        this.setPadding(new Insets(OVERALL_PADDING));
        this.setMaxWidth(450);

        //NewPlayerView:
        VBox middle = new VBox(lblNewPlayer,tfNewPlayer, nameExistsError, goBack);
        middle.setAlignment(Pos.TOP_CENTER);
        this.setCenter(middle);
    }
}