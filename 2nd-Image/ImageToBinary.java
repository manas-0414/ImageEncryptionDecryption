import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageToBinary {

    private static final int BIT_LENGTH = 24; // Adjust the bit length as needed

    public static void main(String[] args) {
        String inputImagePath = "C:\\study\\Minor Project Code\\2nd-Image\\Sample_compressed.jpg";
        String outputDirectoryPath = "C:\\study\\Minor Project Code\\2nd-Image";
        String outputTextPath = outputDirectoryPath + File.separator + "output.txt";

        try {
            BufferedImage image = ImageIO.read(new File(inputImagePath));

            if (image == null) {
                System.out.println("Error: Unable to read the image from the specified path.");
                return;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            try (FileWriter textWriter = new FileWriter(outputTextPath)) {
                StringBuilder binaryStringBuilder = new StringBuilder();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        String binary = Integer.toBinaryString(rgb & 0xFFFFFF); // Mask to get only 24 bits
                        binary = String.format("%" + BIT_LENGTH + "s", binary).replace(' ', '0'); // Left-pad with zeros

                        // Append the binary data to the StringBuilder
                        binaryStringBuilder.append(binary);
                    }
                    // Append a newline after each row
                    binaryStringBuilder.append(System.lineSeparator());
                }

                // Write the entire binary data to the text file
                textWriter.write(binaryStringBuilder.toString());

                System.out.println("Image to Binary conversion successful. Output saved to: " + outputTextPath);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
