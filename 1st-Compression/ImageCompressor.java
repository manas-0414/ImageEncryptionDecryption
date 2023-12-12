import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageCompressor {

    private static final long MAX_FILE_SIZE_BYTES = 500 * 1024; // 500KB

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputImagePath = getUserInput("Enter the path to the input image: ", scanner);
        String outputFolderPath = getUserInput("Enter the path to the output folder: ", scanner);
        scanner.close();

        try {
            compressImage(inputImagePath, outputFolderPath);
            System.out.println("Image compression successful.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // Trim whitespaces
    }

    private static void compressImage(String inputImagePath, String outputFolderPath) throws IOException {
        File inputFile = new File(inputImagePath);

        if (!isValidInputFile(inputFile)) {
            throw new IOException(
                    "Invalid input file or unsupported image format. File: " + inputFile.getAbsolutePath());
        }

        if (inputFile.length() > MAX_FILE_SIZE_BYTES) {
            throw new IOException("Input file size exceeds 500KB. File: " + inputFile.getAbsolutePath());
        }

        BufferedImage originalImage = ImageIO.read(inputFile);

        // Extracting file name without extension
        String fileNameWithoutExtension = getFileNameWithoutExtension(inputFile.getName());

        // Constructing the output file path in the specified folder with a new file
        // name and the ".jpg" extension
        String outputFilePath = outputFolderPath + File.separator + fileNameWithoutExtension + "_compressed.jpg";

        // Creating the output folder if it doesn't exist
        createOutputFolder(outputFolderPath);

        // Using Paths to ensure proper file path handling
        Path outputPath = Paths.get(outputFilePath);

        ImageIO.write(originalImage, "jpg", outputPath.toFile());
    }

    private static boolean isValidInputFile(File file) {
        String fileName = file.getName().toLowerCase();
        return file.isFile() && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"));
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private static void createOutputFolder(String outputFolderPath) throws IOException {
        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            Files.createDirectories(outputFolder.toPath());
        }
    }
}