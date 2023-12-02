package com.jstarczewski.booksapp.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.molecule.producePresenter
import moe.tlaster.precompose.molecule.rememberPresenter
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.RouteBuilder
import kotlin.math.acos

const val USER_ROUTE = "user"

fun RouteBuilder.userScene(
    userRepository: UserRepository
) {
    scene(USER_ROUTE) {
        UserScene(
            userRepository
        )
    }
}

@Composable
fun UserScene(
    userRepository: UserRepository
) {

    val (presenter, action) = rememberPresenter { UserPresenter(userRepository, it) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        AddUser { name, secondName, role ->
            action.trySend(UserAction.AddUser(name, secondName, role))
        }
        presenter.current?.let {
            CurrentUser(it.name, it.secondName, it.roleId?.toString().orEmpty())
        }
        LazyColumn {
            items(presenter.all) {
                User(it.name, it.secondName, it.roleId?.toString().orEmpty()) {
                    action.trySend(UserAction.SetSelectedUser(it.id))
                }
            }
        }
    }
}

@Composable
fun ColumnScope.AddUser(
    onAdd: (name: String, secondName: String, role: Role) -> Unit
) {

    val name = remember { mutableStateOf("") }
    val secondName = remember { mutableStateOf("") }

    TextField(modifier = Modifier.padding(40.dp), value = name.value, onValueChange = {
        name.value = it

    }, placeholder = {
        Text("Name")
    })
    TextField(modifier = Modifier.padding(40.dp), value = secondName.value, onValueChange = {
        secondName.value = it
    }, placeholder = {
        Text("Second Name")
    })
    Row {
        Button(onClick = {
            onAdd(
                name.value,
                secondName.value,
                Role.READER
            )
        }) {
            Text("Add as Reader")
        }
        Button(onClick = {
            onAdd(
                name.value,
                secondName.value,
                Role.EDITOR
            )
        }) {
            Text("Add as Editor")
        }
        Button(onClick = {
            onAdd(
                name.value,
                secondName.value,
                Role.AUTHOR
            )
        }) {
            Text("Add as Author")
        }
    }
}

@Composable
fun CurrentUser(
    name: String,
    secondName: String,
    role: String
) {
    Text(name)
    Text(secondName)
    Text(role)
}

@Composable
fun User(
    name: String,
    secondName: String,
    role: String,
    onUser: () -> Unit
) {
    Text(modifier = Modifier.clickable { onUser() }, text = name)
    Text(secondName)
    Text(role)
}