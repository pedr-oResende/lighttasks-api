package com.resende.lighttasksserver.entities.tasks

import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.tasks.model.Task
import com.resende.lighttasksserver.entities.tasks.model.TaskDTO
import com.resende.lighttasksserver.utils.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    val taskRepository: TasksRepository? = null

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @GetMapping
    fun getTasks(): ResponseEntity<List<TaskDTO>> {
        val tasks = taskRepository?.findAll() ?: emptyList()
        return ResponseEntity(tasks.map { entityToDTO(it) }, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getTasksByUser(@PathVariable id: Long): ResponseEntity<List<TaskDTO>> {
        val tasks = basicUserRepository?.findById(id)?.get()?.tasks ?: emptyList()
        return ResponseEntity(tasks.map { entityToDTO(it) }, HttpStatus.OK)
    }

    @PostMapping
    fun registerTask(@RequestBody newTask: @Valid TaskDTO?): ResponseEntity<TaskDTO> {
        if (newTask?.responsible_id == null) return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val responsible = basicUserRepository?.findById(newTask.responsible_id)?.get()
            ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val task = with(newTask) {
            Task(
                id = id,
                name = name,
                deadline = deadline,
                description = description,
                created_at = DateTimeUtils.getDate(),
                responsible = responsible,
                team_id = team_id,
                is_done = false
            )
        }
        taskRepository?.save(task)
        return ResponseEntity(entityToDTO(task), HttpStatus.OK)
    }

    @PutMapping
    fun updateTask(@RequestBody newTask: @Valid TaskDTO?): ResponseEntity<TaskDTO> {
        if (newTask?.responsible_id == null || newTask.team_id == null) return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val responsible = basicUserRepository?.findById(newTask.responsible_id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val task = taskRepository?.findById(newTask.id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        val editedTask = with(newTask) {
            Task(
                id = id,
                responsible = responsible,
                name = name,
                deadline = deadline,
                description = description,
                is_done = is_done,
                created_at = task.created_at,
                team_id = team_id
            )
        }
        taskRepository?.save(editedTask)
        return ResponseEntity(entityToDTO(editedTask), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteTask(@PathVariable id: Long): HttpStatus {
        taskRepository?.deleteById(id)
        return HttpStatus.OK
    }

    @DeleteMapping("/delete_all")
    fun deleteAllTasks(): HttpStatus {
        taskRepository?.deleteAll()
        return HttpStatus.OK
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
                    team_id = team_id,
                    responsible_id = responsible?.id,
                    is_done = is_done
                )
            }
    }
}