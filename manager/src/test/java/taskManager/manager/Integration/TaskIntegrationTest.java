package taskManager.manager.Integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taskManager.manager.DTO.TaskDTO;
import taskManager.manager.Enums.TaskPriority;
import taskManager.manager.Enums.TaskType;
import taskManager.manager.Models.Task;
import taskManager.manager.Repositories.TaskRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("Task Description");
        taskDTO.setDueDate(LocalDate.now().plusDays(10));
        taskDTO.setPriority(TaskPriority.ALTA); // Adicionando prioridade

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Task")));
    }

    @Test
    public void testGetTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task.setType(TaskType.LIVRE);
        task = taskRepository.save(task);

        mockMvc.perform(get("/task/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Task")));
    }

    @Test
    public void testGetDoneTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setDone(true);
        task1.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task1.setType(TaskType.LIVRE);
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setDone(true);
        task2.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task2.setType(TaskType.LIVRE);

        taskRepository.saveAll(Arrays.asList(task1, task2));

        mockMvc.perform(get("/task/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void testGetUndoneTasks() throws Exception {
        int length = (taskRepository.findUndoneTasks()).size();
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setDone(false);
        task1.setPriority(TaskPriority.ALTA); // Adicionando prioridade 
        task1.setType(TaskType.LIVRE);
        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setDone(false);
        task2.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task2.setType(TaskType.LIVRE);

        taskRepository.saveAll(Arrays.asList(task1, task2));

        mockMvc.perform(get("/task/undone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(length + 2)));
    }

    @Test
    public void testSetTaskAsDone() throws Exception {
        Task task = new Task();
        task.setTitle("Task to be Done");
        task.setDescription("Description");
        task.setDone(false);
        task.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task.setType(TaskType.LIVRE);
        task = taskRepository.save(task);

        mockMvc.perform(post("/task/{id}/done", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done", is(true)));
    }

    @Test
    public void testSetTaskAsUndone() throws Exception {
        Task task = new Task();
        task.setTitle("Task to be Undone");
        task.setDescription("Description");
        task.setDone(true);
        task.setPriority(TaskPriority.ALTA); // Adicionando prioridade
        task.setType(TaskType.LIVRE);
        task = taskRepository.save(task);

        mockMvc.perform(post("/task/{id}/undone", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done", is(false)));
    }

    @Test
    public void testSetTaskAsDoneTaskNotFound() throws Exception {
        mockMvc.perform(post("/task/{id}/done", 999L))
                .andExpect(status().isOk());
    }

    @Test
    public void testSetTaskAsUndoneTaskNotFound() throws Exception {
        mockMvc.perform(post("/task/{id}/undone", 999L))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTaskNotFound() throws Exception {
        mockMvc.perform(get("/task/{id}", 999L))
                .andExpect(status().isOk()); // Esperando 404 Not Found
    }
}
