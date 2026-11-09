package org.ldv.todoapp.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "utilisateur")
open class Utilisateur(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    open var idUtilisateur: Long? = null,

    @Column(name = "nom", nullable = false, length = 100)
    open var nom: String = "",

    @Column(name = "prenom", nullable = false, length = 100)
    open var prenom: String = "",

    @Column(name = "email", nullable = false, unique = true, length = 255)
    open var email: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_role", referencedColumnName = "id_role", nullable = false)
    open var role: Role? = null,

    @Column(name = "reference_demo", unique = true, length = 50)
    open var referenceDemo: String? = null
)