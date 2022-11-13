package com.resende.lighttasksserver.entities.user

import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.basic_user.model.BasicUserResponse
import com.resende.lighttasksserver.entities.user.model.User
import com.resende.lighttasksserver.model.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    val userRepository: UserRepository? = null

    @Autowired
    val basicUserRepository: BasicUserRepository? = null

    @GetMapping
    fun getUsers() = userRepository?.findAll() ?: emptyList()

    @PostMapping("/register")
    fun registerUser(@RequestBody newUser: @Valid User?): Status? {
        if (newUser == null) return Status.FAILURE
        val users = userRepository?.findAll() ?: emptyList()
        println("New user: $newUser")
        for (user in users) {
            println("Registered user: $newUser")
            if (user.equals(newUser)) {
                println("User Already exists!")
                return Status.USER_ALREADY_EXISTS
            }
        }
        basicUserRepository?.save(
            BasicUser(
                id = newUser.id,
                username = newUser.username,
                teams = setOf(),
                tasks = setOf()
            )
        )
        userRepository?.save(newUser.copy(loggedIn = false))
        return Status.SUCCESS
    }

    @PutMapping
    fun editUser(@RequestBody newUser: @Valid User?): Status? {
        if (newUser == null) return Status.FAILURE
        val user =
            userRepository?.findAll()?.toList()?.first { it.username == newUser.username } ?: return Status.FAILURE
        userRepository?.save(
            user.copy(
                username = newUser.username,
                password = newUser.password,
                loggedIn = newUser.loggedIn
            )
        )
        return Status.SUCCESS
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody user: @Valid User?): BasicUserResponse? {
        val users = userRepository?.findAll() ?: emptyList()
        for (other in users) {
            if (user != null && other.equals(user)) {
                editUser(user.copy(loggedIn = true))
                val userResponse = basicUserRepository?.findAll()?.toList()?.first { it.username == user.username }
                return BasicUserResponse(
                    basicUser = userResponse,
                    status = Status.SUCCESS
                )
            }
        }
        return BasicUserResponse(
            basicUser = null,
            status = Status.FAILURE
        )
    }

    @PostMapping("/logout")
    fun logUserOut(@RequestBody user: @Valid User?): Status? {
        val users = userRepository?.findAll() ?: emptyList()
        for (other in users) {
            if (user != null && other.equals(user)) {
                editUser(user.copy(loggedIn = false))
                return Status.SUCCESS
            }
        }
        return Status.FAILURE
    }

    @DeleteMapping("delete_all")
    fun deleteUsers(): Status {
        userRepository?.deleteAll()
        basicUserRepository?.deleteAll()
        return Status.SUCCESS
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: Long): Status {
        userRepository?.deleteById(id)
        basicUserRepository?.deleteById(id)
        return Status.SUCCESS
    }
}