package com.example.unitrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
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
fun LostAndFoundScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lost & Found") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Calculate, contentDescription = "GPA Calculator") },
                    label = { Text("GPA") },
                    selected = false,
                    onClick = { navController.navigate("gpa_calculator") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Assignments") },
                    label = { Text("Assignments") },
                    selected = false,
                    onClick = { navController.navigate("assignments") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Grade, contentDescription = "Lost & Found") },
                    label = { Text("Lost & Found") },
                    selected = true,
                    onClick = { /* Already here */ }
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
            Icon(Icons.Filled.Grade, contentDescription = null, modifier = Modifier.size(100.dp))
            Text(
                "Lost & Found Screen\n(UI Coming Soon)",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
                //new
            )
        }
    }
}
