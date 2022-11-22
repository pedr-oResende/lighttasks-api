package com.resende.lighttasksserver.entities.basic_user

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.basic_user.model.BasicUserDTO
import com.resende.lighttasksserver.entities.tasks.TaskController
import com.resende.lighttasksserver.entities.teams.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/basic_users")
class BasicUserController {

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @Autowired
    val teamRepository: TeamRepository? = null

    @GetMapping
    fun getBasicUsers(): ResponseEntity<List<BasicUserDTO>?> {
        val users = basicUserRepository?.findAll() ?: emptyList()
        return ResponseEntity(
            users.map { basicUser ->
                entityToDTO(basicUser)
            },
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}")
    fun getBasicUserById(@PathVariable id: Long): ResponseEntity<BasicUserDTO?> {
        val user = basicUserRepository?.findById(id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(entityToDTO(user), HttpStatus.OK)
    }

    @PutMapping
    fun editBasicUser(@RequestBody newUser: @Valid BasicUser?): ResponseEntity<BasicUserDTO?> {
        if (newUser?.id == null) return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val user = basicUserRepository?.findById(newUser.id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val editedUser = user.copy(username = newUser.username ?: user.username)
        basicUserRepository?.save(editedUser)
        return ResponseEntity(entityToDTO(editedUser), HttpStatus.OK)
    }

    @PutMapping("{newMemberId}/add_to_team/{teamId}")
    fun addMember(@PathVariable newMemberId: Long?, @PathVariable teamId: Long): HttpStatus {
        if (newMemberId == null) return HttpStatus.NOT_FOUND
        val member = basicUserRepository?.findById(newMemberId)?.get() ?: return HttpStatus.NOT_FOUND
        val team = teamRepository?.findById(teamId)?.get() ?: return HttpStatus.NOT_FOUND
        editBasicUser(member.copy(teams = member.teams?.plus(team)))
        return HttpStatus.OK
    }

    @PutMapping("{newMemberId}/remove_of_team/{teamId}")
    fun removeMember(@PathVariable newMemberId: Long?, @PathVariable teamId: Long): HttpStatus {
        if (newMemberId == null) return HttpStatus.NOT_FOUND
        val member = basicUserRepository?.findById(newMemberId)?.get() ?: return HttpStatus.NOT_FOUND
        val team = teamRepository?.findById(teamId)?.get() ?: return HttpStatus.NOT_FOUND
        editBasicUser(member.copy(teams = member.teams?.minus(team)))
        return HttpStatus.OK
    }

    companion object {
        fun entityToDTO(basicUser: BasicUser) =
            with(basicUser) {
                BasicUserDTO(
                    id = id,
                    username = username,
                    tasks = tasks?.map { TaskController.entityToDTO(it) }?.toSet(),
                    teams_id = teams?.map { it.id }?.toSet()
                )
            }
    }
}