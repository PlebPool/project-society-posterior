package project.society.web.timeblocks.model.dto;

import java.io.Serializable;

public class TimeBlock implements Serializable {
    private String name;
    private Short startTimeMinutes; // Max 1440
    private Short timeDeltaMinutes; // Max 1440-startTimeMinutes
    private String colorHex;

    public TimeBlock(String name, Short startTimeMinutes, Short timeDeltaMinutes, String colorHex) {
        this.name = name;
        this.startTimeMinutes = startTimeMinutes;
        this.timeDeltaMinutes = timeDeltaMinutes;
        this.colorHex = colorHex;
    }

    public TimeBlock() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getStartTimeMinutes() {
        return startTimeMinutes;
    }

    public void setStartTimeMinutes(Short startTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
    }

    public Short getTimeDeltaMinutes() {
        return timeDeltaMinutes;
    }

    public void setTimeDeltaMinutes(Short timeDeltaMinutes) {
        this.timeDeltaMinutes = timeDeltaMinutes;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
