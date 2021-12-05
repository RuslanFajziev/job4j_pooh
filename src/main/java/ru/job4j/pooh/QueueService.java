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
            queue.putIfAbsent(sourceName, new ConcurrentLinkedQueue<>());
            queue.get(sourceName).add(param);
            return new Resp(param, "200 OK");
        }

        if (req.httpRequestType().equals("GET")) {
                ConcurrentLinkedQueue<String> linkedQueue = queue.get(sourceName);
                if (linkedQueue != null && !linkedQueue.isEmpty()) {
                    return new Resp(linkedQueue.poll(), "200 OK");
                }
        }

        return new Resp("", "204 No Content");
    }
}