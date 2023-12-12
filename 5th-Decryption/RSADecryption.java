import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class RSADecryption {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get input from the user
            String privateKeyPath = getUserInput("Enter the path to the private key file (.pem): ", scanner);
            String encryptedSymmetricKeyPath = getUserInput(
                    "Enter the path to the encrypted symmetric key file (.txt): ", scanner);
            String encryptedDataPath = getUserInput("Enter the path to the encrypted data file (.txt): ", scanner);
            String outputPath = getUserInput("Enter the path for the output (decrypted) data file: ", scanner);

            // Load private key
            PrivateKey privateKey = loadPrivateKeyFromPEM(privateKeyPath);

            // Load encrypted symmetric key
            byte[] encryptedSymmetricKey = Files.readAllBytes(Paths.get(encryptedSymmetricKeyPath));

            // Decrypt symmetric key using RSA private key
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedSymmetricKey = rsaCipher.doFinal(encryptedSymmetricKey);

            // Convert decrypted symmetric key to SecretKey
            SecretKey symmetricKey = new SecretKeySpec(decryptedSymmetricKey, "AES");

            // Load encrypted data
            byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedDataPath));

            // Initialize AES cipher with the decrypted symmetric key
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, symmetricKey);

            // Decrypt the actual data using the decrypted symmetric key
            byte[] decryptedData = aesCipher.doFinal(encryptedData);

            // Write decrypted data to output file
            Files.write(Paths.get(outputPath), decryptedData);

            System.out.println("Decryption complete. Decrypted data saved to " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey loadPrivateKeyFromPEM(String privateKeyPath) throws Exception {
        String privateKeyPEM = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""); // Remove whitespace and line breaks

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    }

    private static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
