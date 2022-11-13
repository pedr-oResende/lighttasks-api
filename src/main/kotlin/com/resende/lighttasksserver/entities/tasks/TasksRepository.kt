package com.resende.lighttasksserver.entities.tasks

import com.resende.lighttasksserver.entities.tasks.model.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TasksRepository : JpaRepository<Task, Long>