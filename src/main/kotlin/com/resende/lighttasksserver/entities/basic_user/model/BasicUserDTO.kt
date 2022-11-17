package com.resende.lighttasksserver.entities.basic_user.model

import com.resende.lighttasksserver.entities.tasks.model.TaskDTO

data class BasicUserDTO(
    val id: Long,
    val username: String?,
    val tasks: Set<TaskDTO>?,
    val teams_id: Set<Long>?
)