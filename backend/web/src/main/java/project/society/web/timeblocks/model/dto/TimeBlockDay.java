package project.society.web.timeblocks.model.dto;

import project.society.data.dto.HasId;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public class TimeBlockDay implements HasId<String> {
    private String uuid; // uuid
    private String userId; // google user id.
    private DayOfWeek dayOfWeek;
    private List<TimeBlock> timeBlocks; // Ordered by start time.

    public TimeBlockDay(String userId, DayOfWeek dayOfWeek, List<TimeBlock> timeBlocks) {
        this.uuid = this.generateUUID();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.timeBlocks = timeBlocks;
    }

    public TimeBlockDay(String uuid, String userId, DayOfWeek dayOfWeek, List<TimeBlock> timeBlocks) {
        this.uuid = uuid;
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.timeBlocks = timeBlocks;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return this.uuid;
    }

    public void setId(String id) {
        this.uuid = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Serializable getTimeBlocks() {
        return (Serializable) timeBlocks;
    }

    public void setTimeBlocks(List<TimeBlock> timeBlocks) {
        this.timeBlocks = timeBlocks;
    }
}
