package com.example.taskmanagement.Utils;

import java.sql.Date;

public class Tasks implements Comparable<Tasks> {
    private Integer task_id;
    private String task_name;
    private String description;
    private String priority;
    private String status;
    private Date due_date;
    private Date start_date;
    private Integer user_id;

    public Tasks(Integer task_id,String task_name,String description,String priority, String status,Date start_date, Date due_date,Integer user_id) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.due_date = due_date;
        this.start_date = start_date;
        this.user_id = user_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public Date getDue_date() {
        return due_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Integer getTask_id(){
        return task_id;
    }

    @Override
    public int compareTo(Tasks other) {
        // Compare by priority first
        int priorityComparison = comparePriority(this.priority, other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // If priorities are the same, compare by status
        int statusComparison = compareStatus(this.status, other.status);
        if (statusComparison != 0) {
            return statusComparison;
        }

        // If priorities are the same, compare by due date
        return this.due_date.compareTo(other.due_date);
    }

    private int comparePriority(String priority1, String priority2) {
        if (priority1.equals(priority2)) return 0;
        if (priority1.equals("High")) return -1;
        if (priority2.equals("High")) return 1;
        if (priority1.equals("Medium")) return -1;
        return 1;
    }

    private int compareStatus(String status1, String status2) {
        if (status1.equals(status2)) return 0;
        if (status1.equals("Pending")) return -1;
        return 1;
    }
}
