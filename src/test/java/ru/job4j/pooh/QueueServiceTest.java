package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPostThenGetQueueNotFound() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather2", null)
        );
        assertThat(result.status(), is("204 No Content\r\n\r\n"));
    }

    @Test
    public void whenPostThenGetQueueDouble() {
        QueueService queueService = new QueueService();
        String paramForPostMethod1 = "temperature=18";
        String paramForPostMethod2 = "temperature=20";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod1)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod2)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result1.text(), is(paramForPostMethod1));
        assertThat(result2.text(), is(paramForPostMethod2));
    }

    @Test
    public void whenPostThenGetQueueDouble2() {
        QueueService queueService = new QueueService();
        String paramForPostMethod1 = "temperature=18";
        String paramForPostMethod2 = "temperature=20";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather1", paramForPostMethod1)
        );
        queueService.process(
                new Req("POST", "queue", "weather2", paramForPostMethod2)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather1", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather2", null)
        );
        assertThat(result1.text(), is(paramForPostMethod1));
        assertThat(result2.text(), is(paramForPostMethod2));
    }
}