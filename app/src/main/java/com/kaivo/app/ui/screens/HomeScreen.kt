package com.kaivo.app.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.kaivo.app.R
import com.kaivo.app.data.ClipItem
import com.kaivo.app.ui.components.ClipCard
import com.kaivo.app.util.ClipboardUtil
import com.kaivo.app.util.ExportUtil
import com.kaivo.app.util.ExportFormat
import com.kaivo.app.viewmodel.HomeViewModel
import com.kaivo.app.viewmodel.OneTimeEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val pasteText by viewModel.pasteFieldText.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val items by viewModel.visibleItems.collectAsState()
    val event by viewModel.events.collectAsState()

    var exportMenuExpanded by remember { mutableStateOf(false) }
    var pendingExportFormat by remember { mutableStateOf<ExportFormat?>(null) }

    val copiedMessage = stringResource(R.string.copied_toast)
    val savedMessage = stringResource(R.string.saved_toast)
    val emptyClipboardMessage = stringResource(R.string.pasted_empty_toast)
    val exportSuccessMessage = stringResource(R.string.export_success)
    val exportFailedMessage = stringResource(R.string.export_failed)
    val exportEmptyMessage = stringResource(R.string.export_empty)

    val txtLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(ExportFormat.TXT.mimeType)
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val allItems = viewModel.getAllForExport()
            if (allItems.isEmpty()) {
                Toast.makeText(context, exportEmptyMessage, Toast.LENGTH_SHORT).show()
                return@launch
            }
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    ExportUtil.write(stream, allItems, ExportFormat.TXT)
                }
            }.onSuccess {
                Toast.makeText(context, exportSuccessMessage, Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(context, exportFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val jsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(ExportFormat.JSON.mimeType)
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val allItems = viewModel.getAllForExport()
            if (allItems.isEmpty()) {
                Toast.makeText(context, exportEmptyMessage, Toast.LENGTH_SHORT).show()
                return@launch
            }
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    ExportUtil.write(stream, allItems, ExportFormat.JSON)
                }
            }.onSuccess {
                Toast.makeText(context, exportSuccessMessage, Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(context, exportFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(event) {
        when (event) {
            OneTimeEvent.Copied -> Toast.makeText(context, copiedMessage, Toast.LENGTH_SHORT).show()
            OneTimeEvent.Saved -> Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
            OneTimeEvent.ClipboardEmpty -> Toast.makeText(context, emptyClipboardMessage, Toast.LENGTH_SHORT).show()
            null -> Unit
        }
        if (event != null) viewModel.consumeEvent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { exportMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.FileDownload,
                                contentDescription = stringResource(R.string.export_data)
                            )
                        }
                        DropdownMenu(
                            expanded = exportMenuExpanded,
                            onDismissRequest = { exportMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.export_as_txt)) },
                                onClick = {
                                    exportMenuExpanded = false
                                    txtLauncher.launch(ExportUtil.suggestedFileName(ExportFormat.TXT))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.export_as_json)) },
                                onClick = {
                                    exportMenuExpanded = false
                                    jsonLauncher.launch(ExportUtil.suggestedFileName(ExportFormat.JSON))
                                }
                            )
                        }
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = pasteText,
                onValueChange = viewModel::onPasteFieldChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                placeholder = { Text(stringResource(R.string.paste_placeholder)) },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val clipped = ClipboardUtil.readClipboard(context)
                        viewModel.onClipboardRead(clipped)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(R.string.btn_paste))
                }
                Button(
                    onClick = { viewModel.saveCurrentField() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.btn_save))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                EmptyState(isSearching = searchQuery.isNotBlank())
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(items, key = { it.id }) { item: ClipItem ->
                        ClipCard(
                            item = item,
                            onCopy = {
                                ClipboardUtil.writeClipboard(context, item.content)
                                viewModel.onItemCopied()
                            },
                            onDelete = { viewModel.deleteItem(item) },
                            onTogglePin = { viewModel.togglePin(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(isSearching: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isSearching) stringResource(R.string.no_search_results)
            else stringResource(R.string.empty_history_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (!isSearching) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.empty_history_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
