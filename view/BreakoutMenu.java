package breakout.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.List;

/*
   The menu for Breakout

   *** Nothing to do here ***

 */
public class BreakoutMenu extends MenuBar {

    private final Menu menuFile;
    private final Menu menuLevel;

    public BreakoutMenu(EventHandler<ActionEvent> menuHandler,
                        EventHandler<ActionEvent> optionsHandler) {

        menuFile = createMenuFile();
        menuLevel = createMenuLevel();

        // Connect event handlers to all menu items
        menuFile.getItems().forEach(item -> item.setOnAction(menuHandler));
        menuLevel.getItems().forEach(item -> item.setOnAction(optionsHandler));

        this.getMenus().addAll(menuFile, menuLevel);
    }

    public void fixMenusNewGame() {
        getItemByText(menuFile, "New").setDisable(true);
        getItemByText(menuFile, "Stop").setDisable(false);
        menuLevel.setDisable(true);
    }

    public void fixMenusKillGame() {
        getItemByText(menuFile, "New").setDisable(false);
        getItemByText(menuFile, "Stop").setDisable(true);
        menuLevel.setDisable(false);
    }

    // ------------  Helpers -----------------------------

    private MenuItem getItemByText(Menu menu, String text) {
        return menu.getItems()
                .filtered(i -> i
                        .getText()
                        .equals(text))
                .get(0);
    }

    private Menu createMenuFile() {
        Menu menuFile = new Menu("File");
        List<MenuItem> items = Arrays
                .asList(new MenuItem("New"),
                        new MenuItem("Stop"),
                        new MenuItem("Exit")
                );
        menuFile.getItems().addAll(items);
        return menuFile;
    }

    private Menu createMenuLevel() {
        Menu menuLevel = new Menu("Levels");
        ToggleGroup toggleGroup = new ToggleGroup();
        List<RadioMenuItem> items = Arrays
                .asList(new RadioMenuItem("1"),
                        new RadioMenuItem("2"),
                        new RadioMenuItem("3"),
                        new RadioMenuItem("4"),
                        new RadioMenuItem("5")
                );
        for (RadioMenuItem r : items) {
            r.setToggleGroup(toggleGroup);
        }
        items.get(0).setSelected(true);
        menuLevel.getItems().addAll(items);
        return menuLevel;
    }

}
