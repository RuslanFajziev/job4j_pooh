package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    private String currentKeyQueue = "";

    @Override
    public Resp process(Req req) {
        String sourceName = req.getSourceName();
        String param = req.getParam();

        if (req.httpRequestType().equals("POST") && currentKeyQueue.contains(sourceName)) {
                ConcurrentLinkedQueue<String> linkedQueue = queue.get(currentKeyQueue);
                linkedQueue.add(param);
                return new Resp(param, "200 OK\r\n\r\n");
        }

        if (req.httpRequestType().equals("GET")) {
            String keyQueue = sourceName + param;
            if (queue.containsKey(keyQueue)) {
                ConcurrentLinkedQueue<String> linkedQueue = queue.get(keyQueue);
                if (!linkedQueue.isEmpty()) {
                    currentKeyQueue = keyQueue;
                    return new Resp(linkedQueue.poll(), "200 OK\r\n\r\n");
                }
            } else {
                ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();
                queue.put(keyQueue, linkedQueue);
            }
            currentKeyQueue = keyQueue;
        }
        return new Resp("", "204 No Content\r\n\r\n");
    }
}