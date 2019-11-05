package com.erply.repository;

import com.erply.entity.Task;

import java.util.Collection;

/**
 * Created by dmitrigu on 9/06/2019.
 */
public interface ITaskRepository {

    public void add(Task task) throws Exception;

    public Collection<Task> getTasks(int offset, int limit);

    public boolean updateTask(Task task);

    public boolean deleteTask(Integer id);

    public Task getTask(Integer id);
}
