package com.erply.exceptions;

/**
 * Created by dmitrigu on 9/06/2019.
 */
public class TaskAlreadyExistsException extends Exception {

    public TaskAlreadyExistsException(String s){
        super(s);
    }
}