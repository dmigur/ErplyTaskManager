package com.erply.service;

import com.erply.entity.Task;

import java.util.Collection;

/**
 * Created by dmitrigu on 9/06/2019.
 */
public interface ITaskService {

    public void add(Task task) throws Exception;

    public Collection<Task> getTasks(Integer offset, Integer limit);

    public Task getTask(Integer id);

}
