package Engine;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadImageAsRGB(String filePath) throws IOException {
        // Load the original image
        BufferedImage originalImage = ImageIO.read(new File(filePath));

        // Convert it to TYPE_INT_RGB if needed
        if (originalImage.getType() != BufferedImage.TYPE_INT_RGB) {
            BufferedImage convertedImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            // Draw the original image onto the new image
            Graphics2D g = convertedImage.createGraphics();

            g.drawImage(originalImage, 0, 0, null);
            g.dispose();

            return convertedImage; // Return the converted image
        }

        // If the original image is already TYPE_INT_RGB, return it as-is
        return originalImage;
    }
}
