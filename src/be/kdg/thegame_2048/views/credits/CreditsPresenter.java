package be.kdg.thegame_2048.views.credits;

import be.kdg.thegame_2048.models.PlayerManager;
import be.kdg.thegame_2048.views.start.StartPresenter;
import be.kdg.thegame_2048.views.start.StartView;

/**
 * Logical handling of the go back button on the creditsView
 *
 * @author Jarne van Aerde, Bryan de Ridder
 * @version 1.0 12/02/2017 19:40
 */
public class CreditsPresenter {
    private final PlayerManager model;
    private final CreditsView view;

    public CreditsPresenter(PlayerManager model, CreditsView view) {
        this.model = model;
        this.view = view;
        this.addEventHandlers();
    }

    private void addEventHandlers() {
        view.getBtnGoBack().setOnAction(event -> {
            StartView startView = new StartView();
            new StartPresenter(model, startView);
            view.getScene().setRoot(startView);
        });
    }
}