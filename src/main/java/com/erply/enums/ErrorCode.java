package com.erply.enums;

/**
 * Created by dmitrigu on 9/06/2019.
 */
public enum ErrorCode {
    Success(0),
    Error(1);
    ErrorCode(int cod){
        code = cod;
    }
    private int code;
}