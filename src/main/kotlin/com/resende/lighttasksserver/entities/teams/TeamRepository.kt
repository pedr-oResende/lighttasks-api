package com.resende.lighttasksserver.entities.teams

import com.resende.lighttasksserver.entities.teams.model.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long>