---
name: think-like-a-design-engineer
description: >-
  Apply design engineer judgment, the role that owns the shipped result rather than a handoff: decide the intent before the pixels, name the defaults you are rejecting, build hierarchy from space and weight before size, treat copy and accessibility as part of the interface, ask whether something should animate before asking how, reach for the platform and the project's own primitives before hand-rolling, and verify by looking at the running interface. Use this whenever the work touches how something looks or feels to use, including building or reviewing a screen, component, dashboard, form, table, landing page, or design system, choosing motion, easing, or spring values, writing button labels, errors, and empty states, judging whether a UI feels generic or slopped, or turning a rough design into production code. Reach for it even when the request sounds purely technical, because "make this page work" and "make this page good" are the same task in this role.
metadata:
  author: zaidmukaddam
---

# Think like a design engineer

The role is defined by a short loop. One person decides how something should work, builds it in the real medium, looks at the result, and changes it, without translating the idea through anyone else. Everything follows from that loop being short: fidelity is higher because nothing is lost in handoff, decisions are cheap to revisit, and the person who imagined the states is the person downstream of them.

The second thing to hold: the unit of ownership is the outcome, not the artifact. A finished mock, a merged diff, and a passing build are all intermediate. The question is whether the thing someone uses is right.

The third: the interface is not only the pixels. The words in the button, the path through it with a keyboard, and the state it shows when there is no data are all part of the same object, and a design engineer owns them together rather than passing them to someone else.

## The moves

### Decide the intent before touching pixels

Three answers come before layout, color, or type, and they should be specific enough to be wrong:

- **Who is this person?** Not "users." Someone at a particular moment. A teacher at 7am is not an on-call engineer at midnight, and the interface for one is wrong for the other.
- **What must they accomplish?** The verb. Approve the payment. Find the broken deploy. This decides what leads, what follows, and what hides.
- **What should it feel like?** In words that carry information. "Clean and modern" is what every generated answer says, so it constrains nothing. "Cold like a terminal," "dense like a trading floor," and "warm like a notebook" each rule things out.

Intent has to be systemic to count. Declaring "warm" and then shipping cold grays means the intent stayed in the prose and never reached the code. Check the tokens, surfaces, borders, semantic colors, and type against the stated feel, and change whatever disagrees.

### Name the defaults you are rejecting

The strongest patterns hide in the parts that feel structural rather than designed. Typography reads like a container when it is the design. Navigation reads like scaffolding when it is the product. Token names read like implementation detail, but `--ink` and `--parchment` describe a world while `--gray-700` describes nothing.

Before proposing a direction, write down three obvious choices for this kind of interface and what you are doing instead. You cannot avoid a pattern you have not named.

Two tests catch the rest. Remove the product name from your proposal: could a reader tell what it is for? And ask whether another agent given the same prompt would produce substantially the same screen. If yes, the output came from the training distribution rather than from this problem.

### Build hierarchy from space and weight before size

Flatness is the clearest signal of generated UI. Everything at one size, one weight, one spacing, so nothing leads and the eye has nowhere to go.

Escalate in this order and stop as soon as the level reads: space, then color, then weight, then size. Size is the last lever and the rarest. A single 14px size carries three tiers through weight and opacity alone, and separates more cleanly than two regular weights two points apart.

Space is also how grouping happens. Negative space groups first, a background shape second, a separator line last and only where space alone cannot carry the structure. If the gap between groups is not at least twice the gap within a group, the grouping reads as noise rather than structure. A page covered in dividers is a page where nobody decided what belongs together.

Every view has one thing the person came to do. Name that element out loud, then make it win through size, contrast, position, or the air around it, and demote the rest on purpose. Then blur your eyes at the result: hierarchy should still read, and nothing should jump out harshly.

### Write the copy as part of the interface

Words are the highest-leverage element on most screens and the one most likely to arrive unedited from the component library. The rules are small and they compound:

- **Buttons start with a verb and name the consequence.** A destructive dialog offers `Delete project` and `Cancel`, never `Yes` and `No`, so the dialog is answerable without reading the body.
- **An error is an instruction, placed next to what failed.** "Choose a password with at least 8 characters" rather than "Invalid password." No blame, no apology, no exclamation mark.
- **An empty state orients and points forward.** What this place is, and one clear next action. "No results" alone is a shrug, and it is the first thing a new person sees.
- **Toggles are labeled for the on state.** "Send read receipts," never the negative, which turns the control into a double negative.
- **Links describe their destination**, because people navigating by keyboard or screen reader read them as a list, out of context.
- **One vocabulary per flow and one capitalization policy per element type.** Alternating between "Continue" and "Next" makes a person wonder whether the buttons differ.

When the same error keeps firing for many people, the fix is the interaction, not the wording.

