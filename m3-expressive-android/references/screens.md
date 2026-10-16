# Screen blueprints

Each blueprint lists the structure top-to-bottom with exact spacing. Build screens as stateless composables (`state`, callbacks) with a thin stateful wrapper that talks to the ViewModel.

## Contents
- [App shell](#app-shell)
- [Home / today list](#home--today-list)
- [Settings](#settings)
- [Sub-settings with hero](#sub-settings-with-hero)
- [Create / edit sheet](#create--edit-sheet)
- [Onboarding](#onboarding)
- [Stats & profile tabs](#stats--profile-tabs)
- [Full-screen alert](#full-screen-alert)
- [Tablet, foldable, desktop](#tablet-foldable-desktop)

## App shell

```
Row
├─ WideNavigationRail            (expanded width only)
└─ Scaffold(containerColor = background)
   ├─ bottomBar: ShortNavigationBar   (compact only; 3 tabs, rounded icons)
   ├─ floatingActionButton: FAB menu  (primary tab only)
   └─ content
      ├─ Home header (app name displaySmall Bold + tonal icon buttons / overflow pill)
      └─ AnimatedContent(fade 200ms) → tab content
AppSnackbarHost overlaid at the bottom, above FAB/nav
```

Navigation between tabs is local state; secondary screens (settings, sub-settings, inventory, editor) are separate destinations (Navigation Compose/Nav3, or activities if the project already uses them). Every secondary screen uses the Large top app bar + tonal back button.

## Home / today list

```
Header row                               padding h24 v16, captionBar insets
Spacer 8
Weekly day selector (paged)              contentPadding h16, pageSpacing 16
Spacer 24
HorizontalPager of days
└─ PullToRefreshBox (LoadingIndicator)
   ├─ empty:  [optional celebratory card, h16] + EmptyState(64dp icon, bodyLarge)
   └─ LazyColumn  padding h16
      ├─ pinned/attention cards (24dp, e.g. "Still sick today?  [No | Yes]" connected buttons)
      ├─ grouped content cards (20/4dp, 2dp gaps, animateItem, swipe-to-reveal, check-off)
      ├─ optional named divider → new group (label titleSmall primary, 16dp above)
      └─ Spacer ~140dp (clear the FAB)
```

Attention card: 24dp `surfaceContainerHigh` card, 16dp padding, IconBadge (primaryContainer) + question `titleMedium`, then 16dp and a two-button connected row (outlined transparent toggles, "No" in `onSurfaceVariant`, "Yes" in `primary`).

## Settings

```
LargeTopAppBar "Settings" (exitUntilCollapsed, Normal displaySmall)
LazyColumn  padding h16, width ≤ 700dp centered
├─ Spacer 20
├─ SectionHeader "Customization"
│  SegmentedGroup
│  ├─ Theme row (Amber badge + three-way theme picker inside)
│  ├─ SettingsItem Week start  → OptionDialog
│  └─ SettingsItem Sort order  → OptionDialog
├─ Spacer 32
├─ SectionHeader "Preferences"
│  SegmentedGroup: Notifications ›, Quick actions ›, Inventory ›, App lock [Switch], Advanced ›
├─ Spacer 32
├─ SectionHeader "More":  Language (API 33+, opens system per-app language), Wear OS ›
├─ Spacer 32
├─ SectionHeader "Info":  Version (opens app info), Developer (link), Report a bug (link), Check for updates
└─ Spacer 48
```

Rules that keep it feeling like Pixel Settings:
- Every row has a colored badge; assign colors from the palette by meaning, and don't put two identical colors adjacent.
- Subtitle shows the **current value** for value settings ("Monday", "By time") and a short description for navigation rows.
- Rows that navigate have no chevron; the whole card is the target. Rows with a switch toggle on row tap too.
- Group sizes 2–5. Split longer groups into new sections.
- Sensitive toggles (app lock) confirm with biometrics before enabling.

## Sub-settings with hero

```
LargeTopAppBar "Notifications"
LazyColumn  padding h16, width ≤ 700dp
├─ Spacer 8
├─ Hero illustration 200dp, tint primary, padding v32, Crossfade on state
├─ MasterSwitchCard "Show notifications" (64dp pill, primaryContainer)
├─ Spacer 24
├─ (when master on, AnimatedVisibility expand/fade)
│  SectionHeader + SegmentedGroup of dependent options
│  e.g. Full-screen alarm [Switch], Alarm style [ConnectedChoiceGroup: Buttons | Swipe],
│       Snooze duration → OptionDialog, Sound ›
└─ Spacer 48
```

When the master switch is off, hide the dependent options (or show them disabled at 38% alpha if seeing them helps understanding). The hero changes to the "off" illustration, so the screen tells the user the state before they read anything.

## Create / edit sheet

See `components.md#form-bottom-sheet`. Order: title → avatar (icon/color picker) → name → notes → type-specific tiles (time(s), frequency group, days chips, start/end date, dosage stepper) → Cancel / Save. Validate on Save: mark the empty name field `isError` instead of disabling Save, so the user learns what's missing. Hide the sheet with `sheetState.hide()` before invoking callbacks so it animates out.

For multiple times per day, show a list of value tiles with a trailing remove icon and an "Add time" tonal button below.

## Onboarding

```
HorizontalPager (userScrollEnabled = false; buttons drive it)
├─ Page 1  Hero: RotatingShape (cookie, primary, 20s/turn) with logo; displaySmall Bold title; bodyLarge intro
├─ Page 2  Permissions: SegmentedGroup rows with badges + "Allow" tonal buttons (notifications, exact alarms, full-screen intent)
├─ Page 3  Personal setup (optional): name, preferences via ConnectedChoiceGroup
└─ Page 4  Done: celebratory shape + "Get started"
Bottom bar: [Back][  Next  ]
```

Bottom buttons: Back and Next sit in a row; on the first page Back's weight animates to ~0 and its alpha to 0 so Next fills the width, then Back grows in from the start when the user advances. Use the expressive spring for weights. On tablets center the content at ≤ 600dp wide.

## Stats & profile tabs

Stats: a vertical stack of 24dp `surfaceContainerHigh` cards, 12dp apart, each with a `titleMedium` header and one visualization (adherence ring, weekly bars with rounded tops in `primary` / `primaryContainer`, streak count in `displayMedium` Bold). Pull-to-refresh with `LoadingIndicator`. Use `ConnectedChoiceGroup` for the range (Week / Month / Year).

Profile ("You"): avatar clipped to a Material shape, name `headlineMedium`, then SegmentedGroups of personal data (date of birth, height, weight, blood type, allergies), each opening a small bottom sheet editor. Same badge palette.

## Full-screen alert

Alarm/reminder over the lock screen: `background` = `primaryContainer`, big time `displayLarge` Bold, item title `headlineMedium`, icon in a 120dp rotating cookie. Two styles the user can choose: two big 50dp-plus pill buttons (Snooze outlined, Done filled) or a swipe slider (track `surfaceContainerHigh` pill, thumb a `primary` circle that snaps with the expressive spring).

## Tablet, foldable, desktop

- Width class Expanded → wide nav rail, grid content (`GridCells.Adaptive(340.dp)`), and wider day selector with full day names.
- Text-heavy screens (settings, forms) cap at 700dp (sheets/dialogs at 400–600dp) and center.
- Mouse/keyboard: `pointerHoverIcon(PointerIcon.Hand)` on clickables, hover state on morphing buttons (25% corners), right-click on cards opens their actions, mouse wheel over a horizontal pager pages it, tooltips on icon-only buttons.
- Desktop windows: transparent caption bar and `WindowInsets.captionBar` padding on the home header.
