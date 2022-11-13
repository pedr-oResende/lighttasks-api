package com.resende.lighttasksserver.entities.tasks.model

import com.resende.lighttasksserver.entities.basic_user.model.BasicUser
import com.resende.lighttasksserver.entities.teams.model.Team
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
    val instructions: String?,

    @NotBlank
    val created_at: String?,

    @NotBlank val
    deadline: String?,

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "responsible_id")
    val responsible: BasicUser?,

    @NotBlank
    @ManyToOne
    val team: Team?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Task

        return name == other.name
                && instructions == other.instructions
                && created_at == other.created_at
                && deadline == other.deadline
                && responsible == other.responsible
    }

    override fun hashCode(): Int = javaClass.hashCode()

    fun copy(
        id: Long = this.id,
        name: String? = this.name,
        instructions: String? = this.instructions,
        createdAt: String? = this.created_at,
        deadline: String? = this.deadline,
        responsible: BasicUser? = this.responsible,
        team: Team? = this.team
    ) = Task(
        id = id,
        name = name,
        instructions = instructions,
        created_at = createdAt,
        deadline = deadline,
        responsible = responsible,
        team = team
    )
}
