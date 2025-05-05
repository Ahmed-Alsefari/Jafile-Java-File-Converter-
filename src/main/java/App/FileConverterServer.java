package App;

import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConverterServer {
    public static void main(String[] args) {
        try (ServerSocket serversocket = new ServerSocket(9000)) {
            System.out.println("File converter server running on port [9000] ");

            while (true) {
                Socket clientSocket = serversocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());

        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
                String inputPathStr = (String) in.readObject();
                String outputPathStr = (String) in.readObject();

                Path inputPath = Paths.get(inputPathStr);
                Path outputPath = Paths.get(outputPathStr);

                boolean result = ConvertFile.converFileThread(inputPath, outputPath);

                out.writeObject(result);
            } catch (Exception e) {
                System.err.println("Cilent error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }
    }


}
