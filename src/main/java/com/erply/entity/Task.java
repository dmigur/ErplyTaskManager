package com.erply.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

/**
 * Created by dmitrigu on 9/06/2019.
 */
@Setter
@Getter
public class Task {
    private String name;
    private Calendar dueDate;
    private Integer id;
    private String status;
    private String description;

    public Task() {
    }
    public Task(String name, String description, Calendar dueDate, String status, Integer id) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.id = id;
        this.status = status;

    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return this.id ;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueDate=" + dueDate +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}