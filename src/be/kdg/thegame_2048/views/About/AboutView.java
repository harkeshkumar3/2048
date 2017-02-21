package be.kdg.thegame_2048.views.About;

import be.kdg.thegame_2048.views.SuperView;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Bryan de Ridder
 * @version 1.0 17-02-17 12:11
 */
public class AboutView extends BorderPane {
    private static final ImageView IMG_ABOUT1 = new ImageView("be/kdg/thegame_2048/views/img/about1.png");
    private static final ImageView IMG_ABOUT2 = new ImageView("be/kdg/thegame_2048/views/img/about2.png");
    private static final ImageView IMG_ABOUT3 = new ImageView("be/kdg/thegame_2048/views/img/about3.png");
    private static final double OVERALL_PADDING = 50;
    private Label lblExplanation1;
    private Label lblExplanation2;
    private Label lblExplanation3;
    private Button btnGoBack;
    private ToggleButton rbOption1;
    private ToggleButton rbOption2;
    private ToggleButton rbOption3;
    private Label lblHeader;

    public AboutView() {
        initialiseNodes();
        layoutNodes();
    }

    protected void initialiseNodes() {
        //header
        this.lblHeader = new Label("How to play");

        //explanation
        this.lblExplanation1 = new Label("Use your arrow keys to move the tiles. \n" +
                "Try to combine blocks with the same value.");
        this.lblExplanation2 = new Label("When two tiles with the same number touch,\n they merge into one!");
        this.lblExplanation3 = new Label("The goal of the game is to merge 1024 and 1024 " +
                "\nto get the final block: 2048. Good luck!");
        this.lblExplanation1.setAlignment(Pos.CENTER);
        this.lblExplanation2.setAlignment(Pos.CENTER);
        this.lblExplanation3.setAlignment(Pos.CENTER);
        this.btnGoBack = new Button();

        //radiobuttons
        rbOption1 = new ToggleButton();
        rbOption2 = new ToggleButton();
        rbOption3 = new ToggleButton();
        ToggleGroup options = new ToggleGroup();
        rbOption1.setToggleGroup(options);
        rbOption2.setToggleGroup(options);
        rbOption3.setToggleGroup(options);
        options.selectToggle(rbOption1);
        addStyles();
    }

    protected void layoutNodes() {
        this.setTop(new BorderPane(lblHeader));
        HBox hbox = new HBox(rbOption1, rbOption2, rbOption3);
        VBox vbox = new VBox(IMG_ABOUT1, lblExplanation1, hbox);
        vbox.setPadding(new Insets(50,0,0,0));
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        this.setCenter(vbox);
        BorderPane bottom = new BorderPane(btnGoBack);
        this.setBottom(bottom);
        this.setPadding(new Insets(OVERALL_PADDING));
    }

    void show2ndSlide() {
        HBox hbox = new HBox(rbOption1, rbOption2, rbOption3);
        VBox vbox = new VBox(IMG_ABOUT2, lblExplanation2, hbox);
        vbox.setPadding(new Insets(50,0,0,0));
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        this.setCenter(vbox);
    }

    void show3rdSlide() {
        HBox hbox = new HBox(rbOption1, rbOption2, rbOption3);
        VBox vbox = new VBox(IMG_ABOUT3, lblExplanation3, hbox);
        vbox.setPadding(new Insets(50,0,0,0));
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        this.setCenter(vbox);
    }

    private void addStyles() {
        btnGoBack.getStyleClass().add("btnGoBack");
        lblHeader.getStyleClass().add("lblHeader");
        lblExplanation1.getStyleClass().add("lblExplanation");
        lblExplanation2.getStyleClass().add("lblExplanation");
        lblExplanation3.getStyleClass().add("lblExplanation");
    }

    Button getBtnGoBack() {
        return btnGoBack;
    }

    ToggleButton getRbOption1() {
        return rbOption1;
    }

    ToggleButton getRbOption2() {
        return rbOption2;
    }

    ToggleButton getRbOption3() {
        return rbOption3;
    }
}