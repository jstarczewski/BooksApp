package com.jstarczewski.booksapp.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jstarczewski.booksapp.User
import com.jstarczewski.booksapp.WolneLekturyDatabse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class DomainUser(
    val id: Long,
    val name: String,
    val secondName: String,
    val roleId: Int?
)

fun User.asDomainUser() =
    DomainUser(
        id = Id,
        name = Name,
        secondName = SecondName,
        roleId = RoleId?.toInt()
    )

class UserRepository(
    private val wolneLekturyDatabse: WolneLekturyDatabse,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    val users: Flow<List<DomainUser>> = wolneLekturyDatabse.userQueries.getUsers()
        .asFlow()
        .mapToList(dispatcher)
        .map {
            it.map {
                it.asDomainUser()
            }
        }

    val selectedUser: Flow<DomainUser?> = wolneLekturyDatabse.userQueries.getSelectedUser()
        .asFlow()
        .mapToList(dispatcher)
        .map { ids ->
            when {
                ids.size > 1 -> throw IllegalStateException("Cannot be two users selected")
                ids.isEmpty() -> null
                else -> ids.first()
            }?.asDomainUser()
        }

    fun setSelectedUser(id: Long) {
        wolneLekturyDatabse.userQueries.setSelectedUser(id.toLong())
    }

    fun addNewUser(
        name: String,
        secondName: String,
        roleId: Int
    ) {
        wolneLekturyDatabse.userQueries.addNewUser(
            name, secondName, roleId.toLong()
        )
    }
}