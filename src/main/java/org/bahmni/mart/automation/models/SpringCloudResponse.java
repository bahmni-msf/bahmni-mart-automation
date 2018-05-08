package org.bahmni.mart.automation.models;

public class SpringCloudResponse {
    private String executionId;
    private String exitCode;
    private String taskName;
    private String startTime;
    private String endTime;
    private String exitMessage;
    private String errorMessage;
    private String externalExecutionId;

    public String getExternalExecutionId() {
        return externalExecutionId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getExitCode() {
        return exitCode;
    }

    public String getExecutionId() {
        return executionId;
    }
}
