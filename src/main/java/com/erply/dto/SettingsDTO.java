package com.erply.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dmitrigu on 5/26/2019.
 */
@Setter
@Getter
public class SettingsDTO {

    private String namePattern;

    private String datePattern;

    private Integer limit;


}