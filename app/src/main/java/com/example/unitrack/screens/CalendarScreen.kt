package com.example.unitrack.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.privacysandbox.tools.core.generator.build
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.unitrack.notifications.ReminderWorker
import io.github.kizitonwose.calendar.compose.HorizontalCalendar
import io.github.kizitonwose.calendar.compose.rememberCalendarState
import io.github.kizitonwose.calendar.core.CalendarDay
import io.github.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

// Data class to store reminder info
data class Reminder(val id: String, val date: LocalDate, val title: String)

// Main screen composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val context = LocalContext.current

    // State for the list of reminders
    val reminders = remember { mutableStateListOf<Reminder>() }
    // State to hold the currently selected date
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    // State to control the "Add Reminder" dialog
    var showDialog by remember { mutableStateOf(false) }

    // ---- Notification Permission Handling ----
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "Notification permission is required for reminders.", Toast.LENGTH_LONG).show()
            }
        }
    )

    // On first launch, check and request permission if needed
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    // ---- End of Permission Handling ----

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar & Reminders") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Calendar(
                selectedDate = selectedDate,
                reminders = reminders,
                onDateSelected = { selectedDate = it }
            )
            RemindersList(
                reminders = reminders.filter { it.date == selectedDate }
            )
        }
    }

    if (showDialog) {
        AddReminderDialog(
            date = selectedDate,
            onDismiss = { showDialog = false },
            onConfirm = { title ->
                val newReminder = Reminder(
                    id = System.currentTimeMillis().toString(),
                    date = selectedDate,
                    title = title
                )
                reminders.add(newReminder)
                scheduleReminder(context, newReminder)
                showDialog = false
            }
        )
    }
}


@Composable
fun Calendar(
    selectedDate: LocalDate,
    reminders: List<Reminder>,
    onDateSelected: (LocalDate) -> Unit
) {
    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(100)
    val endMonth = currentMonth.plusMonths(100)
    val firstDayOfWeek = firstDayOfWeekFromLocale()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = { day ->
            Day(
                day = day,
                isSelected = selectedDate == day.date,
                hasReminder = reminders.any { it.date == day.date }
            ) { onDateSelected(day.date) }
        },
        monthHeader = { month ->
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            Text(
                text = month.yearMonth.format(formatter),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, hasReminder: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else Color.LightGray,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (isSelected) Color.White else Color.Black
            )
            if (hasReminder) {
                // Small dot to indicate a reminder exists on this day
                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(if (isSelected) Color.White else MaterialTheme.colorScheme.secondary))
            }
        }
    }
}

@Composable
fun RemindersList(reminders: List<Reminder>) {
    Column {
        Text(
            text = "Reminders for this day",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (reminders.isEmpty()) {
            Text(
                "No reminders for this day.",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            LazyColumn {
                items(reminders) { reminder ->
                    ListItem(headlineContent = { Text(reminder.title) })
                }
            }
        }
    }
}

@Composable
fun AddReminderDialog(date: LocalDate, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Add Reminder", style = MaterialTheme.typography.headlineSmall)
                Text("Date: ${date.format(formatter)}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reminder Title") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { if (title.isNotBlank()) onConfirm(title) }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

fun scheduleReminder(context: Context, reminder: Reminder) {
    val workManager = WorkManager.getInstance(context)

    // Calculate the delay: from now until one day before the event
    val reminderDate = reminder.date.atStartOfDay()
    val now = LocalDate.now().atStartOfDay()
    val daysUntil = java.time.Duration.between(now, reminderDate).toDays()

    // Schedule only if the event is more than one day in the future
    if (daysUntil < 1) {
        Toast.makeText(context, "Reminder not set (date is today or in the past).", Toast.LENGTH_SHORT).show()
        return
    }

    val delayInDays = daysUntil - 1

    val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delayInDays, TimeUnit.DAYS)
        // Pass the title to the worker
        .setInputData(Data.Builder().putString(ReminderWorker.KEY_TITLE, reminder.title).build())
        .build()

    workManager.enqueue(workRequest)

    Toast.makeText(context, "Reminder set for '${reminder.title}'!", Toast.LENGTH_SHORT).show()
}
