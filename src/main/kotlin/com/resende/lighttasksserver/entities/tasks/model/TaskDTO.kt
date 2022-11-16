package com.resende.lighttasksserver.entities.tasks.model

data class TaskDTO(
    val id: Long,
    val name: String?,
    val description: String?,
    val created_at: String?,
    val deadline: String?,
    val team_id: Long?,
    val responsible_id: Long?,
    val is_done: Boolean?
)