package ManageImage;

import javafx.scene.image.*;

// Adapted from: https://dzone.com/articles/design-patterns-strategy Date: Nov 21, 207
public interface FilterStrategy {
    ImageView applyFilter(ImageView image);
}

