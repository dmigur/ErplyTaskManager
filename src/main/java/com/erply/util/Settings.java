package com.erply.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by dmitrigu on 5/26/2019.
 */
@Setter
@Getter
@Service("Settings")
public class Settings {

    @Value("${task.name.pattern}")
    private String namePattern;
    @Value("${task.date.pattern}")
    private String datePattern;
    @Value("${ui.tasks.load.limit}")
    private Integer limit;

}