package org.example.parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ULearn {
    private static final int STATUS_OK = 200;
    private static final String JAVA_COURSE_HTTP = "https://api.ulearn.me/exercise-statistics?courseId=BasicProgramming&count=10000";
    private static final HashMap<String, SlideData> slidesMap;

    static {
        slidesMap = createSlidesData();
    }

    public static SlideData getSlideData(String name) {
        return Objects.requireNonNull(slidesMap).get(name);
    }


    private static HashMap<String, SlideData> createSlidesData() {
        HashMap<String, SlideData> result = new HashMap<>();
        try {
            HttpClient client = HttpClient.newHttpClient();

            ArrayList<String> slidesId =
                    jsonUtils.getStringValues(getResponseString(client, JAVA_COURSE_HTTP), "apiUrl");
            for (String slideId : slidesId) {
                String slideResponse = getResponseString(client, "https://api.ulearn.me" + slideId);

                String title = jsonUtils.getStringValue(slideResponse, "title");
                int started = jsonUtils.getIntValue(slideResponse, "attemptedUsersCount");
                int finished = jsonUtils.getIntValue(slideResponse, "usersWithRightAnswerCount");

                if (title == null || started == -1 || finished == -1) {
                    continue;
                }

                SlideData data = new SlideData(title, started, finished);
                result.put(title, data);
            }
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    private static String getResponseString(HttpClient client, String url) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != STATUS_OK) {
            throw new RuntimeException("Status code: " + response.statusCode());
        }

        return response.body();
    }
}
