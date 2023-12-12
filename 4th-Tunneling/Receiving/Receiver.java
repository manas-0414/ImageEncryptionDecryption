import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Receiver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the port number to listen on: ");
        int port = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the path to save the received file: ");
        String savePath = scanner.nextLine();

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(savePath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