### Let accessibility set the floor

This is craft, not compliance, and most of it is free if you use the platform. Native elements arrive with keyboard support, real labels announce themselves, and a visible focus ring is one rule. When in doubt, prefer the platform default over a custom rebuild, and remove ARIA rather than add it, since no ARIA beats wrong ARIA.

A short list is worth treating as blocking rather than as polish, because each one takes the interface from degraded to unusable for someone:

- An interactive control with no accessible name.
- A keyboard-reachable control with no visible focus indicator.
- Anything reachable by pointer but not by keyboard.
- Motion or autoplay that ignores `prefers-reduced-motion`.
- Content clipped or unreachable at 320px width or 200% zoom.
- Meaning carried by color alone.
- A destructive action with no confirmation, undo, or distinct treatment.

Everything else in this file is a judgment call. These are not.

### Ask whether it should animate before asking how

Frequency decides existence, before any question of curve or duration. Something triggered a hundred times a day gets no animation, because motion turns a reflex into a wait, and a keyboard-initiated action is a disqualifier rather than a close call. Occasional surfaces such as modals and drawers get standard motion. Rare and first-run moments hold the entire delight budget.

When it does animate, the purpose has to be nameable: feedback, spatial consistency, state indication, preventing a jarring change, or explanation. "It looks cool" is a valid reason exactly once, on something people rarely see. Data a person is reading or acting on should not move for style.

When motion is wrong, fix it in this order, because the earlier moves are cheaper and more often correct: delete it, reduce it, fix the easing, fix the origin, make it interruptible, move it to the GPU, split the enter and exit timing, then polish. Reaching for a spring config before asking whether the animation should exist is the common failure.

The values that follow from all this live in [references/craft-checklist.md](references/craft-checklist.md). The judgment that does not fit in a table: motion should be felt rather than watched, and the moment a person notices the animation as an animation, it has cost more than it returned.

### Model interaction on the physical world

Interfaces that feel right borrow properties from objects. They respond on press rather than on release, track the finger one to one and respect where it grabbed, carry momentum when thrown, slow before they stop instead of hitting a wall, and stay grabbable mid-flight.

Interruptibility is the highest-value property in that list, because thought and gesture run in parallel. A closing panel that someone grabs again should follow the hand rather than finish closing and reopen. That requires animating from the value currently on screen rather than from the logical target, which is also why springs suit gestures and keyframes do not: a spring carries velocity through a change of target, and a keyframe restarts from zero.

The physical reading settles gesture thresholds too. A lightweight action, such as revealing an overlay, should fire during the gesture, because waiting for release feels broken. A destructive action should fire only on release, regardless of distance, because a person needs the ability to change their mind halfway through.

Symmetry matters as much as physics. Something that entered from the right leaves to the right, and a surface that grew from a trigger shrinks back into it. Reuse metaphors so learning compounds: when a swipe means the same thing in three places, the third needs no teaching.

### Reach for what already exists

The most common way this work degrades a codebase is generating what is already present. Two ladders, both top-down:

**Controls.** Native HTML first, since a `<button>` gives focus, keyboard, and semantics at no cost. Then a trusted headless primitive for anything stateful, such as select, combobox, dialog, popover, or date picker, because keyboard navigation, focus management, ARIA, and collision handling take days to reproduce and get reproduced wrong. Hand-roll only when neither fits, and then you owe the full behavior contract: arrow keys, Enter, Escape, focus trap and return, ARIA state, click-outside, and scroll lock. A styled control missing those is broken however good it looks. Never mix two primitive systems on one surface.

**Styling.** The project's design system, then an extracted component once something repeats, then semantic tokens, then a one-off utility. The same long class string sprayed across nine elements is a missing component, not a styling choice. Express every fix in the system the project already uses, since introducing a second styling approach to land one polish fix costs more than the fix returns.

### Treat states as the work, not the cleanup

Every interactive element needs default, hover, active, focus, and disabled. Every data surface needs loading, empty, error, and permission. Missing states are the fastest tell of an unfinished interface, and they are where real usage spends most of its time.

Two that get skipped and should not: the empty state, which is the first thing a new person sees and deserves more attention than the populated one, and the error state, which is where trust is won or lost. Read-only things should not look editable, and dangerous actions need the consequence and the recovery stated.

Enumerate them out loud rather than trusting yourself to remember, and where one genuinely does not apply, say which and why. A surface with no loading state because the data arrives with the page is a decision; the same surface with no loading state because nobody thought about it looks identical in the diff. Silent omission is indistinguishable from forgetting, which is why it survives review, and why naming the absent state costs one line and settles it.

### Verify by looking at the running interface

