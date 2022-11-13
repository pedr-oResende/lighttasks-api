package com.resende.lighttasksserver.entities.teams.model

data class TeamDTO(
    val id: Long?,
    val name: String?,
    val members_id: Set<Long>?,
    val tasks_id: Set<Long>?,
    val created_at: String?,
    val leader_id: Long?
)