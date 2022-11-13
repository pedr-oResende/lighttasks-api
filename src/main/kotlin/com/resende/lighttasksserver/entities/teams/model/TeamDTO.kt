package com.resende.lighttasksserver.entities.teams.model

data class TeamDTO(
    val id: Long? = 0L,
    val name: String? = "",
    val membersId: Set<Long>? = setOf(),
    val tasksId: Set<Long>? = setOf(),
    val createdAt: String? = "",
    val leaderId: Long? = 0L
)