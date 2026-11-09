package org.ldv.todoapp.config

import org.ldv.todoapp.model.entity.Role
import org.ldv.todoapp.model.entity.Utilisateur
import org.ldv.todoapp.model.entity.Tache
import org.ldv.todoapp.model.repository.RoleRepository
import org.ldv.todoapp.model.repository.UtilisateurRepository
import org.ldv.todoapp.model.repository.TacheRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class DataInitializer(
    private val roleRepository: RoleRepository,
    private val utilisateurRepository: UtilisateurRepository,
    private val tacheRepository: TacheRepository
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String) {
        println("Vérification des données de démonstration TodoApp...")
        var rolesAjoutes = 0
        var utilisateursAjoutes = 0
        var tachesAjoutees = 0

        // Un rôle existant est conservé. Seuls les rôles manquants sont créés.
        fun initialiserRole(nom: String): Role {
            return roleRepository.findByNom(nom) ?: roleRepository.save(Role(nom = nom)).also {
                rolesAjoutes++
            }
        }

        fun initialiserUtilisateur(reference: String, donnees: Utilisateur): Utilisateur {
            utilisateurRepository.findByReferenceDemo(reference)?.let { return it }
            // Reprise du jeu fourni au TP 1, qui ne possédait pas encore de repère.
            val existant = utilisateurRepository.findByEmail(donnees.email)
            if (existant != null) {
                check(existant.referenceDemo == null) {
                    "L'email ${donnees.email} est déjà associé à une autre référence de démonstration."
                }
                existant.referenceDemo = reference
                return utilisateurRepository.save(existant)
            }
            donnees.referenceDemo = reference
            utilisateursAjoutes++
            return utilisateurRepository.save(donnees)
        }

        val roleUser = initialiserRole("ROLE_USER")
        val roleAdmin = initialiserRole("ROLE_ADMIN")

        // 2. Création des utilisateurs
        val alice = initialiserUtilisateur("alice",
            Utilisateur(
                nom = "Martin",
                prenom = "Alice",
                email = "alice@example.com",
                role = roleUser
            )
        )
        val bob = initialiserUtilisateur("bob",
            Utilisateur(
                nom = "Durand",
                prenom = "Bob",
                email = "bob@example.com",
                role = roleUser
            )
        )
        val claire = initialiserUtilisateur("claire",
            Utilisateur(
                nom = "Bernard",
                prenom = "Claire",
                email = "claire@example.com",
                role = roleUser
            )
        )
        val admin = initialiserUtilisateur("admin",
            Utilisateur(
                nom = "Admin",
                prenom = "TodoApp",
                email = "admin@example.com",
                role = roleAdmin
            )
        )

        // 3. Création des tâches
        val aujourdHui = LocalDate.now()
        val taches = listOf(
            Tache(
                referenceDemo = "alice-spring-boot",
                titre = "Préparer le TP Spring Boot",
                description = "Rédiger le support de cours MVC.",
                dateEcheance = aujourdHui.plusDays(7),
                utilisateur = alice
            ),
            Tache(
                referenceDemo = "alice-intellij",
                titre = "Installer IntelliJ IDEA",
                description = "Configurer l'environnement Kotlin.",
                dateEcheance = aujourdHui.minusDays(2),
                terminee = true,
                utilisateur = alice
            ),
            Tache(
                referenceDemo = "bob-sql",
                titre = "Réviser les requêtes SQL",
                dateEcheance = aujourdHui.plusDays(3),
                utilisateur = bob
            ),
            Tache(
                referenceDemo = "bob-jdbc",
                titre = "Terminer le projet JDBC",
                description = "Finaliser les méthodes DAO.",
                dateEcheance = aujourdHui.minusDays(5),
                utilisateur = bob
            ),
            Tache(
                referenceDemo = "claire-jpa",
                titre = "Étudier les annotations JPA",
                dateEcheance = aujourdHui.plusDays(10),
                utilisateur = claire
            ),
            Tache(
                referenceDemo = "claire-thymeleaf",
                titre = "Lire la documentation Thymeleaf",
                terminee = true,
                utilisateur = claire
            ),
            Tache(
                referenceDemo = "admin-comptes",
                titre = "Vérifier les comptes utilisateurs",
                dateEcheance = aujourdHui.plusDays(1),
                utilisateur = admin
            ),
            Tache(
                referenceDemo = "admin-taches",
                titre = "Contrôler les tâches enregistrées",
                utilisateur = admin
            )
        )

        for (donnees in taches) {
            val reference = requireNotNull(donnees.referenceDemo)
            if (tacheRepository.findByReferenceDemo(reference) != null) {
                continue // Conserver tous les champs d'une tâche déjà identifiée.
            }
            // Reprise des tâches du TP 1, reconnues par propriétaire et titre initial.
            val utilisateurId = requireNotNull(donnees.utilisateur?.idUtilisateur)
            val existante = tacheRepository
                .findFirstByUtilisateur_IdUtilisateurAndTitreAndReferenceDemoIsNullOrderByIdTacheAsc(
                    utilisateurId, donnees.titre
                )
            if (existante != null) {
                existante.referenceDemo = reference
                tacheRepository.save(existante)
            } else {
                tacheRepository.save(donnees)
                tachesAjoutees++
            }
        }

        println("Vérification terminée :")
        println("- $rolesAjoutes rôle(s) ajouté(s)")
        println("- $utilisateursAjoutes utilisateur(s) ajouté(s)")
        println("- $tachesAjoutees tâche(s) ajoutée(s)")
    }
}