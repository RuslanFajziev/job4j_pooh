package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String sourceName = req.getSourceName(); //weather
        String param = req.getParam(); //rus
        if (req.httpRequestType().equals("POST")) {
            var sourceNameQueue = queue.get(sourceName);
            if (sourceNameQueue == null) {
                return new Resp("", "204 No Content");
            }
            for (var sourName : sourceNameQueue.values()) {
                sourName.offer(param);
            }
            return new Resp(param, "200 OK");
        }

        if (req.httpRequestType().equals("GET")) {
            var sourceNameQueue = queue.get(sourceName);
            if (sourceNameQueue == null) {
                ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();
                sourceNameQueue = new ConcurrentHashMap<>();
                sourceNameQueue.putIfAbsent(param, linkedQueue);
                queue.putIfAbsent(sourceName, sourceNameQueue);
                return new Resp("", "204 No Content");
            } else {
                var data = sourceNameQueue.get(param);
                if (data != null) {
                    if (!data.isEmpty()) {
                        return new Resp(data.poll(), "200 OK");
                    }
                } else {
                    ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();
                    sourceNameQueue.putIfAbsent(param, linkedQueue);
                }
                return new Resp("", "204 No Content");
            }

        }
        return new Resp("", "204 No Content");
    }
}