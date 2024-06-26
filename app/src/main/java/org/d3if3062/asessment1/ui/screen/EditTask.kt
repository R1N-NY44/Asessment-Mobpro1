package org.d3if3062.asessment1.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3062.asessment1.R
import org.d3if3062.asessment1.model.ListTaskModel
import org.d3if3062.asessment1.ui.component.shareTodo
import org.d3if3062.asessment1.ui.theme.Asessment1Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(navController: NavHostController, viewModel: ListTaskModel, taskId: Long) {
    val task = viewModel.getTaskById(taskId)
    val context = LocalContext.current

    var taskTitle by rememberSaveable { mutableStateOf(task?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(task?.description ?: "") }
    var emptyTitle by rememberSaveable { mutableStateOf(false) }
    var emptyDeadLine by rememberSaveable { mutableStateOf(false) }

    val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val currentDate = format.parse(task?.deadLine) // Ubah string menjadi Date
    var selectedDate by remember { mutableStateOf(currentDate) }
    var dateString by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(currentDate)) }
    var selectedTime by remember { mutableStateOf(currentDate) }
    var timeString by remember { mutableStateOf(SimpleDateFormat("HH:mm").format(currentDate)) }
    var deadLine = "$dateString $timeString"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.edit_task_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteTodoById(taskId)
                        navController.popBackStack()
                        navController.popBackStack()
                    })
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                },
            )

        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column {
                // TextField untuk judul tugas
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    value = taskTitle,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { taskTitle = it },
                    label = { Text(stringResource(id = R.string.task_title)) },
                    isError = emptyTitle,
                    trailingIcon = { ErrorIcon(emptyTitle) },
                    supportingText = { ErrorHint(emptyTitle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.assignment),
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                )

                // Button untuk memilih tanggal dan waktu deadline
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            val calendar = Calendar.getInstance()
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val date = GregorianCalendar(year, month, dayOfMonth).time
                                    // Hanya memilih hari ini dan hari di depannya
                                    if (!date.before(currentDate)) {
                                        selectedDate = date
                                        dateString = SimpleDateFormat("dd/MM/yyyy").format(date)
                                    }
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.datePicker.minDate =
                                currentDate.time // Batasi tanggal minimum ke tanggal yang tersimpan
                            datePickerDialog.show()
                        }
                    ) {
                        Text(
                            text = dateString,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.date),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }


                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    val calendar = Calendar.getInstance()
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    val time = calendar.time
                                    selectedTime = time
                                    timeString = SimpleDateFormat("HH:mm").format(time)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = timeString,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.time),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 6.dp) // Atur padding untuk ikon
                            )
                        }
                    }
                }

                // TextField untuk deskripsi tugas
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    value = description,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    onValueChange = { description = it },
                    label = { Text(stringResource(id = R.string.task_description)) },
                    isError = emptyTitle,
                    trailingIcon = { ErrorIcon(emptyTitle) },
                    supportingText = { ErrorHint(emptyTitle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.description),
                            contentDescription = null
                        )
                    }
                )
            }

            // Button untuk menyimpan perubahan
            Button(
                onClick = {
                    emptyTitle = (taskTitle.isBlank())
                    emptyDeadLine = (deadLine.isBlank())
                    if (emptyTitle || emptyDeadLine) return@Button

                    task?.let {
                        // Memperbarui tugas dengan nilai yang diisi
                        viewModel.updateTodoById(
                            it.id,
                            taskTitle,
                            deadLine,
                            description,
                            it.status,
                            it.createAt

                        )
                    }
                    // Kembali ke layar sebelumnya
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.edit_task_title))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun EditTaskScreenPreview() {
    Asessment1Theme {
        val navController = rememberNavController()
        val viewModel = ListTaskModel()
        EditTaskScreen(navController, viewModel, 1)
    }
}
