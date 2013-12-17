package com.agrologic.app.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMonitor implements Runnable {
    ExecutorService executor;

    public ServerMonitor() {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        System.out.println("klds");
        try {
            ServerSocket serverSocket = new ServerSocket(5500);
            while (!Thread.currentThread().isInterrupted()) {
                ClientThread clientThread = new ClientThread(serverSocket.accept());
                executor.execute(clientThread);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void shutdown() {
        Thread.currentThread().interrupt();
    }

    class ClientThread implements Runnable {
        Socket socket;

        ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            OutputStream out = null;
            try {
                out = socket.getOutputStream();
                out.write("OK".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ServerMonitor serverMonitor = new ServerMonitor();
        Thread thread1 = new Thread(serverMonitor);
        thread1.start();


    }
}
