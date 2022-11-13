package com.resende.lighttasksserver.entities.tasks.model

data class TaskDTO(
    val id: Long?,
    val name: String?,
    val instructions: String?,
    val created_at: String?,
    val deadline: String?,
    val team_id: Long?,
    val responsible_id: Long?
)