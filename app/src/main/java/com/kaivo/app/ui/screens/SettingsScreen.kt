package com.kaivo.app.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaivo.app.R
import com.kaivo.app.data.AppLanguage
import com.kaivo.app.data.ClipItem
import com.kaivo.app.data.ThemeMode
import com.kaivo.app.util.ExportFormat
import com.kaivo.app.util.ExportUtil
import com.kaivo.app.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    getAllItemsForExport: suspend () -> List<ClipItem>,
    onDeleteAllConfirmed: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val language by viewModel.language.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    val exportSuccessMessage = stringResource(R.string.export_success)
    val exportFailedMessage = stringResource(R.string.export_failed)
    val exportEmptyMessage = stringResource(R.string.export_empty)

    fun doExport(uri: android.net.Uri?, format: ExportFormat) {
        if (uri == null) return
        scope.launch {
            val allItems = getAllItemsForExport()
            if (allItems.isEmpty()) {
                Toast.makeText(context, exportEmptyMessage, Toast.LENGTH_SHORT).show()
                return@launch
            }
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    ExportUtil.write(stream, allItems, format)
                }
            }.onSuccess {
                Toast.makeText(context, exportSuccessMessage, Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(context, exportFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val txtLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(ExportFormat.TXT.mimeType)
    ) { uri -> doExport(uri, ExportFormat.TXT) }

    val jsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(ExportFormat.JSON.mimeType)
    ) { uri -> doExport(uri, ExportFormat.JSON) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            SectionLabel(stringResource(R.string.language))
            LanguageOptionRow(
                label = stringResource(R.string.english),
                selected = language == AppLanguage.ENGLISH,
                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
            )
            LanguageOptionRow(
                label = stringResource(R.string.persian),
                selected = language == AppLanguage.PERSIAN,
                onClick = { viewModel.setLanguage(AppLanguage.PERSIAN) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(stringResource(R.string.appearance))
            LanguageOptionRow(
                label = stringResource(R.string.light_mode),
                selected = themeMode == ThemeMode.LIGHT,
                onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) }
            )
            LanguageOptionRow(
                label = stringResource(R.string.dark_mode),
                selected = themeMode == ThemeMode.DARK,
                onClick = { viewModel.setThemeMode(ThemeMode.DARK) }
            )
            LanguageOptionRow(
                label = stringResource(R.string.system_default),
                selected = themeMode == ThemeMode.SYSTEM,
                onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(stringResource(R.string.export_data))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { txtLauncher.launch(ExportUtil.suggestedFileName(ExportFormat.TXT)) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text(stringResource(R.string.export_as_txt)) }

                Button(
                    onClick = { jsonLauncher.launch(ExportUtil.suggestedFileName(ExportFormat.JSON)) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text(stringResource(R.string.export_as_json)) }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(stringResource(R.string.delete_all_data))
            Button(
                onClick = { showDeleteDialog = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) { Text(stringResource(R.string.delete_all_data)) }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(stringResource(R.string.about_kaivo))
            Text(
                text = stringResource(R.string.about_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.version),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_all_confirm_title)) },
            text = { Text(stringResource(R.string.delete_all_confirm_body)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteAllConfirmed()
                }) {
                    Text(
                        stringResource(R.string.delete_all_confirm_action),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
private fun LanguageOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onBackground,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
