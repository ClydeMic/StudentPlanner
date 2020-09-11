package com.example.studentplanner;


//Task object
public class Task {
    private String taskName;
    private String taskLocation;
    private String taskDuration;
    private String taskNotes;

    public Task(String taskName, String taskLocation, String taskNotes, String taskDuration){
        this.taskDuration = taskDuration;
        this.taskLocation = taskLocation;
        this.taskName = taskName;
        this.taskNotes = taskNotes;
    }

    public String getTaskDuration() {
        return taskDuration;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskNotes() {
        return taskNotes;
    }
}
