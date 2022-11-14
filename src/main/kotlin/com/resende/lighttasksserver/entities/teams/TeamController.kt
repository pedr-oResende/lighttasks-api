package com.resende.lighttasksserver.entities.teams

import com.resende.lighttasksserver.entities.basic_user.BasicUserController
import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.tasks.TaskController
import com.resende.lighttasksserver.entities.teams.model.Team
import com.resende.lighttasksserver.entities.teams.model.TeamDTO
import com.resende.lighttasksserver.entities.teams.model.TeamResponse
import com.resende.lighttasksserver.model.Status
import com.resende.lighttasksserver.utils.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/teams")
class TeamController {

    @Autowired
    val teamRepository: TeamRepository? = null

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @GetMapping
    fun getTeams(): List<TeamDTO> {
        val teams = teamRepository?.findAll() ?: emptyList()
        return teams.map { entityToDTO(it) }
    }
    @GetMapping("/{id}")
    fun getTeamsByUser(@PathVariable id: Long): List<TeamDTO> {
        val teams = basicUserRepository?.findById(id)?.get()?.teams ?: emptyList()
        return teams.map { entityToDTO(it) }
    }

    @PostMapping
    fun registerTeams(@RequestBody newTeam: @Valid Team?): Status? {
        if (newTeam?.members?.map { it.id }
                ?.contains(newTeam.leader_id) == false || newTeam?.leader_id == null) return Status.FAILURE
        val members = newTeam.members?.map { basicUser ->
            basicUserRepository?.findById(basicUser.id)?.get() ?: return Status.FAILURE
        }?.toSet()
        val team = newTeam.copy(
            createdAt = DateTimeUtils.getDate(),
            members = members
        )
        teamRepository?.save(team)
        return Status.SUCCESS
    }

    @PutMapping
    fun editTeam(@RequestBody newTeam: @Valid Team?): TeamResponse? {
        if (newTeam?.id == null) return TeamResponse(
            team = null,
            status = Status.FAILURE
        )
        val team = teamRepository?.findById(newTeam.id)?.get() ?: return TeamResponse(
            team = null,
            status = Status.FAILURE
        )
        teamRepository?.save(
            with(newTeam) {
                team.copy(
                    name = name,
                    leaderId = leader_id
                )
            }
        )
        return TeamResponse(
            team = newTeam,
            status = Status.SUCCESS
        )
    }

    @DeleteMapping("/remove/{id}")
    fun deleteTeam(@PathVariable id: Long): Status? {
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
                    tasks = tasks?.map { TaskController.entityToDTO(it) }?.toSet(),
                    members = members?.map {  basicUser ->
                        val tasks = basicUser.tasks?.toMutableSet()
                        tasks?.removeIf { it.team?.id != id }
                        BasicUserController.entityToDTO(basicUser.copy(tasks = tasks))
                    }?.toSet()
                )
            }
    }
}