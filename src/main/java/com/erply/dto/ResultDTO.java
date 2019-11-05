package com.erply.dto;

import com.erply.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * Created by dmitrigu on 9/06/2019.
 */

@Setter
@Getter
public class ResultDTO {
    private ErrorCode status;
    private String message;
    private Collection<TaskDTO> data;
}