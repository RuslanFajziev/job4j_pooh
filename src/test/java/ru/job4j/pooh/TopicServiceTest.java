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
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        Resp result5 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        topicService.process(
                new Req("POST", "topic", "weather", t90)
        );

        Resp result6 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        Resp result7 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        assertThat(result1.text(), is(t18));
        assertThat(result2.text(), is(t19));
        assertThat(result3.text(), is(""));
        assertThat(result4.text(), is(t45));
        assertThat(result5.text(), is(t90));
        assertThat(result6.text(), is(t90));
        assertThat(result7.text(), is(""));
    }
}