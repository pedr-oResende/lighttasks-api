package com.resende.lighttasksserver.entities.teams

import com.resende.lighttasksserver.entities.basic_user.BasicUserController
import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.teams.model.Team
import com.resende.lighttasksserver.entities.teams.model.TeamDTO
import com.resende.lighttasksserver.utils.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getTeams(): ResponseEntity<List<TeamDTO>> {
        val teams = teamRepository?.findAll() ?: emptyList()
        return ResponseEntity(teams.map { entityToDTO(it) }, HttpStatus.OK)
    }

    @GetMapping("/{teamId}")
    fun getTeamsById(@PathVariable teamId: Long): ResponseEntity<TeamDTO> {
        val team = teamRepository?.findById(teamId)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(entityToDTO(team), HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    fun getTeamsByUser(@PathVariable userId: Long): ResponseEntity<List<TeamDTO>> {
        val teams = basicUserRepository?.findById(userId)?.get()?.teams ?: emptyList()
        return ResponseEntity(teams.map { entityToDTO(it) }, HttpStatus.OK)
    }

    @PostMapping
    fun registerTeams(@RequestBody newTeam: @Valid Team?): ResponseEntity<TeamDTO> {
        if (newTeam?.members?.map { it.id }
                ?.contains(newTeam.leader_id) == false || newTeam?.leader_id == null) return ResponseEntity(
            null,
            HttpStatus.NOT_FOUND
        )
        val members = newTeam.members?.map { basicUser ->
            basicUserRepository?.findById(basicUser.id)?.get() ?: return ResponseEntity(
                null,
                HttpStatus.NOT_FOUND
            )
        }?.toSet()
        val team = newTeam.copy(
            createdAt = DateTimeUtils.getDate(),
            members = members
        )
        createTeam(team, members)
        return ResponseEntity(entityToDTO(team), HttpStatus.OK)
    }

    private fun createTeam(team: Team, members: Set<BasicUser>?) {
        teamRepository?.save(team)
        members?.forEach { member ->
            basicUserRepository?.save(member.copy(teams = member.teams?.plus(team)))
        }
    }

    @PutMapping
    fun editTeam(@RequestBody newTeam: @Valid Team?): ResponseEntity<TeamDTO> {
        if (newTeam?.id == null) return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val team = teamRepository?.findById(newTeam.id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (newTeam.members?.map { it.id }?.contains(newTeam.leader_id) == false)
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val editedTeam = with(newTeam) {
            team.copy(
                name = name,
                leader_id = leader_id
            )
        }
        teamRepository?.save(editedTeam)
        return ResponseEntity(entityToDTO(editedTeam), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: Long): HttpStatus {
        teamRepository?.deleteById(id)
        return HttpStatus.OK
    }

    companion object {
        fun entityToDTO(team: Team) =
            with(team) {
                TeamDTO(
                    id = id,
                    name = name,
                    created_at = created_at,
                    leader_id = leader_id,
                    members = members?.map { basicUser ->
                        val tasks = basicUser.tasks?.toMutableSet()
                        tasks?.removeIf { it.team_id != id }
                        BasicUserController.entityToDTO(basicUser.copy(tasks = tasks))
                    }?.toSet()
                )
            }
    }
}