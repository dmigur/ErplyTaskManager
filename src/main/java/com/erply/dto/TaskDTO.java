package com.erply.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by dmitrigu on 9/06/2019.
 */
@Setter
@Getter
public class TaskDTO implements Serializable {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("dueDate")
    private Calendar dueDate;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private String status;
}