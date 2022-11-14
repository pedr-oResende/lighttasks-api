package com.resende.lighttasksserver.entities.tasks

import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.tasks.model.Task
import com.resende.lighttasksserver.entities.tasks.model.TaskDTO
import com.resende.lighttasksserver.entities.teams.TeamRepository
import com.resende.lighttasksserver.model.Status
import com.resende.lighttasksserver.utils.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    val taskRepository: TasksRepository? = null

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @Autowired
    val teamsRepository: TeamRepository? = null

    @GetMapping
    fun getTasks(): List<TaskDTO> {
        val tasks = taskRepository?.findAll() ?: emptyList()
        return tasks.map { entityToDTO(it) }
    }

    @GetMapping("/{id}")
    fun getTasksByUser(@PathVariable id: Long): List<TaskDTO> {
        val tasks = basicUserRepository?.findById(id)?.get()?.tasks ?: emptyList()
        return tasks.map { entityToDTO(it) }
    }

    @PostMapping
    fun registerTask(@RequestBody newTask: @Valid Task?): Status {
        if (newTask?.responsible?.id == null) return Status.FAILURE
        val responsible = basicUserRepository?.findById(newTask.responsible.id)?.get() ?: return Status.FAILURE
        val task = newTask.copy(
            createdAt = DateTimeUtils.getDate(),
            responsible = responsible
        )
        taskRepository?.save(task)
        return Status.SUCCESS
    }

    @PutMapping
    fun updateTask(@RequestBody newTask: @Valid Task?): Status {
        if (newTask?.responsible?.id == null) return Status.FAILURE
        val responsible = basicUserRepository?.findById(newTask.responsible.id)?.get()
        val task = taskRepository?.findById(newTask.id)?.get() ?: return Status.FAILURE
        taskRepository?.save(
            with(newTask) {
                task.copy(
                    responsible = responsible,
                    name = name,
                    deadline = deadline,
                    instructions = description,
                    team = team
                )
            }
        )
        return Status.SUCCESS
    }

    @DeleteMapping("{id}")
    fun deleteTask(@PathVariable id: Long): Status? {
        taskRepository?.deleteById(id)
        return Status.SUCCESS
    }

    @DeleteMapping("/delete_all")
    fun deleteAllTasks(): Status? {
        taskRepository?.deleteAll()
        return Status.SUCCESS
    }

    companion object {
        fun entityToDTO(task: Task) =
            with(task) {
                TaskDTO(
                    id = id,
                    name = name,
                    description = description,
                    created_at = created_at,
                    deadline = deadline,
                    team_id = team?.id,
                    responsible_id = responsible?.id
                )
            }
    }
}