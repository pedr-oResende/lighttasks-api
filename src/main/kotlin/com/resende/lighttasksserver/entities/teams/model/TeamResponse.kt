package com.resende.lighttasksserver.entities.teams.model

import com.resende.lighttasksserver.model.Status

class TeamResponse(
    val team: TeamDTO?,
    val status: Status
)