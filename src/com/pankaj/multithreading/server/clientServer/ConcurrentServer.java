package com.pankaj.multithreading.server.clientServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentServer {

    private static ServerSocket socket;
    private static ExecutorService service = Executors.newFixedThreadPool(4);
    private static volatile boolean disconnect = false;

    private static ParallelCache cache = new ParallelCache();


    public static void connect() {
        try {
            socket = new ServerSocket( 2221);
            do {
                service.execute(new RequestWorker(socket.accept()));

            } while (!disconnect);

            service.awaitTermination(1, TimeUnit.DAYS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.disconnect = true;
    }

    private static class RequestWorker implements Runnable {
        private Socket clientSocket;

        RequestWorker(Socket clientSocket) {
            clientSocket = clientSocket;
        }

        @Override
        public void run() {
            PrintWriter writer = null;

            try {
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String command = inputStreamReader.readLine();

                String resultValue = cache.getFromCache(command);
                if (resultValue == null) {
                    switch (command) {
                        case "h":
                            cache.addToCache(command, "Hello Pankaj");
                            resultValue = "Hello Pankaj";
                            break;
                        case "s":
                            cache.addToCache(command, "Status is noon and Pankaj is coding");
                            break;
                        default:
                            throw new IllegalArgumentException("Wrong command");
                    }
                } else {
                    System.out.println("Getting from cache");
                }

                writer.write(resultValue);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                }
            }
        }
    }


    public static void main(String[] args) {
        ConcurrentServer.connect();
    }
}
