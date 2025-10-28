package com.example.unitrack.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data class to hold subject information
data class Subject(
    val id: Long = System.currentTimeMillis(), // Unique ID for each subject
    var name: String = "",
    var code: String = "",
    var credits: String = ""
)

// A simple bottom navigation bar for the main features
@Composable
fun AppBottomNavigationBar(navController: NavController, currentScreen: String) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Calculate, contentDescription = "GPA Calculator") },
            label = { Text("GPA") },
            selected = currentScreen == "gpa_calculator",
            onClick = { navController.navigate("gpa_calculator") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Assignments") },
            label = { Text("Assignments") },
            selected = currentScreen == "assignments",
            onClick = { navController.navigate("assignments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Grade, contentDescription = "Lost & Found") },
            label = { Text("Lost & Found") },
            selected = currentScreen == "lost_and_found",
            onClick = { navController.navigate("lost_and_found") }
        )
    }
}

@Composable
fun GpaDisplayCard(currentGpa: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Current GPA",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "%.2f".format(currentGpa),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun YearSelectionGrid(onYearSelected: (String) -> Unit) {
    val years = listOf("First Year", "Second Year", "Third Year", "Fourth Year")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(years) { year ->
            Card(
                modifier = Modifier
                    .aspectRatio(1.5f)
                    .clickable { onYearSelected(year) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = year, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun SemesterSelectionScreen(year: String, onSemesterSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = year, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSemesterSelected(1) }, modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            Text("Semester 1", fontSize = 16.sp)
        }
        Button(onClick = { onSemesterSelected(2) }, modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            Text("Semester 2", fontSize = 16.sp)
        }
    }
}

@Composable
fun SubjectRow(
    subject: Subject,
    onSubjectChange: (Subject) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = subject.name,
            onValueChange = { onSubjectChange(subject.copy(name = it)) },
            label = { Text("Subject Name") },
            modifier = Modifier.weight(0.4f)
        )
        OutlinedTextField(
            value = subject.code,
            onValueChange = { onSubjectChange(subject.copy(code = it)) },
            label = { Text("Code") },
            modifier = Modifier.weight(0.3f)
        )
        OutlinedTextField(
            value = subject.credits,
            onValueChange = { onSubjectChange(subject.copy(credits = it)) },
            label = { Text("Credits") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(0.3f)
        )
    }
}


// Screen where users will add subjects for a specific semester
@Composable
fun SubjectEntryScreen(
    year: String,
    semester: Int,
    subjects: SnapshotStateList<Subject>,
    onAddSubject: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            Text(
                text = "$year - Semester $semester",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        itemsIndexed(subjects) { index, subject ->
            SubjectRow(
                subject = subject,
                onSubjectChange = { updatedSubject ->
                    subjects[index] = updatedSubject
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaCalculatorScreen(navController: NavController) {
    // State to manage which view is shown: "years", "semesters", or "subjects"
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedSemester by remember { mutableStateOf<Int?>(null) }

    // This state will hold the list of subjects for the selected semester
    val subjects = remember { mutableStateListOf<Subject>() }

    // This will be calculated later based on user input
    val currentGpa by remember { mutableStateOf(3.85f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            selectedYear == null -> "GPA Calculator"
                            selectedSemester == null -> selectedYear!!
                            else -> "Semester $selectedSemester"
                        }
                    )
                },
                navigationIcon = {
                    // Show back button if we are not on the main year selection screen
                    if (selectedYear != null) {
                        IconButton(onClick = {
                            if (selectedSemester != null) {
                                selectedSemester = null // Go back to semester selection
                                subjects.clear() // Clear subjects when leaving
                            } else {
                                selectedYear = null // Go back to year selection
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            // Only show the main bottom navigation on the primary GPA screen
            if (selectedYear == null) {
                AppBottomNavigationBar(navController = navController, currentScreen = "gpa_calculator")
            }
        },
        floatingActionButton = {
            // Show FAB only on the subject entry screen
            if (selectedYear != null && selectedSemester != null) {
                FloatingActionButton(onClick = { subjects.add(Subject()) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Subject")
                }

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp), // Add some overall padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                // State 1: Show Year Selection Grid
                selectedYear == null -> {
                    GpaDisplayCard(currentGpa = currentGpa)
                    Spacer(modifier = Modifier.height(16.dp))
                    YearSelectionGrid(onYearSelected = { year ->
                        selectedYear = year
                    })
                }
                // State 2: Show Semester Selection for the chosen year
                selectedSemester == null -> {
                    SemesterSelectionScreen(
                        year = selectedYear!!,
                        onSemesterSelected = { semester ->
                            selectedSemester = semester
                            subjects.clear() // Clear list for new semester
                            subjects.add(Subject()) // Start with one empty row
                        }
                    )
                }
                // State 3: Show the screen for adding subjects
                else -> {
                    SubjectEntryScreen(
                        year = selectedYear!!,
                        semester = selectedSemester!!,
                        subjects = subjects,
                        onAddSubject = { subjects.add(Subject()) }
                    )
                }
            }
        }
    }
}
