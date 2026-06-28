package com.madiwist.twitch.presentation.edit_profile.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.madiwist.twitch.presentation.ui.theme.Shapes

@Composable
fun SkillsChips(
    text: String,
    selected: Boolean = false,
    enabled: Boolean = true,
    onSelectedChange: (Boolean) -> Unit,
) {
    FilterChip(
        onClick = { onSelectedChange(!selected) },
        label = {
            Text(text)
        },
        selected = selected,
        enabled = enabled,
        leadingIcon = if (selected) {
            @Composable {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            iconColor = MaterialTheme.colorScheme.onSurface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            containerColor = Color.Transparent,

            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedContainerColor = MaterialTheme.colorScheme.primary
        ),
        shape = Shapes.extraLarge
    )
}