package com.erply.service;

import com.erply.entity.Task;
import com.erply.repository.TaskRepository;
import com.erply.util.Settings;
import com.erply.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;

/**
 * Created by dmitrigu on 9/06/2019.
 */


@Service
public class TaskService implements ITaskService {


    @Autowired
    private TaskRepository repository;

    @Autowired
    private Settings settings;

    private static Logger logger = LoggerFactory.getLogger(TaskService.class);

    public void add(Task task) throws Exception {
        logger.info("Adding task to REPO");
        repository.add(task);
    }

    public Collection<Task> getTasks(Integer offset, Integer limit) {

        return repository.getTasks(offset, limit);

    }


    public Task parseTask(String name, String description, Calendar dueDate, String status) throws Exception {

        if (!name.matches(settings.getNamePattern())) {
            return null;
        }

        if (!(Status.Open.name().equals(status) ||
                Status.Completed.name().equals(status))) {
            return null;
        }
        return new Task(name, description, dueDate, status, 0);
    }

    public boolean deleteTask(Integer id) {

        return repository.deleteTask(id);
    }


    public boolean updateTask(Task task) {

        return repository.updateTask(task);
    }

    public Task getTask(Integer id) {

        return repository.getTask(id);
    }
}