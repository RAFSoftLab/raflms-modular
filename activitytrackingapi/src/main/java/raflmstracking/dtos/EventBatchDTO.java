package raflmstracking.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// RISK-18 fix: @Size(max=100) sprecava napad milion-stavki; @Valid kaskira validaciju na elemente
public class EventBatchDTO {

    @NotNull
    @Size(max = 100, message = "Batch ne sme da sadrzi vise od 100 dogadjaja")
    @Valid
    private List<StudentEventDTO> events;

    public EventBatchDTO() {}

    public EventBatchDTO(List<StudentEventDTO> events) {
        this.events = events;
    }

    public List<StudentEventDTO> getEvents() { return events; }
    public void setEvents(List<StudentEventDTO> events) { this.events = events; }
}
