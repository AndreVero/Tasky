package com.vero.tasky.agenda.presentation.taskdetails


import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    onEditTitle: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    navigateBack: () -> Unit,
    title: String? = null,
    description: String? = null,
    deletedPhotoUri: String? = null,
) {
}