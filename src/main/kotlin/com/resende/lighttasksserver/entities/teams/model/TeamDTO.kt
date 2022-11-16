package com.resende.lighttasksserver.entities.teams.model

import com.resende.lighttasksserver.entities.basic_user.model.BasicUserDTO

data class TeamDTO(
    val id: Long?,
    val name: String?,
    val members: Set<BasicUserDTO>?,
    val created_at: String?,
    val leader_id: Long?
)