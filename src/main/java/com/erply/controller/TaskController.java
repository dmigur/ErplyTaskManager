package com.erply.controller;

import com.erply.dto.ResultDTO;
import com.erply.dto.SettingsDTO;
import com.erply.dto.TaskDTO;
import com.erply.entity.Task;
import com.erply.enums.ErrorCode;
import com.erply.service.TaskService;
import com.erply.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by dmitrigu on 9/06/2019.
 */

@RestController
public class TaskController {

    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskdService;

    @Autowired
    private Settings settings;

    @RequestMapping("/api/tasks")
    public Collection<TaskDTO> getTasks(@RequestParam(value = "offset", required = false) Integer offset) {

        offset = offset == null ? 0 : offset;
        Collection<Task> tasks = taskdService.getTasks(offset, settings.getLimit());

        logger.info("Getting tasks");

        Collection<TaskDTO> result = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO dto = new TaskDTO();
            dto.setName(task.getName());

            dto.setDescription(task.getDescription());
            dto.setDueDate(task.getDueDate());
            dto.setId(task.getId());
            dto.setStatus(task.getStatus());
            result.add(dto);
        }
        return result;
    }


    @ResponseStatus(CREATED)
    @RequestMapping(value = "/api/addtask", method = POST)
    @ResponseBody
    public ResponseEntity addTask(@RequestBody TaskDTO data) {
        logger.info("Adding task");

        String name = data.getName();
        Calendar due = data.getDueDate();
        String status = data.getStatus();
        String description = data.getDescription();

        ResultDTO result = new ResultDTO();
        try {
            Task task = taskdService.parseTask(name, description, due, status);

            if (task == null) {
                result.setStatus(ErrorCode.Error);
                result.setMessage("Error parsing task");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

            }
            taskdService.add(task);
            result.setStatus(ErrorCode.Success);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error adding task", e);
            result.setStatus(ErrorCode.Error);
            result.setMessage(e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        //return result;
    }


    @RequestMapping("/api/settings")
    @ResponseStatus(OK)
    @ResponseBody
    public SettingsDTO getSettings() {
        logger.info("getting settings");

        SettingsDTO result = new SettingsDTO();
        result.setNamePattern(settings.getNamePattern());
        result.setDatePattern(settings.getDatePattern());
        result.setLimit(settings.getLimit());
        return result;
    }


    @RequestMapping(value = "/api/deletetask/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(OK)
    @ResponseBody
    public ResponseEntity deleteTask(@PathVariable("id") Integer id) {

        logger.info("deleteing task id = " + id);
        ResultDTO result = new ResultDTO();

        try {
            if (taskdService.deleteTask(id)) {
                result.setStatus(ErrorCode.Success);
            } else {
                result.setStatus(ErrorCode.Error);
                result.setMessage("Task not found!");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            result.setStatus(ErrorCode.Error);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ResponseStatus(OK)
    @RequestMapping(value = "/api/updatetask", method = PUT)
    @ResponseBody
    public ResponseEntity updateTask(@RequestBody TaskDTO data) {
        logger.info("updating task  2");
        ResultDTO result = new ResultDTO();
        try {

            String name = data.getName();
            String description = data.getDescription();
            Calendar due = data.getDueDate();
            String status = data.getStatus();
            Integer id = data.getId();

            Task task = taskdService.parseTask(name, description, due, status);

            if (task == null) {
                result.setStatus(ErrorCode.Error);
                result.setMessage("Error updating task, parse error");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            task.setId(id);

            if (!taskdService.updateTask(task)) {
                result.setStatus(ErrorCode.Error);
                result.setMessage("Task not fouund!");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            } else {
                result.setStatus(ErrorCode.Success);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
        logger.error("Error adding TASK", e);
            result.setStatus(ErrorCode.Error);
            result.setMessage(e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/api/task/{id}", method = RequestMethod.GET)
    @ResponseStatus(OK)
    @ResponseBody
    public ResponseEntity getTask(@PathVariable("id") Integer id) {

        logger.info("deleteing task id = " + id);
        TaskDTO result = new TaskDTO();

        try {
            Task task = taskdService.getTask(id);
            if (task != null) {
                result.setStatus(task.getStatus());
                result.setDueDate(task.getDueDate());
                result.setName(task.getName());
                result.setId(task.getId());
            } else {

                ResultDTO res = new ResultDTO();
                res.setMessage("task not found for id = " + id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}