package com.resende.lighttasksserver.entities.basic_user

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.basic_user.model.BasicUserDTO
import com.resende.lighttasksserver.entities.basic_user.model.BasicUserResponse
import com.resende.lighttasksserver.entities.tasks.TaskController
import com.resende.lighttasksserver.entities.teams.TeamController
import com.resende.lighttasksserver.model.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/basic_users")
class BasicUserController {

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @GetMapping
    fun getBasicUsers(): List<BasicUserDTO>? {
        val users = basicUserRepository?.findAll() ?: emptyList()
        return users.map { basicUser ->
            entityToDTO(basicUser)
        }
    }

    @GetMapping("/{id}")
    fun getBasicUserById(@PathVariable id: Long): BasicUserDTO? {
        val user = basicUserRepository?.findById(id)?.get() ?: return null
        return entityToDTO(user)
    }

    @PutMapping
    fun editBasicUser(@RequestBody newUser: @Valid BasicUser?): Status? {
        if (newUser == null) return Status.FAILURE
        val user =
            basicUserRepository?.findAll()?.toList()?.first { it.username == newUser.username } ?: return Status.FAILURE
        basicUserRepository?.save(
            user.copy(
                username = newUser.username,
                tasks = newUser.tasks,
                teams = newUser.teams
            )
        )
        return Status.SUCCESS
    }

    @DeleteMapping("/remove/{id}")
    fun deleteBasicUser(@PathVariable id: Long): Status? {
        if (basicUserRepository?.findAll()?.toList()?.map { it.id }?.contains(id) == true) {
            return Status.FAILURE
        }
        basicUserRepository?.deleteById(id)
        return Status.SUCCESS
    }

    companion object {
        fun entityToDTO(basicUser: BasicUser) =
            with(basicUser) {
                BasicUserDTO(
                    id = id,
                    username = username,
                    tasks = tasks?.map { TaskController.entityToDTO(it) }?.toSet(),
                    teams = teams?.map { TeamController.entityToDTO(it) }?.toSet()
                )
            }
    }
}