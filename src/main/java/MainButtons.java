import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class MainButtons {

    public static class ButtonsBuilder {
        private Button button;
        private double minHeight = 40;
        private double maxHeight = 40;
        private double prefHeight = 40;
        private double minWidth = 120;
        private double maxWidth = 120;
        private double prefWidth = 120;

        public ButtonsBuilder(String test) {
            button = new Button(test);
            button.setEffect(dropShadow());
            button.setMinHeight(minHeight);
            button.setMaxHeight(maxHeight);
            button.setPrefHeight(prefHeight);
            button.setMinWidth(minWidth);
            button.setMaxWidth(maxWidth);
            button.setPrefWidth(prefWidth);
            button.setEffect(dropShadow());
            button.setPadding(new Insets(10,10,10,10));
        }

        public ButtonsBuilder myTooltip(String tooltip) {
            button.setTooltip(new Tooltip(tooltip));
            return this;
        }

        public Button build (){
            return button;
        }

        private DropShadow dropShadow() {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
            dropShadow.setColor(Color.color(0.0353, 0.0667, 0.2471));
            return dropShadow;
        }

    }
}
