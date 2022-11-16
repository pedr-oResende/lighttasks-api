package com.resende.lighttasksserver.entities.tasks.model

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "tasks")
class Task(
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    val id: Long,

    @NotBlank
    val name: String?,

    @NotBlank
    val description: String?,

    @NotBlank
    val created_at: String?,

    @NotBlank val
    deadline: String?,

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "responsible_id")
    val responsible: BasicUser?,

    @NotBlank
    val team_id: Long?,

    @NotBlank
    val is_done: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Task

        return name == other.name
                && description == other.description
                && created_at == other.created_at
                && deadline == other.deadline
                && responsible == other.responsible
                && is_done == other.is_done
    }

    override fun hashCode(): Int = javaClass.hashCode()

    fun copy(
        id: Long = this.id,
        name: String? = this.name,
        description: String? = this.description,
        createdAt: String? = this.created_at,
        deadline: String? = this.deadline,
        responsible: BasicUser? = this.responsible,
        team_id: Long? = this.team_id,
        is_done: Boolean? = this.is_done
    ) = Task(
        id = id,
        name = name,
        description = description,
        created_at = createdAt,
        deadline = deadline,
        responsible = responsible,
        team_id = team_id,
        is_done = is_done
    )
}
