import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class LargeFileRSAEncryption {

    private static final int BUFFER_SIZE = 117; // For RSA 2048-bit key

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input file path: ");
        String inputFile = scanner.nextLine();
        String encryptedOutputFile = "output.txt";
        String publicKeyFile = "publickey.txt";
        String privateKeyFile = "privatekey.txt";
        String encryptedKeyFile = "encryptedkey.txt";

        try {
            // Generate RSA key pair
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Save public and private keys to files
            saveKeyToFile(publicKey, publicKeyFile);
            saveKeyToFile(privateKey, privateKeyFile);

            // Generate AES key for file encryption
            SecretKey secretKey = generateAESKey();

            // Encrypt the AES key using RSA and save to file
            byte[] encryptedKey = encryptKey(secretKey, publicKey);
            saveKeyToFile(new SecretKeySpec(encryptedKey, "AES"), encryptedKeyFile);

            // Encrypt the input file using AES
            encryptFile(inputFile, encryptedOutputFile, secretKey);

            System.out.println("RSA encryption completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Use 256-bit key for AES
        return keyGenerator.generateKey();
    }

    private static byte[] encryptKey(SecretKey secretKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(secretKey.getEncoded());
    }

    private static void encryptFile(String inputFilePath, String outputFilePath, SecretKey secretKey)
            throws Exception {
        try (CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(outputFilePath),
                Cipher.getInstance("AES/ECB/PKCS5Padding"))) {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            try (InputStream is = Files.newInputStream(Paths.get(inputFilePath))) {
                while ((bytesRead = is.read(buffer)) != -1) {
                    byte[] encryptedData = cipher.update(buffer, 0, bytesRead);
                    cos.write(encryptedData);
                }
            }

            byte[] finalEncryptedData = cipher.doFinal();
            cos.write(finalEncryptedData);
        }
    }

    private static void saveKeyToFile(Key key, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(key);
        }
    }
}
