package application;

import java.util.function.Function;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class FXUtilities {

    /**
     * Given a list view of type T and a function that takes an obj. of type T
     * and returns a string, set the text of each cell to the result of the
     * function call on each cell's content.
     * 
     * @param listView
     * @param func
     * @author matthewriley
     */
    public static <T> void setCelllNames(ListView<T> listView,
            Function<T, String> func) {

        listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
            public ListCell<T> call(ListView<T> param) {
                final ListCell<T> cell = new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(func.apply(item));
                        }
                    }
                };
                return cell;
            }
        });
    }
}
