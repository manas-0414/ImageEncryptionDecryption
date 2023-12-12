import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class ImageDecompressor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String inputImagePath = getUserInput("Enter the path to the compressed image: ", scanner);
        String outputFolderPath = getUserInput("Enter the path to the output folder: ", scanner);

        try {
            decompressImage(inputImagePath, outputFolderPath);
            System.out.println("Image decompression successful.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner in the finally block
        }
    }

    private static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void decompressImage(String inputImagePath, String outputFolderPath) throws IOException {
        File compressedFile = new File(inputImagePath);

        if (!isValidInputFile(compressedFile)) {
            throw new IOException(
                    "Invalid input file or unsupported image format. File: " + compressedFile.getAbsolutePath());
        }

        BufferedImage compressedImage = ImageIO.read(compressedFile);

        // Extracting file name without extension
        String fileNameWithoutExtension = getFileNameWithoutExtension(compressedFile.getName());

        // Constructing the output file path in the specified folder with the original
        // file name and extension
        String outputFilePath = outputFolderPath + File.separator + fileNameWithoutExtension + "_decompressed.jpg";

        // Creating the output folder if it doesn't exist
        createOutputFolder(outputFolderPath);

        // Using Paths to ensure proper file path handling
        Path outputPath = Paths.get(outputFilePath);

        ImageIO.write(compressedImage, "jpg", outputPath.toFile());
    }

    private static boolean isValidInputFile(File file) {
        String fileName = file.getName().toLowerCase();
        return file.isFile() && fileName.endsWith("_compressed.jpg");
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int underscoreIndex = fileName.lastIndexOf('_');
        int dotIndex = fileName.lastIndexOf('.');
        return (underscoreIndex == -1) ? fileName
                : fileName.substring(0, underscoreIndex) + fileName.substring(dotIndex);
    }

    private static void createOutputFolder(String outputFolderPath) throws IOException {
        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            Files.createDirectories(outputFolder.toPath());
        }
    }
}
