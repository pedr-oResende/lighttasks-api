package com.resende.lighttasksserver.entities.user.model

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    val id: Long,
    @NotBlank val username: String?,
    @NotBlank val full_name: String?,
    @NotBlank val password: String?,
    @NotBlank val logged_in: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return Objects.equals(username, other.username)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    fun copy(
        id: Long = this.id,
        username: String? = this.username,
        full_name: String? = this.full_name,
        password: String? = this.password,
        loggedIn: Boolean? = this.logged_in
    ) = User(
        id = id,
        username = username,
        full_name = full_name,
        password = password,
        logged_in = loggedIn
    )

}