package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String sourceName = req.getSourceName();
        String param = req.getParam();
        if (req.httpRequestType().equals("POST")) {
            if (queue.containsKey(sourceName)) {
                ConcurrentLinkedQueue<String> linkedQueue = queue.get(sourceName);
                linkedQueue.add(param);
            } else {
                ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();
                linkedQueue.add(param);
                queue.put(sourceName, linkedQueue);
            }
            return new Resp(param, "200 OK\r\n\r\n");
        }

        if (req.httpRequestType().equals("GET")) {
            if (queue.containsKey(sourceName)) {
                ConcurrentLinkedQueue<String> linkedQueue = queue.get(sourceName);
                if (!linkedQueue.isEmpty()) {
                    return new Resp(linkedQueue.poll(), "200 OK\r\n\r\n");
                }
            }
        }

        return new Resp("", "204 No Content\r\n\r\n");
    }
}