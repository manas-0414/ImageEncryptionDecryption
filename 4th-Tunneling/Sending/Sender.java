import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Sender {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the path of the file to send: ");
        String filePath = scanner.nextLine();

        System.out.print("Enter the IP address of the receiver: ");
        String host = scanner.nextLine();

        System.out.print("Enter the port number: ");
        int port = Integer.parseInt(scanner.nextLine());

        try (Socket socket = new Socket(host, port);
                FileInputStream fileInputStream = new FileInputStream(filePath);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = socket.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
