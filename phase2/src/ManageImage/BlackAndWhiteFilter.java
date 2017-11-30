package ManageImage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * Gives a user the ability to view a black & white version of the image
 */
public class BlackAndWhiteFilter implements FilterStrategy{

    /**
     * Convert an image into it's black & white version.
     *
     * @param imageView view of an image before an filter implementation
     */
    @Override
    public void applyFilter(ImageView imageView) {
        // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        imageView.setEffect(colorAdjust);

    }
}
