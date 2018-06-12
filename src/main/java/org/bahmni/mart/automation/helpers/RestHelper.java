package org.bahmni.mart.automation.helpers;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bahmni.mart.automation.models.SpringCloudResponse;
import org.bahmni.mart.automation.pollers.MethodPoller;

import static java.util.Objects.isNull;

public class RestHelper {
    private static int INTERVAL = 10000;

    public static String startBatchJob(String hostUrl) throws UnirestException {
        return Unirest.post(getTaskExecutionUrl(hostUrl))
                .queryString("name", "create-bahmni-mart")
                .asString().getBody();
    }

    private static String getTaskExecutionUrl(String hostUrl) {
        return String.format("http://%s:9393/tasks/executions", hostUrl);
    }

    public static SpringCloudResponse getDetails(String hostUrl, String jobExeId) {
        String taskExecutionUrl = getTaskExecutionUrl(hostUrl);
        try {
            String responseBody = Unirest.get(String.format("%s/%s", taskExecutionUrl, jobExeId))
                    .asString().getBody();
            return new Gson().fromJson(responseBody, SpringCloudResponse.class);

        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private static Boolean isJobComplete(SpringCloudResponse response) {
        return !isNull(response.getEndTime());
    }

    public static SpringCloudResponse pollUntilComplete(String hostUrl, String jobExeId) throws InterruptedException {
        MethodPoller<SpringCloudResponse> methodPoller = new MethodPoller<>();
        return methodPoller.poll(INTERVAL).method(() -> getDetails(hostUrl, jobExeId))
                .until(RestHelper::isJobComplete).execute();
    }


}
