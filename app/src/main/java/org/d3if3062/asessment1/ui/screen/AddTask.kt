package org.d3if3062.asessment1.ui.screen

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3062.asessment1.R
import org.d3if3062.asessment1.model.ListTaskModel
import org.d3if3062.asessment1.ui.theme.Asessment1Theme


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController, viewModel: ListTaskModel) {
    var taskTitle by rememberSaveable { mutableStateOf("") }
    var deadLine by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var emptyTitle by rememberSaveable { mutableStateOf(false) }
    var emptyDeadLine by rememberSaveable { mutableStateOf(false) }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.add_task_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() })
                    {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = taskTitle,
                singleLine = true,
                isError = emptyTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Add Title") },
                trailingIcon = {ErrorIcon(emptyTitle)},
                supportingText = {ErrorHint(emptyTitle)},
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.assignment), contentDescription = null)
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = deadLine,
                singleLine = true,
                isError = emptyDeadLine,
                onValueChange = { deadLine = it },
                label = { Text("DD/MM/YYYY") },
                trailingIcon = {ErrorIcon(emptyTitle)},
                supportingText = {ErrorHint(emptyTitle)},
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.assignment), contentDescription = null)
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description,
                singleLine = false,
                isError = emptyDeadLine,
                onValueChange = { description = it },
                label = { Text("To do Description") },
                trailingIcon = {ErrorIcon(emptyTitle)},
                supportingText = {ErrorHint(emptyTitle)},
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.assignment), contentDescription = null)
                }
            )

            Button(
                onClick = {
                    emptyTitle = (taskTitle == "" || taskTitle == "0")
                    emptyDeadLine = (deadLine == "" || deadLine == "0")
                    if (emptyTitle || emptyDeadLine) return@Button

                    // Memanggil fungsi addTodo dari ViewModel
                    viewModel.addTodo(
                        judul = taskTitle,
                        deadLine = deadLine,
                        description = description,
                        status = false,
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.add_task))
            }



        }
    }

}

/*----------------[Exception]----------------*/
@Composable
fun ErrorHint(isError: Boolean){
    if (isError){
        Text(
            text = stringResource(id = R.string.invalid_input),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
        )
    }
}
@Composable
fun ErrorIcon(isError: Boolean){
    if (isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}
/*----------------[Exception]----------------*/

//@Preview(showBackground = true)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun AddTaskPreview() {
//    Asessment1Theme {
//        AddTaskScreen(navController = rememberNavController(), )
//    }
//}
