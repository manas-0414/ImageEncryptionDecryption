import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BinaryToImage {

    private static final int BIT_LENGTH = 24; // Adjust the bit length as needed
    private static final int MAX_DIMENSION = 65500;

    public static void main(String[] args) {
        String inputTextPath = "C:\\study\\Minor Project Code\\2nd-Image\\output.txt";
        String outputImagePath = "C:\\study\\Minor Project Code\\2nd-Image\\Sample_compressed.jpg";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputTextPath))) {
            String line;
            StringBuilder binaryData = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                binaryData.append(line);
            }

            int width = binaryData.length() / BIT_LENGTH;
            int height = 1; // Assuming a single row of pixels in the text file

            // Resize the image if dimensions exceed the maximum
            if (width > MAX_DIMENSION) {
                height = (int) Math.ceil((double) width / MAX_DIMENSION);
                width = MAX_DIMENSION;
            }

            BufferedImage reconstructedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                String binary = binaryData.substring(x * BIT_LENGTH, (x + 1) * BIT_LENGTH);
                int rgb = Integer.parseInt(binary, 2);
                reconstructedImage.setRGB(x, 0, rgb);
            }

            // Resize the image if needed
            if (height > 1) {
                Image resizedImage = reconstructedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedResizedImage.createGraphics();
                g2d.drawImage(resizedImage, 0, 0, null);
                g2d.dispose();
                reconstructedImage = bufferedResizedImage;
            }

            // Save the reconstructed image
            ImageIO.write(reconstructedImage, "jpg", new File(outputImagePath));

            System.out.println("Binary to Image conversion successful. Output saved to: " + outputImagePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
