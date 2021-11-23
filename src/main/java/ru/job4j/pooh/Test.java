package ru.job4j.pooh;

import java.io.*;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls
                + "Content-Length: 14" + ls
                + "Content-Type: application/x-www-form-urlencoded" + ls
                + "" + ls
                + "temperature=18" + ls
                + "end";
        HashMap<String, String> hashMap = new HashMap();
        InputStream targetStream = new ByteArrayInputStream(content.getBytes());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(targetStream))) {
            String str = in.readLine();
            while (!str.contains("end")) {
                System.out.println("--------------- " + str);
                str = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}