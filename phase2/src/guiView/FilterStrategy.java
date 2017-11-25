package guiView;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

// Adapted from: https://dzone.com/articles/design-patterns-strategy Date: Nov 21, 207
interface FilterStrategy {
    ImageView applyFilter(ImageView image);
}

class BlackAndWhiteFilter implements FilterStrategy{
    @Override
    public ImageView applyFilter(ImageView imageView) {
        // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        imageView.setEffect(colorAdjust);

        return imageView;
    }
}

class NormalFilter implements FilterStrategy{
    @Override
    public ImageView applyFilter(ImageView imageView) {
        imageView.setEffect(null);
        return imageView;
    }
}

class InvertColoursFilter implements FilterStrategy{

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
