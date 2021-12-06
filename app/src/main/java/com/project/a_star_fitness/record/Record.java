package com.project.a_star_fitness.record;

public class Record {
    private String type;
    private String duration;
    private String description;

    public Record() {
    }

    public Record(String type, String duration, String description) {
        this.type = type;
        this.duration = duration;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Record{" +
                "type='" + type + '\'' +
                ", duration='" + duration + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
