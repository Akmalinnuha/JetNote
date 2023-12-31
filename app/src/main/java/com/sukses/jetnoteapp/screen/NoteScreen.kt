package com.sukses.jetnoteapp.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sukses.jetnoteapp.components.NoteButton
import com.sukses.jetnoteapp.components.NoteInputText
import com.sukses.jetnoteapp.data.NoteDataSource
import com.sukses.jetnoteapp.model.Note
import com.sukses.jetnoteapp.util.formatDate

@Composable
fun NoteScreen(
    notes: List<Note>,
    noteViewModel: NoteViewModel?
) {
    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var openDialog by remember {
        mutableStateOf(false)
    }

    var selectedNote: Note? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(title = {
            Text(text = "JetNote App")
        },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon")
            },
            backgroundColor = Color(0xFFDADFE3))

        // Content
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            NoteInputText(
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp),
                text = title,
                label = "Title",
                onTextChange = {
                    if (it.all { char ->
                            char.isLetterOrDigit() || char.isWhitespace()
                        }) title = it
                } )

            NoteInputText(
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp),
                text = description,
                label = "Add a note",
                onTextChange = {
                    if (it.all { char ->
                            char.isLetterOrDigit() || char.isWhitespace()
                        }) description = it
                })

            NoteButton(text = "Save",
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        noteViewModel?.addNote(Note(title = title,
                            description = description))
                        title = ""
                        description = ""
                        Toast.makeText(context, "Note Added",
                            Toast.LENGTH_SHORT).show()
                    }
                })

        }
        Divider(modifier = Modifier.padding(10.dp))
        Column {
            LazyColumn {
                items(notes) { note ->
                    NoteRow(
                        note = note
                    ) {
                        openDialog = true
                        selectedNote = it
                    }
                }
            }
            AnimatedVisibility(visible = openDialog) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog = false
                    },
                    title = { Text(text = "Delete a note?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedNote?.let { noteViewModel?.removeNote(it) }
                                openDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red
                            ),
                        ) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { openDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onCLick: (Note) -> Unit) {
    Surface(
        modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = Color(0xFF5BB6FF),
        elevation = 6.dp) {
        Column(modifier
            .clickable {
                onCLick(note)
            }
            .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.Start) {
            Text(text = note.title,
                style = MaterialTheme.typography.subtitle2)
            Text(text = note.description, style = MaterialTheme.typography.subtitle1)
            Text(text = formatDate(note.entryDate.time),
                style = MaterialTheme.typography.caption)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    NoteScreen(notes = NoteDataSource().loadNotes(), viewModel())
}