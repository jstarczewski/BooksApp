package com.jstarczewski.booksapp.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow
import moe.tlaster.precompose.molecule.collectAction

class UserModel(
    val all: List<DomainUser>,
    val current: DomainUser?
)

sealed interface UserAction {

    data class SetSelectedUser(val id: Long) : UserAction

    data class AddUser(val name: String, val secondName: String, val role: Role) : UserAction
}

enum class Role(val key: String, val id: Int) {

    EDITOR("editor", 2),
    READER("reader", 1),
    AUTHOR("author", 3),
    UNKNOWN("unknown", Int.MAX_VALUE)
}

@Composable
fun UserPresenter(
    userRepository: UserRepository,
    action: Flow<UserAction>
): UserModel {

    val users by userRepository.users.collectAsState(emptyList())
    val selectedUser by userRepository.selectedUser.collectAsState(null)

    action.collectAction {
        when (this) {
            is UserAction.AddUser -> userRepository.addNewUser(name, secondName, role.id)
            is UserAction.SetSelectedUser -> userRepository.setSelectedUser(id)
        }
    }

    return UserModel(
        current = selectedUser,
        all = users
    )
}