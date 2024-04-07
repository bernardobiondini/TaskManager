package taskManager.manager.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import taskManager.manager.Models.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
  @Query("SELECT t FROM Task t WHERE t.done = true")
  List<Task> findDoneTasks();

  @Query("SELECT t FROM Task t WHERE t.done = false")
  List<Task> findUndoneTasks();
}
