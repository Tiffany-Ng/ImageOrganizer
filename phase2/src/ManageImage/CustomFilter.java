package ManageImage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * CustomFilter is a filter that can be customized by its caller
 */
public class CustomFilter implements FilterStrategy{

    /**
     * Apply a normal filter onto the image
     * @param imageView the image
     * @return the image with filter
     */
    @Override
    public ImageView applyFilter(ImageView imageView) {
        // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207

        ColorAdjust colorAdjust = new ColorAdjust();
        imageView.setEffect(colorAdjust);

        return imageView;
    }

    /**
     * Apply a custom filter onto the image
     * @param imageView the image
     * @param brightness the brightness of the image. Max 1, min -1
     * @param contrast the contrast of the image. Max 1, min -1
     * @param hue the hue of the image. Max 1, min -1
     * @param saturation the saturation of the image. Max 1, min -1
     * @return the image with filter
     */
    public ImageView applyFilter(ImageView imageView, double brightness, double contrast, double hue, double saturation) {
        ColorAdjust colorAdjust = new ColorAdjust(hue, saturation, brightness, contrast);
        imageView.setEffect(colorAdjust);

        return imageView;
    }
}
