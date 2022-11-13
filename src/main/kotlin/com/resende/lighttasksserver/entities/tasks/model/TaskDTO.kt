package com.resende.lighttasksserver.entities.tasks.model

data class TaskDTO(
    val id: Long?,
    val name: String?,
    val instructions: String?,
    val createdAt: String?,
    val deadline: String?,
    val teamId: Long?,
    val responsibleId: Long?
)