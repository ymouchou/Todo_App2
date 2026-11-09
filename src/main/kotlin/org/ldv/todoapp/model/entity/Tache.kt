package org.ldv.todoapp.model.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "tache")
class Tache(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tache")
    open var idTache: Long? = null,

    @Column(name = "titre", nullable = false, length = 255)
    open var titre: String = "",

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    open var description: String? = null,

    @Column(name = "date_creation", nullable = false)
    open var dateCreation: LocalDateTime = LocalDateTime.now(),

    @Column(name = "date_echeance", nullable = true)
    open var dateEcheance: LocalDate? = null,

    @Column(name = "terminee", nullable = false)
    open var terminee: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "fkid_utilisateur", referencedColumnName = "id_utilisateur", nullable = false)
    open var utilisateur: Utilisateur? = null,

    @Column(name = "reference_demo", unique = true, length = 50)
    open var referenceDemo: String? = null
)