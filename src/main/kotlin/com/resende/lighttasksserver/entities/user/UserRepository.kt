package com.resende.lighttasksserver.entities.user

import com.resende.lighttasksserver.entities.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>