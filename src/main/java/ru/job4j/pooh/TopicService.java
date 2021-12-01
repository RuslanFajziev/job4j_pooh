package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String sourceName = req.getSourceName();
        String param = req.getParam();
        String prefSourceName = sourceName.concat("_");
        var outputPost = false;

        if (req.httpRequestType().equals("POST")) {
            var keysSet = queue.keySet();
            for (var key : keysSet) {
                if (key.contains(prefSourceName)) {
                    queue.get(key).add(param);
                    outputPost = true;
                }
            }
            if (outputPost) {
                return new Resp(param, "200 OK\r\n\r\n");
            }
        }

        if (req.httpRequestType().equals("GET")) {
            String keyQueue = prefSourceName.concat(param);
            ConcurrentLinkedQueue<String> linkedQueue = queue.get(keyQueue);
            if (linkedQueue != null && !linkedQueue.isEmpty()) {
                return new Resp(linkedQueue.poll(), "200 OK\r\n\r\n");
            } else {
                linkedQueue = new ConcurrentLinkedQueue<>();
                queue.putIfAbsent(keyQueue, linkedQueue);
            }
        }
        return new Resp("", "204 No Content\r\n\r\n");
    }
}