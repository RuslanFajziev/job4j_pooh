package ru.job4j.pooh;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoohServer {
    private final HashMap<String, Service> modes = new HashMap<>();

    public void start() {
        modes.put("queue", new QueueService());
        modes.put("topic", new TopicService());
        ExecutorService pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                         InputStream input = socket.getInputStream()) {
                        byte[] buff = new byte[1_000_000];
                        var total = input.read(buff);
                        var content = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                        var req = Req.of(content);
                        var resp = modes.get(req.getPoohMode()).process(req);
                        String ls = System.lineSeparator();
                        out.write(("HTTP 1.1 " + resp.status() + ls).getBytes());
                        out.write((resp.text().concat(ls)).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PoohServer().start();
    }
}