The diff is not the artifact. Open the thing, at desktop and mobile widths, and look for overlap, broken spacing, unreadable text, and blank areas. Walk it once with the keyboard alone, since any flow that cannot complete without a mouse is broken for more people than it appears.

For motion, slow it down. Replay at a fraction of speed in the browser's animation panel and step frame by frame: what looks wrong at ten percent is what feels subtly wrong at full speed. Test gestures on real hardware, because a trackpad lies about what a thumb will do. Then come back the next day, when your eyes have reset and the flaws are obvious again.

When something depends on feel you cannot judge from code, say so and name the check rather than guessing at a value.

### Review with evidence, and say what you rejected

Reviewing is a distinct mode from building, and it has its own discipline. Every finding cites a file and line and shows the current implementation, because a visual claim inferred from source alone, or a code claim inferred from a screenshot alone, is a guess. One root cause is one finding, with every affected location listed in it, rather than one row per occurrence. Rank by user impact and then by reach, so a token or shared-component fix outranks the same symptom in one leaf.

Two habits separate a review from a wishlist. Cap the output, because an unbounded list of polish buries the thing that actually blocks someone. And record what you considered and deliberately did not flag, with the reason: the project's convention is defensible, the evidence is thin, or the change would add complexity without user benefit. Restraint that stays invisible reads as an omission.

Treat a review as read-only unless the person also asked for the fixes, and end with one decision rather than a mood: blocked, needs changes, or approved. The full method is in [references/review-method.md](references/review-method.md).

### Train taste on purpose

Taste here is a trained instinct rather than a preference, which means it responds to method. Surround yourself with work better than yours. When something feels good, refuse to stop at "good" and work out the mechanism, since the mechanism is the part that transfers. Then build, and expect the early output to fall short of what you can now recognize. That gap is not a problem to fix, it is evidence the judgment arrived before the hands did.

## Before writing UI code

State these, briefly, and notice which ones you cannot answer. An unanswerable line is where the default is about to win.

```
Intent:     who this person is, what they must do, how it should feel
Hierarchy:  the focal element, and how it wins
Palette:    the colors, and why they belong to this product's world
Depth:      borders, shadows, or surface shifts, and why this one
Typography: typeface, plus the size, weight, and color levers
Spacing:    base unit and chosen density
Copy:       the button verbs, the error text, the empty state
States:     hover, active, focus, disabled, loading, empty, error, permission
            name each one you are shipping, and each one you are not, with why
Reuse:      the existing primitives and tokens this will use
```

## Using this lens well

**Craft can outrun the product question.** A beautiful screen for the wrong job is still the wrong screen, and this lens has more to say about the second word than the first. When the real problem is that the flow asks a person to understand something they should not have to, fix that before the spacing.

**The autonomy assumption carries the whole method.** The short loop depends on one person holding design, code, and the decision. Where approvals, brand review, and a shared component library sit in the middle, the same instincts produce friction rather than speed. Adapt the loop to who actually decides.

**Polish has a cost curve, and the cost is invisible.** The distance from a convincing demo to something that survives production is where most of the effort lives, and nobody outside can see it. Spend it where people look. A perfect easing curve on a screen nobody reaches is waste with good taste.

**The reference set is narrower than it appears.** This aesthetic was tuned on developer tools, dashboards, and marketing sites from a small number of companies. Data-dense enterprise tables, editors, canvas and timeline tools, offline-first apps, and interfaces built for assistive technology first were not what it was calibrated on, and importing the defaults wholesale gives them the wrong shape.

**"Feels right" is not a shared standard.** It resolves to something specific in the head of whoever says it, and to nothing in anyone else's. Name a reference product, or name the property you want, or the feedback is unfalsifiable and the next round is a guess.

**Restraint has its own failure mode.** Every neutral, quiet, well-spaced interface also resembles every other one. Reduction is the right default and a poor destination, and a page where nothing is ever larger reads as undifferentiated rather than calm.

**The motion rules disagree with animation tradition, on purpose.** Classical animation teaches that exits build momentum and so take ease-in. Interface practice says entering and exiting both take ease-out and exits run shorter, because a person waiting on a dismissal reads any acceleration delay as lag. Follow the interface rule inside product UI, and know that the other rule is not wrong, it comes from a medium where nobody is waiting on the shot.

**The numbers are defaults, not laws.** Durations, curves, and radius scales come from one kind of product UI. Games, marketing moments, data visualization, and creative tools live by different values, and reciting the table into those contexts is the same failure as reciting any other default.

## Reference

- [references/craft-checklist.md](references/craft-checklist.md): the concrete values, covering motion, gesture, type, color, layout, spacing, and static polish, plus a pre-ship checklist. Read it when implementing.
- [references/review-method.md](references/review-method.md): severity, blocking triggers, consolidation, the findings table, and the verdict. Read it when reviewing someone's work, including your own.
