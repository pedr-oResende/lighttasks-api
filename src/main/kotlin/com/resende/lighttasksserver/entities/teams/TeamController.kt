package com.resende.lighttasksserver.entities.teams

import com.resende.lighttasksserver.entities.teams.model.Team
import com.resende.lighttasksserver.entities.basic_user.BasicUserController
import com.resende.lighttasksserver.entities.teams.model.TeamDTO
import com.resende.lighttasksserver.entities.teams.model.TeamResponse
import com.resende.lighttasksserver.utils.DateTimeUtils
import com.resende.lighttasksserver.model.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/teams")
class TeamController {

    @Autowired
    val teamRepository: TeamRepository? = null

    @Autowired
    val usersController: BasicUserController? = null

    @GetMapping
    fun getTeams() = teamRepository?.findAll() ?: emptyList()

    @GetMapping("/{id}")
    fun getTeamsByUser(@PathVariable id: Long): List<Team> = teamRepository?.findAll()?.toList()?.filter { team ->
        usersController?.getBasicUserById(id)?.teams?.map { it.id }?.contains(team.id) == true
    } ?: emptyList()

    @PostMapping("/create")
    fun registerTeams(@RequestBody newTeam: @Valid Team?): TeamResponse? {
        if (newTeam == null) return TeamResponse(
            team = null,
            status = Status.FAILURE
        )
        val team = newTeam.copy(
            createdAt = DateTimeUtils.getDate()
        )
        teamRepository?.save(team)
        return TeamResponse(
            team = team,
            status = Status.SUCCESS
        )
    }

    @PatchMapping("/create")
    fun editTeam(@RequestBody newTeam: @Valid Team?): TeamResponse? {
        if (newTeam == null) return TeamResponse(
            team = null,
            status = Status.FAILURE
        )
        teamRepository?.save(newTeam)
        return TeamResponse(
            team = newTeam,
            status = Status.SUCCESS
        )
    }

    @DeleteMapping("/remove/{id}")
    fun deleteTeam(@PathVariable id: Long): Status? {
        if (teamRepository?.findAll()?.toList()?.map { it.id }?.contains(id) == true) {
            return Status.FAILURE
        }
        teamRepository?.deleteById(id)
        return Status.SUCCESS
    }

    companion object {
        fun entityToDTO(team: Team) =
            with(team) {
                TeamDTO(
                    id = id,
                    name = name,
                    created_at = created_at,
                    leader_id = leader_id,
                    tasks_id = tasks?.map { it.id }?.toSet(),
                    members_id = members?.map { it.id }?.toSet()
                )
            }
    }
}