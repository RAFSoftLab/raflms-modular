package raflmstracking.dtos;

import java.util.List;

public class EventBatchDTO {
    private List<StudentEventDTO> events;

    public EventBatchDTO() {}

    public EventBatchDTO(List<StudentEventDTO> events) {
        this.events = events;
    }

    public List<StudentEventDTO> getEvents() { return events; }
    public void setEvents(List<StudentEventDTO> events) { this.events = events; }
}