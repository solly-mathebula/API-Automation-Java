package utilities;

public class TestResult {

    private String scenario;
    private String status;
    private String duration;
    private String message;

    public TestResult(String scenario, String status, String duration, String message) {
        this.scenario = scenario;
        this.status = status;
        this.duration = duration;
        this.message = message;
    }

    // Getters
    public String getScenario() { return scenario; }
    public String getStatus() { return status; }
    public String getDuration() { return duration; }
    public String getMessage() { return message; }
}
