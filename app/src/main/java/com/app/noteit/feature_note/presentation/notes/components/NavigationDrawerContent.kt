package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.noteit.feature_note.domain.model.NavigationItem
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    drawerState: DrawerState
) {

    val navigationItems = listOf(
        NavigationItem(
            title = "Notes",
            selectedIcon = Icons.Filled.Notes,
            unselectedIcon = Icons.Outlined.Notes
        ),
        NavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
        /*NavigationItem(
            title = "Reset passcode",
            selectedIcon = Icons.Filled.Password,
            unselectedIcon = Icons.Outlined.Password
        ),
        NavigationItem(
            title = "Share Note It with others",
            selectedIcon = Icons.Filled.Share,
            unselectedIcon = Icons.Outlined.Share
        ),
        NavigationItem(
            title = "Sync notes",
            selectedIcon = Icons.Filled.Sync,
            unselectedIcon = Icons.Outlined.Sync
        ),*/
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Text(
            text = "Note It",
            modifier = Modifier.padding(all = 16.dp),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        navigationItems.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = {
                    Text(text = item.title)
                },
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    scope.launch {
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    .wrapContentWidth()
            )
        }
    }

}