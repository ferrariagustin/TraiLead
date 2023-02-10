package com.aferrari.login.db

class UserRepository(private val dao: UserDao) {

    private lateinit var trainees: List<Trainee>
    private lateinit var leaders: List<Leader>

    suspend fun get(user_id: Int): User? {
        dao.getLeader(user_id).apply {
            if (this == null) {
                dao.getTrainee(user_id).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String): User? {
        dao.getLeader(user_email).apply {
            if (this == null) {
                dao.getTrainee(user_email).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun get(user_email: String, user_pass: String): User? {
        dao.getLeader(user_email, user_pass).apply {
            if (this == null) {
                dao.getTrainee(user_email, user_pass).let {
                    return it
                }
            } else {
                return this
            }
        }
    }

    suspend fun insertLeader(leader: Leader): Long = dao.insertLeader(leader)

    suspend fun insertTrainee(trainee: Trainee): Long = dao.insertTrainee(trainee)

    suspend fun updateLeader(leader: Leader) = dao.updateLeader(leader)

    suspend fun updateTrainee(trainee: Trainee) = dao.updateTrainee(trainee)

    suspend fun updateTraineeName(idTrainee: Int, name: String) =
        dao.updateTraineeName(idTrainee, name)

    suspend fun updateTraineeLastName(idTrainee: Int, lastName: String) =
        dao.updateTraineeLastName(idTrainee, lastName)

    suspend fun deleteLeader(leader: Leader) {
        dao.deleteLeader(leader)
    }

    suspend fun deleteTrainee(trainee: Trainee) {
        dao.deleteTrainee(trainee)
    }

    suspend fun deleteAllLeader() {
        dao.deleteAllLeader()
    }

    suspend fun deleteAllTrainee() {
        dao.deleteAllTrainee()
    }

    suspend fun getAllTrainee(): List<Trainee> {
        trainees = dao.getAllTrainee()
        return trainees
    }

    suspend fun getAllLeader(): List<Leader> {
        leaders = dao.getAllLeader()
        return leaders
    }

    suspend fun setLinkedTrainee(trainee: Trainee, leader: Leader) {
        dao.setLinkedTrainee(trainee.id, leader.id)
    }

    suspend fun getUnlinkedTrainees(): List<Trainee> {
        return dao.getUnlinkedTrainees()
    }

    suspend fun getLinkedTrainees(leader: Leader): List<Trainee> = dao.getLinkedTrainees(leader.id)

    suspend fun setUnlinkedTrainee(trainee: Trainee) = dao.setUnlinkedTrainee(trainee.id)

    suspend fun updateTraineePassword(password: String, id: Int) =
        dao.updateTraineePassword(password, id)

    suspend fun updateTraineePosition(trainee: Trainee, position: Position) =
        dao.updateTraineePosition(trainee.id, position)

    suspend fun updateLeaderName(leaderId: Int, name: String) = dao.updateLeaderName(leaderId, name)

    suspend fun updateLeaderLastName(leaderId: Int, lastName: String) =
        dao.updateLeaderLastName(leaderId, lastName)

    suspend fun updateLeaderPass(leaderId: Int, pass: String) =
        dao.updateLeaderPassword(leaderId, pass)

    //    Category

    suspend fun insertCategory(category: Category) = dao.insertCategory(category)

    suspend fun getAllCategory(leader: Leader) = dao.getAllCategory(leader.id)

    suspend fun deleteCategory(category: Category) = dao.deleteCategory(category)

    suspend fun updateCategory(categoryId: Int, categoryName: String) =
        dao.updateCategory(categoryId, categoryName)


    //    Material

    suspend fun insertMaterial(newMaterial: Material) = dao.insertMaterial(newMaterial)

    suspend fun getAllMaterial(leader: Leader) = dao.getAllMaterial(leader.id)

    suspend fun deleteMaterial(material: Material) = dao.deleteMaterial(material)

}
