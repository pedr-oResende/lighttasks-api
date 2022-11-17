package com.resende.lighttasksserver.entities.user

import com.resende.lighttasksserver.entities.basic_user.BasicUserController
import com.resende.lighttasksserver.entities.basic_user.BasicUserRepository
import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.basic_user.model.BasicUserDTO
import com.resende.lighttasksserver.entities.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userRepository?.findById(id)?.get() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody newUser: @Valid User?): HttpStatus {
        if (newUser == null) return HttpStatus.NOT_FOUND
        val users = userRepository?.findAll() ?: emptyList()
        if (users.contains(newUser)) {
            println("User Already exists!")
            return HttpStatus.ALREADY_REPORTED
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
        return HttpStatus.OK
    }

    @PutMapping("/{id}")
    fun editUser(@PathVariable("id") id: Long?, @RequestBody newUser: @Valid User?): HttpStatus {
        if (id == null) return HttpStatus.NOT_FOUND
        val user =
            userRepository?.findById(id)?.get() ?: return HttpStatus.NOT_FOUND
        val basicUser = basicUserRepository?.findById(id)?.get() ?: return HttpStatus.NOT_FOUND
        basicUserRepository?.save(
            basicUser.copy(
                username = newUser?.username
            )
        )
        userRepository?.save(
            user.copy(
                username = newUser?.username,
                password = newUser?.password
            )
        )
        return HttpStatus.OK
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody user: @Valid User?): ResponseEntity<BasicUserDTO>? {
        val users = userRepository?.findAll() ?: emptyList()
        return if (users.contains(user)) {
            editUser(user?.id , user?.copy(loggedIn = true))
            ResponseEntity(
                BasicUserController.entityToDTO(
                    basicUser = basicUserRepository?.findAll()?.toList()?.first { it.username == user?.username }
                        ?: return null
                ),
                HttpStatus.OK
            )
        } else {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/logout/{id}")
    fun logUserOut(@PathVariable id: Long): HttpStatus {
        val user = userRepository?.findById(id)?.get() ?: return HttpStatus.NOT_FOUND
        editUser(user.id , user.copy(loggedIn = false))
        return HttpStatus.OK
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: Long): HttpStatus {
        userRepository?.deleteById(id)
        basicUserRepository?.deleteById(id)
        return HttpStatus.OK
    }
}