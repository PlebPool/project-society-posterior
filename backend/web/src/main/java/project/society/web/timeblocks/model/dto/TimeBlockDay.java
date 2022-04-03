package project.society.web.timeblocks.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;
import project.society.data.dto.HasId;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeBlockDay implements HasId<String> {
    private String uuid; // uuid
    @JsonIgnore private String userId; // google user id. // is not revealed to the user.
    @Transient private int dayIndex;
    private DayOfWeek dayOfWeek;
    private ArrayList<TimeBlock> timeBlocks; // Ordered by start time.

    public TimeBlockDay(String uuid, String userId, DayOfWeek dayOfWeek, ArrayList<TimeBlock> timeBlocks) {
        this.uuid = uuid;
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.timeBlocks = timeBlocks;
        this.dayIndex = dayOfWeek.getValue();
    }

    public TimeBlockDay(String userId, DayOfWeek dayOfWeek, ArrayList<TimeBlock> timeBlocks) {
        this.uuid = this.generateUUID();
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.timeBlocks = timeBlocks;
        this.dayIndex = dayOfWeek.getValue();
    }

    public TimeBlockDay() {
        this.timeBlocks = new ArrayList<>();
        this.uuid = this.generateUUID();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
        if(dayIndex > 0 && dayIndex < 8) {
            this.dayOfWeek = DayOfWeek.of(dayIndex);
        }
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

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = DayOfWeek.of(dayOfWeek);
    }

    public Serializable getTimeBlocks() {
        return timeBlocks;
    }

    public void setTimeBlocks(ArrayList<TimeBlock> timeBlocks) {
        this.timeBlocks = timeBlocks;
    }
}
