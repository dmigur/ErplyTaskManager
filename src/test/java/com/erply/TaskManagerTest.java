package com.erply;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.erply.dto.ResultDTO;
import com.erply.entity.Task;
import com.erply.enums.ErrorCode;
import com.erply.repository.TaskRepository;
import com.erply.service.TaskService;
import com.erply.util.Constants;
import com.erply.util.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.erply.dto.TaskDTO;

import javax.xml.transform.Result;
import java.util.*;

public class TaskManagerTest extends AbstractTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private List<Task> tasks = new ArrayList<>();
    Task task = new Task("MyTask","", Calendar.getInstance(), Status.Open.name(), 1 );

    @Override
    @Before
    public void setUp() {

        super.setUp();

        tasks.add(task);
        ReflectionTestUtils.setField(taskRepository, "tasks", tasks);

    }

    @Test
    public void getTaskList() throws Exception {
        String uri = "/api/tasks";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        TaskDTO[] tasks = super.mapFromJson(content, TaskDTO[].class);
        assertTrue(tasks.length > 0);
    }


    @Test
    public void getTask() throws Exception {
        Integer ID = 1;
        String uri = "/api/task/" + ID;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        TaskDTO task = super.mapFromJson(content, TaskDTO.class);
        assertTrue(task.getId() == ID);
    }

    @Test
    public void createTask() throws Exception {
        String uri = "/api/addtask";
        Task task = new Task();
        task.setName("Test2");
        task.setDueDate(Calendar.getInstance());
        task.setStatus(Status.Open.name());

        String inputJson = super.mapToJson(task);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();

        ResultDTO res = super.mapFromJson(content, ResultDTO.class);
        assertTrue(res!=null &&
                ErrorCode.Success.equals(res.getStatus()));
    }


    @Test
    public void updateTaskName() throws Exception {
        String uri = "/api/updatetask";
        Task task = new Task("MyTask","test",Calendar.getInstance(), Status.Open.name(), 1 );
        String newName = "Changed";
        task.setName(newName);

        String inputJson = super.mapToJson(task);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        ResultDTO res = super.mapFromJson(content, ResultDTO.class);


        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/task/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        TaskDTO newtask = super.mapFromJson(content, TaskDTO.class);
        assertTrue(newName.equals(newtask.getName()));
    }

    @Test
    public void updateTaskStatus() throws Exception {
        String uri = "/api/updatetask";
        Task task = new Task("MyTask","test",Calendar.getInstance(), Status.Open.name(), 1 );
        String newStatus = Status.Completed.name();
        task.setStatus(newStatus);

        String inputJson = super.mapToJson(task);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        ResultDTO res = super.mapFromJson(content, ResultDTO.class);


        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/task/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        content = mvcResult.getResponse().getContentAsString();
        TaskDTO newtask = super.mapFromJson(content, TaskDTO.class);
        assertTrue(newStatus.equals(newtask.getStatus()));
    }

    @Test
    public void updateTaskNotFound() throws Exception {
        String uri = "/api/updatetask";

        Task task = new Task("MyTask", "test", Calendar.getInstance(), Status.Open.name(), 1 );
        task.setId(2);

        String inputJson = super.mapToJson(task);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

    }
    @Test
    public void deleteTask() throws Exception {
        String uri = "/api/deletetask/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        ResultDTO res = super.mapFromJson(content, ResultDTO.class);
        assertTrue(res!=null &&
                ErrorCode.Success.equals(res.getStatus()));
    }

    @Test
    public void deleteTaskNotFound() throws Exception {
        String uri = "/api/deletetask/2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }
}