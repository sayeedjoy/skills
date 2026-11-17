# Craft checklist

The concrete values behind the judgment in SKILL.md. Read this when implementing or reviewing a surface, not when deciding a direction. Every number is a default with a reason attached, so deviate when the reason does not apply and say which reason you are overriding.

Express every fix in the styling system the project already uses. Introducing a second approach to land one polish fix costs more than the fix returns.

## Contents

- [Motion](#motion)
- [Gesture and drag](#gesture-and-drag)
- [Static polish](#static-polish)
- [Type](#type)
- [Color and tokens](#color-and-tokens)
- [Depth and elevation](#depth-and-elevation)
- [Layout and spacing](#layout-and-spacing)
- [Copy](#copy)
- [Accessibility](#accessibility)
- [Pre-ship checklist](#pre-ship-checklist)

## Motion

**Should it animate at all**

| Frequency | Decision |
| --- | --- |
| 100+ times a day (keyboard shortcuts, command palette, core navigation) | None. Motion turns a reflex into a wait. Keyboard-initiated is a disqualifier, not a close call. |
| Tens of times a day (hover, list navigation, frequent toggles) | Near-imperceptible or nothing |
| Occasional (modals, drawers, toasts, settings) | Standard animation |
| Rare or first run (onboarding, success, celebration) | The whole delight budget lives here |

**Duration.** Stay under 300ms for anything in the interaction path. Large overlay surfaces are the one exception: a modal or drawer travels farther than a tooltip, so it may run to 500ms without reading as slow.

| Element | Duration |
| --- | --- |
| Button press feedback | 100–160ms |
| Tooltips, small popovers | 125–200ms |
| Dropdowns, selects | 150–250ms |
| Modals, drawers | 200–500ms |
| Marketing and explanatory | Longer is fine |

Similar elements share timing. A primary button at 200ms beside a secondary at 150ms reads as an accident.

**Easing.** Built-in CSS curves are too weak to read as intentional.

```css
--ease-out:    cubic-bezier(0.23, 1, 0.32, 1);    /* entering, exiting, interactive */
--ease-in-out: cubic-bezier(0.77, 0, 0.175, 1);   /* movement across the screen */
--ease-drawer: cubic-bezier(0.32, 0.72, 0, 1);    /* iOS-style drawers */
```

Entering or exiting uses ease-out. Movement on screen uses ease-in-out. Hover and color use ease. Constant motion uses linear. Never ease-in on UI: it delays the first frame, which is the frame the person is watching, so the same duration reads as slower.

**Springs** replace curves when motion is gesture-driven, interruptible, or should feel alive. Think in two parameters rather than three:

| Interaction | Damping / bounce | Response |
| --- | --- | --- |
| Default UI, no overshoot | damping `1.0`, bounce `0` | `0.3–0.4` |
| Move or reposition | damping `1.0` | `0.4` |
| Drawer or sheet, rotation | damping `~0.8`, bounce `0.1–0.3` | `0.3–0.4` |

Bounce is earned only when the gesture itself carried momentum. Overshoot on a menu that faded in feels wrong; overshoot on a card you flicked feels right.

**Rules that hold across cases**

- Animate `transform` and `opacity` only. Width, height, margin, padding, top, and left trigger layout and paint and drop frames. Height is tolerated for accordions, where no transform equivalent exists.
- Never `transition: all`. Name the properties.
- Never enter from `scale(0)`. Nothing appears from nothing. Start entrances at `scale(0.9–0.97)` with `opacity: 0`.
- Buttons scale to `0.96` on press, never below `0.95`, which reads as exaggerated. Use `transition: scale 200ms ease-out` so an interrupted press returns smoothly.
- Popovers, dropdowns, and tooltips scale from their trigger, so set `transform-origin` to it. Modals are the exception and stay centered, since nothing anchors them.
- Enter and exit along the same path. What slid in from the right leaves to the right.
- Exits run shorter and quieter than entrances.
- Asymmetric timing where a person is deciding: slow on the deliberate phase (a hold-to-confirm at 2s linear), snappy on the system response (release at 200ms ease-out).
- Prefer CSS transitions over keyframes for anything triggered rapidly. Transitions retarget mid-flight; keyframes restart from zero. Keep keyframes for sequences that run once.
- CSS animations of compositor properties (`transform`, `opacity`, `filter`) run off the main thread and stay smooth while the page is busy; a CSS animation of a layout property gets no such protection. Use JS motion for dynamic and interruptible cases, CSS for predetermined ones. In Framer Motion specifically, the `x`/`y`/`scale` shorthands run on the main thread via `requestAnimationFrame` and drop frames under load; pass the full `transform` string when that matters.
- Never drive a child's transform from a CSS variable on the parent. It recalculates styles for every child.
- Stagger individual items 30–80ms apart; stagger staged groups about 100ms apart. Stagger is decorative and must never block interaction.
- Cross-fade swapping icons: the entering icon runs scale `0.25 → 1`, opacity `0 → 1`, blur `4px → 0`, and the exiting one reverses it. The deep scale is a deliberate exception to the entrance floor — at icon size, `0.95` reads as no motion at all.
- Pause looping animations when off-screen.
- Disable transitions entirely while the theme switches.
- A crossfade that still looks wrong after tuning duration and easing needs a brief `filter: blur(2px)` to merge the two states. Keep blur under 20px, since large values are expensive in Safari.
- `will-change` only for `transform`, `opacity`, and `filter`, only while motion is active. It also fixes the 1–2px random shift some elements get while animating in Safari on iOS.

## Gesture and drag

- Respond on pointer-down, not on release. Feedback that waits for the click reads as dead.
- Track one to one for the whole gesture and respect the offset from where the person grabbed. Snapping to the element's center breaks the illusion immediately.
- Capture pointer events once a drag starts so tracking survives leaving the element.
- Ignore extra touch points after a drag begins, otherwise switching fingers teleports the element.
- Require about 10px of movement before committing to a direction.
- Dismiss on velocity, not distance alone. `Math.abs(distance) / elapsedMs` above roughly `0.11` means a flick, and a flick should count.
- Project where the gesture was going rather than snapping from the release point: `current + (v / 1000) * d / (1 - d)` with `d ≈ 0.998`, then snap to the target nearest that projection and hand the release velocity to the spring.
- Rubber-band past boundaries instead of stopping hard. Resistance rises with distance.
- Animate from the value currently on screen, never from the logical target, or an interrupted gesture jumps.
- Fire lightweight actions during the gesture. Fire destructive ones on release only.

## Static polish

- **Concentric radius.** Nested rounded elements: `outer = inner + padding`. Equal radius on parent and child is the most common reason a card looks slightly wrong.
- **Tabular numbers.** `font-variant-numeric: tabular-nums` on counters, prices, timers, and table columns, so values do not shift the layout as they change. Skip it in a monospace face, which already has them.
- **Optical alignment.** Fix what looks off rather than what measures even. Icon-side padding runs about 2px tighter than text-side; a play triangle sits about 2px right of geometric center.
- **Image edges.** A 1px outline offset by `-1px`, black at 8% opacity in light mode and white at 8% in dark. Never a tinted near-black, which picks up the surface underneath and reads as dirt on the edge.
- **Icons.** One set per surface, never two libraries mixed. Stroke matches the optical weight of adjacent text: `1.5px` beside regular, `2px` beside semibold. One SVG using `currentColor`, with hover, selected, and disabled coming from CSS rather than separate assets. Outline is the default and fill marks the active state.
- **Decorative elements** get `pointer-events: none`, so glows and gradients never swallow a click meant for a control.
- **Interactive elements** disable `user-select` on their inner content.

## Type

- Serve `.woff2`. Never `.ttf` or `.otf` on the web.
- Prefer the real property over the raw feature tag: `font-weight: 650` rather than `font-variation-settings`, `font-variant-numeric: tabular-nums` rather than `font-feature-settings`. Properties keep working when a non-variable fallback renders.
- Pick a ratio and step it: about 1.2 for dense or calm UI, 1.25 for most product UI, 1.333 for expressive. From a 14–16px body that yields a visibly distinct scale rather than 15/16/17 mush.
- Weight and color carry hierarchy before size does. Three tiers at one size: value at 600 primary, label at 500 secondary, meta at 400 muted.
- No weights below 400 under 18px. Weights under 300 are display-only.
- Weight must not change on hover or selection, since it shifts the layout.
- Heading sizes descend with level. A subordinate heading should never outweigh its parent.
- Line-height by role: headings near 1.1, body 1.5 to 1.6, unitless so it scales. Anything wrapping to three or more lines needs at least 1.4 whatever the row height.
- Tracking by size: slightly negative on large headings, slightly positive on small uppercase labels, neither on body.
- Cap long-form measure at 60 to 75 characters.
- `text-wrap: balance` on headings, `text-wrap: pretty` on descriptions, neither in long-form text.
- `overflow-wrap: break-word` where long words, links, or IDs can escape; `white-space: nowrap` on labels and badges.
- `-webkit-font-smoothing: antialiased` and `-moz-osx-font-smoothing: grayscale` once at the root, never per component.
- Inputs never go below 16px on touch, or iOS zooms on focus. Never block zoom to work around it.
- Truncated text keeps the full value reachable in a tooltip or an expanded view.
- Store copy in natural case and control presentation with `text-transform`, so a redesign never means rewriting strings.
- Smart punctuation: curly quotes, an en dash for ranges, the single ellipsis character.
- `text-underline-position: from-font` with `text-decoration-skip-ink: auto`, so underlines clear descenders.

## Color and tokens

- Every step in a palette has a job: page background, component hover, border, solid fill, body text. Steps nothing uses are noise.
- Components bind to semantic tokens, never primitives. `--color-text-secondary`, not `--blue-500`. The primitive is the raw value; the token is how the value is used.
- Name a token for its role, never its appearance or its first use. `--color-accent-solid`, not `--color-blue-button` or `--color-sidebar-gray`.
- Never borrow a token from another role because the color happens to match. When that role changes, your element changes with it. Add a token for the missing role.
- One color, one meaning. A hue used for links and again for decoration teaches nothing.
- Reserve the accent for the brand, so "primary" does not mean both the brand and the main body text.
- Fill only the single primary action in a view. Secondary actions stay neutral.
- Roughly 60/30/10: a dominant neutral surface, a secondary tone, about 10% accent. Gray builds structure, color communicates status, action, or identity.
- Four text levels: primary, secondary, tertiary, muted. Two means the hierarchy is too flat.
- Measure contrast against the background the element actually renders on, not the page background.
- Dark mode is not the light palette reversed. Start from it, then retune chroma and lightness and recheck every pair.
- Pick one theme-switching mechanism, a media query or a class, and use it for every token.
- Gradients can name their interpolation space: `in oklab` for even brightness, `in oklch` for more vivid midtones, neither for the classic muted sRGB midpoint.

## Depth and elevation

- Choose one depth strategy and hold it: borders only, subtle shadows, layered shadows, or surface tints. Mixing them is the tell.
- Surfaces step by a few percentage points of lightness. Sidebars share the canvas background rather than introducing a second world. Popovers sit one level above their parent. Inputs sit slightly darker than their surroundings, because they receive content.
- Prefer layered transparent box-shadow for anything that lifts; keep real borders for dividers, outlines, and state. Light mode stacks a 1px ring plus two soft depths; dark mode collapses to a single ring, since depth shadows do not read on dark.
- Borders should disappear until looked for. Low-opacity rgba rather than solid hex, around `rgba(255,255,255,0.06–0.12)` on dark.
- Radius as a scale: small for inputs and buttons, medium for cards, large for modals. Never mix sharp and soft at random.
- Translucent chrome is a layer content scrolls under, not a strip that consumes space. Never stack one light translucent surface on another. Bigger surfaces read as thicker, with stronger blur and deeper shadow than small chips.
- Verify in grayscale, in both themes. If the hierarchy only works because of the accent, the hierarchy is wrong.

## Layout and spacing

- One base unit, 4 or 8px, multiples only. Random values are the clearest sign of no system.
- Group with space first, a background shape second, a separator line last. The gap between groups is at least twice the gap within one: 8px inside, 16px or more between.
- Interactive elements must look interactive. Never style a control identically to adjacent static text.
- Align to shared edges and hold them. Every stray edge reads as noise.
- Order by importance: the most important content sits near the top and the leading edge.
- Symmetrical padding unless the content genuinely demands otherwise.
- Space unevenly on purpose. Group related things tightly, then put real air between groups. Identical gaps everywhere is the sound of nobody deciding.
- Start around 12px between adjacent bordered controls and 24px of clearance around borderless icon-only ones.
- Hidden content needs a visible cue. Let the next item peek 16 to 32px past the scroll edge, or show a disclosure control.
- Content bleeds to the edges; controls and text stay inside the layout margins and safe areas.
- Breakpoints come from the content, not device presets. Hold the expanded layout until it genuinely stops fitting, and prefer container queries for component-level adaptation.
- Use logical properties (`margin-inline-start`, `padding-inline-end`) rather than left and right, so the layout mirrors correctly.
- No fixed widths or heights on text containers. Translated strings grow, and rows must wrap.
- Never park a critical action where scrolling or resizing clips it.
- Use a fixed z-index scale rather than arbitrary values, and `h-dvh` rather than `h-screen`.

## Copy

- Buttons start with a verb naming the action: "Save draft", "Delete project". Never "OK", "Let's go", or a bare yes and no on a consequential action.
- Confirmation buttons repeat the consequence, so the dialog is answerable without reading the body.
- One vocabulary per flow. Pick "Continue" or "Next" and keep it.
- One capitalization policy per element type. Sentence case is the safer default.
- Link text describes the destination. Never "Click here", and never a bare "Learn more" where several appear.
- Toggles are labeled for the state they turn on.
- Errors say how to fix, next to what failed, with no blame and no exclamation mark.
- Empty states orient and offer one next action.
- Address the reader as "you", not "the user".
- Never build a sentence by concatenating fragments around a variable. Word order changes per language, so use full templated strings with pluralization.
- Match the verb to the device: tap on touch, click with a pointer, select when both are possible.

## Accessibility

- Native elements first. `<button>` for actions, `<a href>` for navigation, never a `<div>` with a click handler. No ARIA beats wrong ARIA.
- Style `:focus-visible` rather than bare `:focus`. Never `outline: none` without a verified replacement, and use at least a 2px visible perimeter.
- Every pointer interaction needs a keyboard path: Escape closes overlays, arrow keys move within composite widgets, Enter and Space activate.
- Only `tabindex="0"` and `tabindex="-1"`. Positive values break the natural order.
- Modals set the background inert, move focus in on open, and return focus to the trigger on close.
- Icon-only controls get a descriptive `aria-label`. Never put `aria-hidden` on something focusable.
- Alt text by purpose: `alt="Search"` on a search button, not `alt="magnifying glass"`. Decorative images get `alt=""`.
- Every input gets a real `<label>`, the right `type`, and `inputmode`. A placeholder is never the label. Never block paste.
- Keep submit enabled until the request starts, then validate on submit: `aria-invalid="true"`, `aria-describedby` pointing at the error, focus on the first invalid field.
- A tooltip on a disabled control never opens for keyboard or touch. Put the explanation in visible text, or use `aria-disabled` to keep the control focusable and block the behavior in code.
- Hit areas: 24×24px is the floor, 44×44px on touch, 40×40px on dense desktop. Extend with a pseudo-element and never let two overlap. Leave no dead gaps between items in a list; grow the padding instead.
- Status never rides on color alone. Add an icon, a label, or an underline.
- Wrap movement in `@media (prefers-reduced-motion: no-preference)` so it is opt-in. Reduced motion means gentler, not none: replace slides and scales with opacity crossfades, keep color transitions, and kill parallax and autoplay entirely.
- Gate hover effects behind `@media (hover: hover)`, since on touch `:hover` sticks after a tap and reads as selected.
- `role="status"` for routine updates, `role="alert"` only for urgent errors.
- The skip-to-content link is the first focusable element, and anchored headings get `scroll-margin-top`.
- The page works at 200% zoom and reflows at 320px with no horizontal scrolling.

## Pre-ship checklist

Answer each before showing the work. A no is a revision, not a caveat.

- Does one element clearly lead, and can you name it?
- Does hierarchy survive the squint test, with nothing jumping out harshly?
- Does the interface read in grayscale, in both themes?
- Does every border, badge, divider, and card earn its place?
- Are all colors semantic tokens, named for their role rather than their appearance?
- Does every interactive element have hover, active, focus, and disabled? Does every data surface have loading, empty, error, and permission, or a stated reason one does not apply?
- Do the button labels start with verbs, and does the empty state offer a next action?
- Can the primary flow complete with the keyboard alone, with focus visible the whole way?
- Is every animation under 300ms, on transform and opacity, with a purpose you can state?
- Do hit areas clear the floor, with no dead gaps and no overlaps?
- Does mobile keep the spacing rhythm rather than collapsing it to fit?
- Swap the typeface for the usual one and the layout for a standard template: would anything feel different? Where it would not, that is where the default won.
- Read the token names aloud. Do they belong to this product, or to any product?