package org.ldv.todoapp.model.repository

import org.ldv.todoapp.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UtilisateurRepository : JpaRepository<Utilisateur, Long> {

    fun findByEmail(email: String): Utilisateur?

    @Query("""
        SELECT u
        FROM Utilisateur u
        JOIN FETCH u.role
        WHERE u.idUtilisateur = :id
    """)
    fun findByIdAvecRole(@Param("id") id: Long): Utilisateur?

    @Query("""
        SELECT u
        FROM Utilisateur u
        JOIN FETCH u.role
        ORDER BY u.nom, u.prenom
    """)
    fun findAllAvecRole(): List<Utilisateur>

    fun findByReferenceDemo(referenceDemo: String): Utilisateur?
}
