package ManageImage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

// Adapted from: https://dzone.com/articles/design-patterns-strategy Date: Nov 21, 207
public interface FilterStrategy {
    ImageView applyFilter(ImageView image);
}

