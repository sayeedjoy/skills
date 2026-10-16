@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class
)

package com.example.app.ui.components // TODO: change to the project's package

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------------------------
// Motion tokens
// ---------------------------------------------------------------------------------------------

/** The bouncy spring used for every shape/color/size change on controls. */
fun <T> expressiveSpring() = spring<T>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessMediumLow
)

/** Corner percent for a pill that squishes when pressed: 50% idle, 25% hovered, 15% pressed. */
@Composable
fun rememberMorphingCornerPercent(interactionSource: MutableInteractionSource): Int {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val percent by animateIntAsState(
        targetValue = when {
            pressed -> 15
            hovered -> 25
            else -> 50
        },
        animationSpec = expressiveSpring(),
        label = "morphingCorner"
    )
    return percent
}

// ---------------------------------------------------------------------------------------------
// Buttons
// ---------------------------------------------------------------------------------------------

/** Tonal circular icon button (top bar actions, back button). */
@Composable
fun ExpressiveIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    size: Dp = 48.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val corner = rememberMorphingCornerPercent(interactionSource)
    Surface(
        onClick = onClick,
        modifier = modifier.size(size).pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(corner),
        color = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}

/** Filled 50dp primary action. */
@Composable
fun ExpressiveButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val corner = rememberMorphingCornerPercent(interactionSource)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp).pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(corner),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        interactionSource = interactionSource
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, maxLines = 1)
    }
}

/** Outlined 50dp secondary action (Cancel next to Save). */
@Composable
fun ExpressiveOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val corner = rememberMorphingCornerPercent(interactionSource)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp).pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(corner),
        interactionSource = interactionSource
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, maxLines = 1)
    }
}

/** Text button for dialogs. */
@Composable
fun ExpressiveTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.primary,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val corner = rememberMorphingCornerPercent(interactionSource)
    TextButton(
        onClick = onClick,
        modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(corner),
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
        interactionSource = interactionSource
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Row of two equal 50dp actions: outlined dismiss + filled confirm. Used at the bottom of sheets. */
@Composable
fun DualActionRow(
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ExpressiveOutlinedButton(onClick = onDismiss, text = dismissText, modifier = Modifier.weight(1f))
        ExpressiveButton(onClick = onConfirm, text = confirmText, modifier = Modifier.weight(1f))
    }
}

// ---------------------------------------------------------------------------------------------
// Lists & settings
// ---------------------------------------------------------------------------------------------

/** Colored circle with a centered icon — the leading element of almost every row. */
@Composable
fun IconBadge(
    icon: ImageVector,
    containerColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
) {
    Box(
        modifier = modifier.size(size).clip(CircleShape).background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(size / 2))
    }
}

/** Primary-colored section label above a group. */
@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

/** Vertical container that spaces segmented items by the standard 2dp gap. */
@Composable
fun SegmentedGroup(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        content = content
    )
}

/** One row of a settings group. [index]/[count] give it the right outer/inner corners. */
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    index: Int,
    count: Int,
    badgeColor: Color,
    badgeIconColor: Color,
    onClick: () -> Unit,
    subtitle: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    SegmentedListItem(
        selected = false,
        onClick = onClick,
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconBadge(icon, badgeColor, badgeIconColor)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    if (!subtitle.isNullOrEmpty()) {
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (trailingContent != null) {
                    Spacer(Modifier.width(16.dp))
                    trailingContent()
                }
            }
        }
    )
}

/**
 * Manual grouped shape for cards in a LazyColumn when SegmentedListItem isn't a fit
 * (custom card content) or isn't available: 20dp outside the group, 4dp between items,
 * and every corner eases to 20dp while pressed.
 */
@Composable
fun animatedGroupShape(isFirst: Boolean, isLast: Boolean, pressed: Boolean): Shape {
    val top by animateDpAsState(if (isFirst || pressed) 20.dp else 4.dp, expressiveSpring(), label = "top")
    val bottom by animateDpAsState(if (isLast || pressed) 20.dp else 4.dp, expressiveSpring(), label = "bottom")
    return RoundedCornerShape(topStart = top, topEnd = top, bottomStart = bottom, bottomEnd = bottom)
}

/** Switch with check / close icons in the thumb, plus haptics. */
@Composable
fun ExpressiveSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, enabled: Boolean = true) {
    val haptic = LocalHapticFeedback.current
    Switch(
        checked = checked,
        enabled = enabled,
        onCheckedChange = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onCheckedChange(it)
        },
        thumbContent = {
            Icon(
                imageVector = if (checked) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )
        }
    )
}

/** Big pill card that turns a whole feature on/off (top of a sub-settings screen). */
@Composable
fun MasterSwitchCard(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(64.dp)
    Card(
        modifier = Modifier.fillMaxWidth().clip(shape).clickable { onCheckedChange(!checked) },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        ListItem(
            modifier = Modifier.padding(vertical = 4.dp),
            headlineContent = {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            },
            trailingContent = { ExpressiveSwitch(checked, onCheckedChange) },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
}

// ---------------------------------------------------------------------------------------------
// Choices
// ---------------------------------------------------------------------------------------------

/** Connected single-select button group (2–4 options). Checked item morphs to a full pill. */
@Composable
fun ConnectedChoiceGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        options.forEachIndexed { index, label ->
            val checked = index == selectedIndex
            ToggleButton(
                checked = checked,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSelected(index)
                },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                colors = ToggleButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = if (checked) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.weight(1f).height(40.dp)
            ) {
                Text(label, maxLines = 1, textAlign = TextAlign.Center)
            }
        }
    }
}

/** Single-choice dialog with animated, softly-filled radio rows. Selecting closes it. */
@Composable
fun OptionDialog(
    icon: ImageVector,
    title: String,
    options: List<String>,
    selectedIndex: Int,
    cancelText: String,
    onSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(icon, null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary) },
        title = { Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().animateContentSize(expressiveSpring()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEachIndexed { index, option ->
                    val selected = index == selectedIndex
                    val container by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                        expressiveSpring(), label = "optionContainer"
                    )
                    val content by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        expressiveSpring(), label = "optionContent"
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(container)
                            .clickable { onSelected(index) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Spacer(Modifier.width(16.dp))
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = content
                        )
                    }
                }
            }
        },
        confirmButton = { ExpressiveTextButton(onClick = onDismiss, text = cancelText) },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 6.dp
    )
}

// ---------------------------------------------------------------------------------------------
// Feedback & states
// ---------------------------------------------------------------------------------------------

@Composable
fun AppSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            actionColor = MaterialTheme.colorScheme.inversePrimary,
            actionContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

/** Centered quiet empty state. */
@Composable
fun EmptyState(icon: ImageVector, message: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(Modifier.height(16.dp))
            Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
