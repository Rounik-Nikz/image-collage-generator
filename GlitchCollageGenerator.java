import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlitchCollageGenerator {

    public static void main(String[] args) {
        try {
            // 👇 Load all the dataset images from the 'samples' folder
            File samplesFolder = new File("small-images-data");
            BufferedImage[] datasetImages = loadDatasetImages(samplesFolder);

            if (datasetImages == null || datasetImages.length == 0) {
                System.err.println("Error: No images found in the 'samples' folder. Exiting.");
                return;
            }

            // 💡 Size of the final collage
            int canvasWidth = 1024;
            int canvasHeight = 1024;

            // 🎨 Create a blank canvas
            BufferedImage collage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = collage.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, canvasWidth, canvasHeight); // Optional background

            // 🎲 Scatter image fragments
            Random rand = new Random();
            int numberOfFragments = 2000; // Adjust for more/fewer fragments
            int fragmentSize = 32;     // Size of each fragment

            for (int i = 0; i < numberOfFragments; i++) {
                // Pick a random image from the loaded dataset
                BufferedImage img = datasetImages[rand.nextInt(datasetImages.length)];

                if (img.getWidth() < fragmentSize || img.getHeight() < fragmentSize) {
                    continue; // Skip if the image is smaller than the fragment size
                }

                // Get a random crop from the chosen image
                int srcX = rand.nextInt(img.getWidth() - fragmentSize);
                int srcY = rand.nextInt(img.getHeight() - fragmentSize);
                BufferedImage chunk = img.getSubimage(srcX, srcY, fragmentSize, fragmentSize);

                // Random position to paste on canvas
                int destX = rand.nextInt(canvasWidth - fragmentSize);
                int destY = rand.nextInt(canvasHeight - fragmentSize);

                g2d.drawImage(chunk, destX, destY, null);
            }

            g2d.dispose();

            // 💾 Save the collage image
            File outputfile = new File("glitch_collage.png");
            ImageIO.write(collage, "png", outputfile);
            System.out.println("✅ Collage image saved as: " + outputfile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🧠 Load all PNG images from the specified folder
    private static BufferedImage[] loadDatasetImages(File folder) {
        List<BufferedImage> imagesList = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                    try {
                        BufferedImage img = ImageIO.read(file);
                        if (img != null) {
                            imagesList.add(img);
                        } else {
                            System.err.println("⚠️ Could not read image file: " + file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.err.println("⚠️ Error loading image: " + file.getAbsolutePath() + " - " + e.getMessage());
                    }
                }
            }
        } else {
            System.err.println("⚠️ Error accessing the folder: " + folder.getAbsolutePath());
        }

        return imagesList.toArray(new BufferedImage[0]);
    }
}