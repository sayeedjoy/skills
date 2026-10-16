# Component cookbook

Snippets assume the opt-ins from `setup.md` and the shared components from `assets/templates/ExpressiveComponents.kt` (`ExpressiveIconButton`, `SettingsItem`, `expressiveSpring()`, ...).

## Contents
- [Icon badge palette](#icon-badge-palette)
- [Top bars](#top-bars)
- [Overflow menu pill](#overflow-menu-pill)
- [Adaptive navigation](#adaptive-navigation)
- [FAB menu](#fab-menu)
- [Floating toolbar](#floating-toolbar)
- [Form bottom sheet](#form-bottom-sheet)
- [Content card in a grouped list](#content-card-in-a-grouped-list)
- [Check-off celebration](#check-off-celebration)
- [Swipe to reveal actions](#swipe-to-reveal-actions)
- [Weekly day selector](#weekly-day-selector)
- [Three-way theme picker](#three-way-theme-picker)
- [Pull to refresh & loading](#pull-to-refresh--loading)
- [Delete with undo](#delete-with-undo)
- [Shape motifs](#shape-motifs)
- [Hero illustration that reflects state](#hero-illustration-that-reflects-state)

## Icon badge palette

Fixed pastel container + deep tint pairs, taken from the reference app's settings. Assign one per settings row by meaning and keep it stable across the app (the same feature always has the same color).

| Name | Container | Icon tint | Typical use |
|---|---|---|---|
| Amber | `0xFFFCBD00` | `0xFF6D3A01` | Theme / appearance, security / lock |
| Apricot | `0xFFFFB683` | `0xFF753403` | Calendar, scheduling |
| Sky | `0xFF40C4FF` | `0xFF003B5C` | Sorting, inventory, data |
| Coral | `0xFFFFB4AB` | `0xFF690005` | Notifications, alerts |
| Mint | `0xFF80DA88` | `0xFF00522C` | Presets, quick actions, health |
| Lavender | `0xFFD9BAFD` | `0xFF5629A4` | Language, accessibility |
| Cyan | `0xFF67D4FF` | `0xFF004E5D` | Devices / Wear OS, updates |
| Periwinkle | `0xFFA1C9FF` | `0xFF0641A0` | About / version / info |
| Rose | `0xFFFFB3AE` | `0xFF8A1A16` | Report a bug, feedback |
| Silver | `0xFFC7C7C7` | `0xFF2C2C2C` | Advanced, developer |

```kotlin
object BadgeColors {
    val Amber = Color(0xFFFCBD00) to Color(0xFF6D3A01)
    val Apricot = Color(0xFFFFB683) to Color(0xFF753403)
    val Sky = Color(0xFF40C4FF) to Color(0xFF003B5C)
    val Coral = Color(0xFFFFB4AB) to Color(0xFF690005)
    val Mint = Color(0xFF80DA88) to Color(0xFF00522C)
    val Lavender = Color(0xFFD9BAFD) to Color(0xFF5629A4)
    val Cyan = Color(0xFF67D4FF) to Color(0xFF004E5D)
    val Periwinkle = Color(0xFFA1C9FF) to Color(0xFF0641A0)
    val Rose = Color(0xFFFFB3AE) to Color(0xFF8A1A16)
    val Silver = Color(0xFFC7C7C7) to Color(0xFF2C2C2C)
}
```

These are intentionally the same in light and dark mode; pastel circles read well on both `surfaceContainerHigh` tones.

**User-pickable colors** for user content (an item's color in an icon/color picker): `"dynamic"` (use theme container colors) plus `#ffb3b6 #ffb869 #e8c349 #a0d57b #97cbff #b6c6ed #cabeff #f7adfd`. Icon tint on these: `Color.Black.copy(alpha = 0.7f)`. Show the picker as a grid of 48dp circles; the selected one gets a check icon and a ring.

## Top bars

**Secondary screens** (settings, details): `LargeTopAppBar` that collapses on scroll, Normal-weight display title, tonal circular back button.

```kotlin
val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
val barTypography = MaterialTheme.typography.copy(
    headlineMedium = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Normal),
    titleLarge = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
)
Scaffold(
    topBar = {
        MaterialTheme(typography = barTypography) {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.settings_title), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        ExpressiveIconButton(
                            onClick = onBack,
                            icon = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    },
    containerColor = Color.Transparent,
    modifier = Modifier.widthIn(max = 700.dp).fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)
) { padding -> /* LazyColumn(contentPadding = padding, modifier = Modifier.padding(horizontal = 16.dp)) */ }
```

Keeping `scrolledContainerColor = background` stops the bar turning a different tone on scroll; the collapse alone signals scrolling. `LargeFlexibleTopAppBar` (with a `subtitle`) is a good alternative on newer material3 versions.

**Home screen**: no app bar component; a header row with the app name and tonal icon buttons.

```kotlin
Row(
    modifier = Modifier
        .windowInsetsPadding(WindowInsets.captionBar)
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        stringResource(R.string.app_name),
        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ExpressiveIconButton(onClick = onCalendar, icon = Icons.Rounded.CalendarMonth, contentDescription = stringResource(R.string.calendar))
        ExpressiveIconButton(onClick = onSettings, icon = Icons.Rounded.Settings, contentDescription = stringResource(R.string.settings))
    }
}
```

## Overflow menu pill

When there are more than ~2 header actions, collapse them into a tall narrow pill (32×44dp) that squishes to 25% on press and opens a `DropdownMenu`.

```kotlin
Box {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val corner by animateIntAsState(if (pressed) 25 else 50, expressiveSpring(), label = "corner")
    Surface(
        onClick = { expanded = true },
        modifier = Modifier.width(32.dp).height(44.dp),
        shape = RoundedCornerShape(corner),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        interactionSource = source
    ) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.MoreVert, stringResource(R.string.more)) } }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.animateContentSize(expressiveSpring())
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.settings)) },
            leadingIcon = { Icon(Icons.Rounded.Settings, null) },
            onClick = { expanded = false; onSettings() }
        )
    }
}
```

## Adaptive navigation

```kotlin
Row(Modifier.fillMaxSize()) {
    if (isExpanded) {
        val railState = rememberWideNavigationRailState(initialValue = WideNavigationRailValue.Expanded)
        WideNavigationRail(state = railState, modifier = Modifier.width(IntrinsicSize.Max)) {
            val railExpanded = railState.currentValue == WideNavigationRailValue.Expanded
            tabs.forEachIndexed { i, tab ->
                WideNavigationRailItem(
                    railExpanded = railExpanded,
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    icon = { Icon(tab.icon, null) },
                    label = { Text(stringResource(tab.label)) }
                )
            }
        }
    }
    Scaffold(
        modifier = Modifier.weight(1f),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = { /* FAB menu on the primary tab only */ },
        bottomBar = {
            if (!isExpanded) ShortNavigationBar {
                tabs.forEachIndexed { i, tab ->
                    ShortNavigationBarItem(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        icon = { Icon(tab.icon, null) },
                        label = { Text(stringResource(tab.label)) }
                    )
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
            label = "tabs"
        ) { tab -> /* tab content */ }
    }
}
```

Three tabs is the sweet spot (e.g. Home / Stats / You). Back from a non-home tab returns to home (`BackHandler(enabled = selectedTab != 0) { selectedTab = 0 }`); back with the FAB menu open closes it first.

## FAB menu

```kotlin
FloatingActionButtonMenu(
    expanded = fabExpanded,
    button = {
        ToggleFloatingActionButton(
            modifier = Modifier
                .semantics {
                    traversalIndex = -1f
                    stateDescription = if (fabExpanded) expandedLabel else collapsedLabel
                    contentDescription = menuLabel
                }
                .animateFloatingActionButton(visible = true, alignment = Alignment.BottomEnd),
            checked = fabExpanded,
            onCheckedChange = { fabExpanded = !fabExpanded }
        ) {
            val icon by remember { derivedStateOf { if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add } }
            Icon(rememberVectorPainter(icon), null, Modifier.animateIcon({ checkedProgress }))
        }
    }
) {
    actions.forEach { action ->
        FloatingActionButtonMenuItem(
            onClick = { fabExpanded = false; onAction(action) },
            icon = { Icon(action.icon, null) },
            text = { Text(action.label) }
        )
    }
}
```

Put the menu items in order of frequency, most-used closest to the button. Quick presets (user-defined one-tap items) can be listed here too with their own color as the icon tint. Wrap the FAB in a `TooltipBox` + `PlainTooltip` for desktop hover. If `AppTheme` already uses `MaterialExpressiveTheme`, no extra wrapper is needed.

## Floating toolbar

For a temporary mode (reorder/edit/select), show a floating toolbar instead of changing the top bar:

```kotlin
HorizontalFloatingToolbar(
    modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 32.dp)
        .offset(y = -FloatingToolbarDefaults.ScreenOffset)
        .zIndex(1f),
    expanded = true,
    leadingContent = { IconButton(onClick = onSave) { Icon(Icons.Rounded.Check, stringResource(R.string.save)) } },
    trailingContent = {
        IconButton(onClick = { deleteMode = !deleteMode }) {
            Icon(if (deleteMode) Icons.Rounded.Close else Icons.Rounded.Delete, stringResource(R.string.delete_mode))
        }
    },
    content = {
        FilledIconButton(modifier = Modifier.width(64.dp), onClick = onAdd) { Icon(Icons.Rounded.Add, stringResource(R.string.add)) }
    }
)
```

Leave ~140dp of bottom spacer in the list so the last item can scroll above the toolbar. If the user leaves the mode with unsaved changes, ask with a Save / Discard dialog.

## Form bottom sheet

Create/edit forms live in a `ModalBottomSheet`. Layout from top: bold title → large tappable avatar (icon + color, with an edit badge) → text fields → setting tiles → two 50dp buttons.

```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    modifier = Modifier.statusBarsPadding(),
    contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.ime),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(stringResource(titleRes), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp))
        }
        item { AvatarPicker(icon, color, onClick = { showIconPicker = true }) }   // 80dp circle, 40dp icon, 24dp edit badge
        item { Spacer(Modifier.height(24.dp)) }
        item { OutlinedTextField(name, { name = it; nameError = false }, Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.name_hint)) }, singleLine = true, isError = nameError) }
        item { Spacer(Modifier.height(12.dp)) }
        item { OutlinedTextField(notes, { notes = it }, Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.notes_hint)) }, minLines = 2, maxLines = 4) }
        item { Spacer(Modifier.height(24.dp)) }
        item { ValueTile(label = stringResource(R.string.time), value = time.format(hhmm), onClick = { showTimePicker = true }) }
        item { Spacer(Modifier.height(32.dp)) }
        item {
            DualActionRow(
                dismissText = stringResource(R.string.cancel), confirmText = stringResource(R.string.save),
                onDismiss = { scope.launch { sheetState.hide() }.invokeOnCompletion { if (!sheetState.isVisible) onDismiss() } },
                onConfirm = { if (name.isBlank()) nameError = true else onSave(...) },
                modifier = Modifier.navigationBarsPadding().padding(bottom = 16.dp)
            )
        }
    }
}
```

Avatar: 80dp `CircleShape` filled with the chosen color (or `primary` for "dynamic"), 40dp icon tinted `onPrimary` (or black 70%), and a 24dp `surface` circle with a 14dp edit icon at `BottomEnd` offset (-4dp, -4dp).

Value tile (time, date, frequency): full-width row, `RoundedCornerShape(12.dp)`, background `surfaceVariant` at 50% alpha (100% on hover), 12dp padding; label `bodyMedium` on the left, value `titleMedium` Bold `primary` + 16dp edit icon on the right.

Segmented choices inside forms (frequency: Daily / Specific days / Interval) use `ConnectedChoiceGroup` or the `OutlinedToggleButton` variant with `horizontalScroll` when labels are long. Pickers (time, date) are `TimePicker` / `DatePicker` in dialogs on `surfaceContainerHigh` with `RoundedCornerShape(32.dp)` (20dp for a custom `Dialog` surface).

## Content card in a grouped list

User content (today's items) uses the same grouping idea as settings, but with a `Card` so it can hold richer content and long-press:

```kotlin
itemsIndexed(items, key = { _, it -> it.id }) { index, item ->
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val shape = animatedGroupShape(isFirst = index == 0, isLast = index == items.lastIndex, pressed = pressed)
    Box(Modifier.animateItem(
        placementSpec = spring(stiffness = Spring.StiffnessMediumLow, visibilityThreshold = IntOffset.VisibilityThreshold),
        fadeInSpec = spring(stiffness = Spring.StiffnessMediumLow),
        fadeOutSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )) {
        Card(
            modifier = Modifier.fillMaxWidth().clip(shape)
                .combinedClickable(interactionSource = source, indication = LocalIndication.current,
                    onClick = { onOpen(item) }, onLongClick = { onEdit(item) }),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            ListItem(
                modifier = Modifier.padding(vertical = 4.dp),
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                leadingContent = { IconBadge(item.icon, item.badgeColor ?: MaterialTheme.colorScheme.tertiaryContainer,
                    item.badgeTint ?: MaterialTheme.colorScheme.onTertiaryContainer) },
                headlineContent = { Text(item.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                supportingContent = { /* "Scheduled 08:00" or clock icon (12dp) + "Taken at 08:04", bodyMedium at 70% alpha */ },
                trailingContent = { /* RadioButton check-off with celebration, see below */ }
            )
        }
    }
}
```

Users can insert named dividers to split the list into several groups; first/last-in-group is then computed relative to dividers. Group spacing 2–4dp inside, 16–24dp between groups. On expanded widths use `LazyVerticalGrid(GridCells.Adaptive(minSize = 340.dp))` with 12dp spacing and standalone 24dp cards.

## Check-off celebration

When an item is marked done, a random Material shape pops out behind the radio button, spins half a turn and fades. Small, quick, and it makes the core action feel rewarding.

```kotlin
val scale = remember { Animatable(0f) }
val rotation = remember { Animatable(0f) }
val alpha = remember { Animatable(0f) }
var shape by remember { mutableStateOf(MaterialShapes.Cookie9Sided) }

LaunchedEffect(isDone) {
    if (!isDone) { scale.animateTo(0f, tween(300)); alpha.animateTo(0f, tween(300)); return@LaunchedEffect }
    shape = listOf(MaterialShapes.Cookie9Sided, MaterialShapes.Cookie12Sided, MaterialShapes.Triangle,
        MaterialShapes.Gem, MaterialShapes.Ghostish, MaterialShapes.Pentagon).random()
    launch { alpha.animateTo(1f, tween(200)) }
    launch { scale.snapTo(0f); scale.animateTo(1.1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
    rotation.snapTo(0f)
    rotation.animateTo(180f, tween(900, easing = FastOutSlowInEasing))
    launch { scale.animateTo(0f, tween(300, easing = FastOutSlowInEasing)) }
    launch { alpha.animateTo(0f, tween(300, easing = FastOutSlowInEasing)) }
}

Box(contentAlignment = Alignment.Center) {
    if (alpha.value > 0f) {
        Box(Modifier.size(32.dp)
            .graphicsLayer { scaleX = scale.value; scaleY = scale.value; rotationZ = rotation.value; this.alpha = alpha.value }
            .background(MaterialTheme.colorScheme.primary, shape.toShape()))
    }
    RadioButton(selected = isDone, onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onToggle() }, enabled = canToggle)
}
```

If `MaterialShapes` isn't available in the project's material3 version, use vector drawables of the same shapes (`Icon(painterResource(R.drawable.ic_cookie_10), ...)`), which is what the reference app does.

## Swipe to reveal actions

Swiping a list card to the end reveals two pill actions (Delete in `error`, Edit in `onPrimary` with `primary` icon), separated by 4dp gaps. Release past 10% → snaps open at 25% width; past 50% → the delete pill grows to full width and deletes (with haptic + undo snackbar); otherwise springs back. All with `spring(stiffness = Spring.StiffnessMediumLow)`. Build it with `Modifier.draggable` + a custom `Layout` that sizes the two pills from the current offset; the action pills use `RoundedCornerShape(100.dp)`. On mouse devices, right-click on the card opens it to the 25% position.

Prefer `SwipeToDismissBox` if the project only needs a single delete action; the two-pill reveal is worth it when edit is equally common.

## Weekly day selector

A horizontally paged week strip above the day's list:
- Month label: `labelSmall`, `primary`, 12dp below.
- 7 equal tiles, 6dp apart, 68dp tall, `RoundedCornerShape(32.dp)` that squishes to 12dp while pressed (tween 200).
- Tile: day initial `labelSmall` at 80% alpha, 4dp, day number `titleMedium` Bold.
- Selected: `primary` / `onPrimary` (animated color). Others: `surfaceContainerHigh`. Today: 1dp `primary` border.
- Press emphasis: the pressed tile's weight animates to 1.25, neighbours shrink to 0.875 (0.75 at the edges), tween 200. It looks like the tile pushes its neighbours.
- On expanded widths, replace with a connected `ToggleButton` row (full day names) 45dp tall.
- Below it, a `HorizontalPager` of days kept in sync with the selection, so the user can swipe between days.

## Three-way theme picker

Inside the first settings row (with an Amber palette badge): System on top spanning the full width, Dark and Light side by side underneath, 2dp gaps, each a `ToggleButton` 40dp tall. Unchecked: transparent with `outlineVariant` 1dp border; checked: full pill in `primary`. Outer corners 50%, inner corners 15%, so together they form one rounded block:

```kotlin
// System:  top 50%, bottom 15%        Dark: topStart 15, bottomStart 50, others 15       Light: bottomEnd 50, others 15
shapes = ToggleButtonShapes(
    shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomStartPercent = 15, bottomEndPercent = 15),
    pressedShape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomStartPercent = 15, bottomEndPercent = 15),
    checkedShape = RoundedCornerShape(50)
)
```

Each button shows a 20dp icon (Smartphone / DarkMode / LightMode) + 8dp + label. Changing it Crossfades the whole screen (see setup.md).

## Pull to refresh & loading

```kotlin
PullToRefreshBox(
    state = pullState, isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize(),
    indicator = {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            PullToRefreshDefaults.LoadingIndicator(state = pullState, isRefreshing = refreshing)
        }
    }
) { /* list */ }
```

Standalone loading (e.g. checking for updates): `LoadingIndicator()` or `ContainedLoadingIndicator()` centered, with a `bodyLarge` status line below. Progress with a known value: `LinearWavyProgressIndicator` / `CircularWavyProgressIndicator`.

## Delete with undo

Delete immediately, then offer undo; don't ask first. Dismiss any current snackbar, show the new one with `actionLabel = Undo`, `withDismissAction = true`, and auto-dismiss after ~2s. Place the host above the FAB/nav bar (bottom padding ~180dp on compact, ~120dp on expanded) so it doesn't cover them.

```kotlin
scope.launch {
    snackbarHostState.currentSnackbarData?.dismiss()
    val timeout = launch { delay(2000); snackbarHostState.currentSnackbarData?.dismiss() }
    val result = snackbarHostState.showSnackbar(message = deletedMsg, actionLabel = undoLabel,
        withDismissAction = true, duration = SnackbarDuration.Indefinite)
    timeout.cancel()
    if (result == SnackbarResult.ActionPerformed) viewModel.restore(item)
}
```

## Shape motifs

Material 3 Expressive ships 35 shapes in `MaterialShapes` (`Cookie4Sided`…`Cookie12Sided`, `Gem`, `Ghostish`, `Pentagon`, `Triangle`, `Sunny`, `VerySunny`, `Burst`, `SoftBurst`, `Flower`, `Clover4Leaf`, `Clover8Leaf`, `Puffy`, `Pill`, `Arch`, `Heart`, ...). Use `.toShape()` to clip or fill with them. Places they earn their keep:
- **Onboarding hero**: a large cookie in `primary` rotating slowly (`infiniteRepeatable(tween(20000, easing = LinearEasing))`), with the monochrome app logo on top in `background` color.
- **Celebratory card** (birthday, streak, milestone): `primaryContainer` 24dp card, leading 48dp `Cookie12Sided` in `primary` with a 24dp icon in `onPrimary`.
- **Profile avatar** or stat highlights: clip to `Cookie9Sided` / `Sunny` instead of a circle.
- **Check-off** burst (above).
- Morph between two shapes for a selected state with `Morph(start, end)` from graphics-shapes.

Use them as accents, one per screen at most; if every element is a cookie none of them is special.

## Hero illustration that reflects state

Sub-settings screens open with a 200dp monochrome illustration tinted `primary`, 32dp vertical padding, that changes with the settings below it (notifications off / notifications / full-screen alarm). Swap with `Crossfade(targetState = res, animationSpec = tween(300))`. Draw them as single-color vector drawables so they tint with dynamic color.
