package ManageImage;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

/**
 * Gives a user the ability to view an inverted colour version of an image.
 */
public class InvertColoursFilter implements FilterStrategy{

    /**
     * Apply a filter which inverts the original colours of an image
     *
     * @param imageView view of an image before an filter implementation
     * @return view of an image after applying the filter
     */
    // adapted from http://java-buddy.blogspot.ca/2012/12/javafx-example-copy-image-pixel-by.html
    @Override
    public ImageView applyFilter(ImageView imageView) {

        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color.invert());

            }
        }

        imageView.setImage(writableImage);

        return imageView;

    }
}
