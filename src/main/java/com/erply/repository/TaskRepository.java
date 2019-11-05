package com.erply.repository;

import com.erply.entity.Task;
import com.erply.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by dmitrigu on 9/06/2019.
 */
@Repository
public class TaskRepository implements ITaskRepository {

    private static Logger logger = LoggerFactory.getLogger(TaskRepository.class);

    public void add(Task task) throws Exception {

        Optional<Task> tmax = tasks.stream().max(Comparator.comparing(Task::getId));
        Integer id = !tmax.isPresent()? 1: tmax.get().getId()+1;
        task.setId(id);
        tasks.add(task);

    }


    public Collection<Task> getTasks(int offset, int limit) {

        List<Task> items = tasks.stream().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(items)) {
            return items ;

        }
        if (offset >= items.size()) {
            return new ArrayList<Task>();
        }

        List<Task> sublist = null;

        if (limit == -1) {
            sublist = items.subList(offset, items.size());
        } else {
            sublist = items.subList(offset, Math.min(items.size(), offset + limit));
        }

        return sublist;

    }

    private List<Task> tasks = new CopyOnWriteArrayList<Task>();
    public TaskRepository(){

/*      For testing purposes

        Task task = new Task("Task1",  Calendar.getInstance(), Status.Open.name(), 1);
        tasks.add(task);

        */

    }

    public boolean deleteTask(Integer id)  {

        return tasks.removeIf(item->id.equals(item.getId()));
    }

    public boolean updateTask(Task t)  {

        Optional<Task> optional = tasks.stream().filter(el->el.getId().equals(t.getId())).findFirst();
        if (!optional.isPresent()){
            return false;
        }
        Task task = optional.get();
        task.setName(t.getName());
        task.setDescription(t.getDescription());
        task.setDueDate(t.getDueDate());
        task.setStatus(t.getStatus());
        return true;
    }


    public Task getTask(Integer id)  {

        Optional<Task> optional = tasks.stream().filter(el->el.getId().equals(id)).findFirst();
        return optional.get();
    }
}