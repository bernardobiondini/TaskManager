package taskManager.manager.Unit;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taskManager.manager.Models.Task;
import taskManager.manager.Repositories.TaskRepository;
import taskManager.manager.Services.TaskService;

public class TaskUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDueDays(10);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");
        savedTask.setDueDate(LocalDate.now().plusDays(10));

        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.save(task);
        assertEquals(savedTask.getId(), result.getId());
        assertEquals(savedTask.getDueDate(), result.getDueDate());
    }

    @Test
    public void testSetAsDone() {
        Task task = new Task();
        task.setId(1L);
        task.setDone(false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.setAsDone(1L);
        assertTrue(result.isDone());
    }

    @Test
    public void testSetAsUndone() {
        Task task = new Task();
        task.setId(1L);
        task.setDone(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.setAsUndone(1L);
        assertFalse(result.isDone());
    }

    @Test
    public void testFindDoneTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDone(true);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDone(true);

        List<Task> doneTasks = Arrays.asList(task1, task2);

        when(taskRepository.findDoneTasks()).thenReturn(doneTasks);

        List<Task> result = taskService.findDoneTasks();
        assertEquals(2, result.size());
    }

    @Test
    public void testFindUndoneTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDone(false);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDone(false);

        List<Task> undoneTasks = Arrays.asList(task1, task2);

        when(taskRepository.findUndoneTasks()).thenReturn(undoneTasks);

        List<Task> result = taskService.findUndoneTasks();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDone(false);
        task.setDueDate(LocalDate.now().minusDays(5));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getById(1L);
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Prevista, 5 dias de atraso", result.getInfo());
    }

    @Test
    public void testGetByIdTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task result = taskService.getById(1L);
        assertNull(result);
    }
}
