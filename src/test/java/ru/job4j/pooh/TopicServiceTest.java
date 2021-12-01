package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";

        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTopic2() {
        TopicService topicService = new TopicService();
        String t18 = "temperature=18";
        String t19 = "temperature=19";
        String t45 = "temperature=45";
        String t90 = "temperature=90";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";

        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        topicService.process(
                new Req("POST", "topic", "weather", t18)
        );
        topicService.process(
                new Req("POST", "topic", "weather", t19)
        );

        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        topicService.process(
                new Req("POST", "topic", "weather", t45)
        );
        topicService.process(
                new Req("POST", "topic", "weather", t90)
        );

        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result3 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result4 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result5 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        Resp result6 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        Resp result7 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result8 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        assertThat(result1.text(), is(t18));
        assertThat(result2.text(), is(t19));
        assertThat(result3.text(), is(t45));
        assertThat(result4.text(), is(t90));
        assertThat(result7.text(), is(""));
        assertThat(result5.text(), is(t45));
        assertThat(result6.text(), is(t90));
        assertThat(result8.text(), is(""));
    }

    @Test
    public void whenTopic3() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
    /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
    Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        /*=====================================================================*/
    /* Режим topic. Добавляем данные в топик weather. Теперь данные должны попасть
    в обе индивидуальные очереди*/
        topicService.process(
                new Req("POST", "topic", "weather", "humidity=70")
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result3 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565. */
        Resp result4 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407.
         * Очередь пустая */
        Resp result5 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        /*===========================================================*/
        /* Режим topic. Забираем данные из индивидуальной очереди в топике traffic. Очередь client407.
         * Еще не подписывались. Очередь пустая */
        Resp result6 = topicService.process(
                new Req("GET", "topic", "traffic", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
        assertThat(result3.text(), is("humidity=70"));
        assertThat(result4.text(), is("humidity=70"));
        assertThat(result5.text(), is(""));
        assertThat(result6.text(), is(""));
    }
}