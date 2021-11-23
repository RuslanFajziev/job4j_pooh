package ru.job4j.pooh;

import java.io.*;
import java.util.HashMap;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String httpRequestType = "";
        String poohMode = "";
        String sourceName = "";
        String param = "";
        String reqType1 = "POST";
        String reqType2 = "GET";
        String markerStop = "end";
        content += markerStop;

        HashMap<String, String> mapParam = new HashMap();
        InputStream targetStream = new ByteArrayInputStream(content.getBytes());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(targetStream))) {
            String str = in.readLine();
            while (!str.contains(markerStop)) {
                String[] strArr = str.split(" ");
                if (strArr.length > 1) {
                    mapParam.put(strArr[0], strArr[1]);
                } else if (strArr.length == 1) {
                    param = strArr[0];
                }
                str = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mapParam.containsKey(reqType1)) {
            httpRequestType = reqType1;
        } else if (mapParam.containsKey(reqType2)) {
            httpRequestType = reqType2;
        }

        String[] strArr2 = mapParam.get(httpRequestType).split("/");
        poohMode = strArr2[1];
        sourceName = strArr2[2];
        if (strArr2.length == 4) {
            param = strArr2[3];
        }

        if (httpRequestType.isEmpty() || poohMode.isEmpty() || sourceName.isEmpty()
                || (param.isEmpty() && httpRequestType.equals(reqType1))) {
            throw new IllegalArgumentException("one of the parameters is empty");
        }

        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}