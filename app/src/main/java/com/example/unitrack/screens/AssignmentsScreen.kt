package com.example.unitrack.screens

import androidx.compose.foundation.layout.*
// Correct imports for Material 3 icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate // This is from the correct package for M3 extended icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Assignments Tracker") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    // This will now resolve correctly
                    icon = { Icon(Icons.Filled.Calculate, contentDescription = "GPA Calculator") },
                    label = { Text("GPA") },
                    selected = false,
                    onClick = { navController.navigate("gpa_calculator") }
                )
                NavigationBarItem(
                    // This will now resolve correctly
                    icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Assignments") },
                    label = { Text("Assignments") },
                    selected = true,
                    onClick = { /* Already here */ }
                )
                NavigationBarItem(
                    // This will now resolve correctly
                    icon = { Icon(Icons.Filled.Grade, contentDescription = "Lost & Found") },
                    label = { Text("Lost & Found") },
                    selected = false,
                    onClick = { navController.navigate("lost_and_found") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.ListAlt, contentDescription = null, modifier = Modifier.size(100.dp))
            Text(
                "Assignments Screen",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // ---- ADD THIS BUTTON ----
            Button(onClick = { navController.navigate("calendar") }) {
                Text("Open Calendar & Reminders")
            }
            // -----------------------
        }
    }
}
