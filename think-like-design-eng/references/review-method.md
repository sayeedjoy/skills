# Review method

How to review an interface, your own or someone else's. Read this when the task is judging existing work rather than building new work. The rules being judged live in [craft-checklist.md](craft-checklist.md); this file is only about running the review.

Default to flagging. Approval is earned rather than assumed. A transition that runs but feels sluggish, a control that looks right and traps a keyboard, and a screen that works and reads as generic are all findings, not passes.

## Contents

- [Recon before judgment](#recon-before-judgment)
- [Evidence](#evidence)
- [Severity](#severity)
- [Blocking triggers](#blocking-triggers)
- [Consolidation and caps](#consolidation-and-caps)
- [The remedial order](#the-remedial-order)
- [Considered and rejected](#considered-and-rejected)
- [Verification](#verification)
- [Output format](#output-format)
- [Verdict](#verdict)
- [Common review failures](#common-review-failures)

## Recon before judgment

Identify the framework, styling system, component library, tokens, supported viewports, and the commands available to preview or test. Every fix gets written in the project's own idiom, so no finding arrives as a request to adopt a different stack.

Then read what the project says about its own interface: contributing guides, agent instructions, a design-system doc, interface decision records. Name what you found, or say there was none.

Read those for leverage, not permission. A documented convention is not evidence the convention is good, and "it is in the style guide" does not retire a finding. What they change is where you report it: when a shared token or guideline is the cause, report it once against that source and list the components as its locations.

## Evidence

Every finding cites a file and line and shows the current implementation. Where the artifact has no source, cite the exact screen and component.

Do not report a code-level finding from appearance alone, or a visual finding from source alone when runtime behavior decides the result. Re-read the cited code yourself before reporting it. A finding you have not confirmed at its location is a guess wearing a citation.

## Severity

One scale, whatever the domain:

- **High.** Blocks a task, misleads, hides content or controls, risks data loss, or repeats systemically.
- **Medium.** Meaningfully harms comprehension, efficiency, adaptability, or consistency.
- **Low.** Isolated polish with limited task impact.

Within a severity, rank by reach and leverage. A token or shared-component fix outranks the same symptom in one leaf component.

## Blocking triggers

Once confirmed, each of these is high on sight. They are not averaged down because the surface is minor, and they are not withheld to stay under a cap.

**Accessibility**

- An interactive control with no accessible name.
- A keyboard-reachable control with no visible focus indicator.
- A control or path reachable by pointer but not by keyboard.
- Motion or autoplay that ignores `prefers-reduced-motion`.
- Content clipped, overlapped, or unreachable at 320px width or 200% zoom.
- Body or control text whose rendered contrast fails its requirement.
- State or meaning carried by color alone.
- A destructive action with no confirmation, undo, or distinct treatment.

**Motion**

- Animation on a keyboard shortcut, command palette, or any action repeated 100+ times a day.
- `ease-in` on an entering or exiting UI element.
- An entrance from `scale(0)`.
- `transition: all`.
- Animating layout properties where a transform equivalent exists.

These set severity, not new rules. Whether the symptom is present is still a judgment; this list decides what it costs once it is.

## Consolidation and caps

One root cause is one finding, with every confirmed location listed in the same row. A row per occurrence turns one missing token into a twelve-row report and buries the rest.

Cap the output: about 5 findings for a quick pass, about 15 for a full one. A cap may shorten a report, but it may never be the reason a blocking trigger went unreported. When more triggers fire than the cap allows, list them first and say how many findings the cap excluded.

Never pad to reach the cap. A short review is a valid result, and so is no findings at all.

In a change-scoped review, separate what this change introduced or regressed from what predates it. Pre-existing issues sit outside the cap and outside the verdict, capped at about three, so touching a legacy file does not turn into a full-file audit. A change whose only findings are pre-existing is an approval.

## The remedial order

When proposing a fix, prefer the earlier move. Earlier moves are cheaper, and they are more often the correct answer.

1. **Delete it.** No purpose, too frequent, keyboard-triggered.
2. **Reduce it.** Shorter, smaller, fewer properties.
3. **Fix the easing.**
4. **Fix the origin and the physicality.**
5. **Make it interruptible.** Keyframes to transitions, or a spring for gesture-driven motion.
6. **Move it to the GPU.**
7. **Split the timing.** Slow the deliberate phase, snap the response.
8. **Polish.** Blur to bridge a crossfade, stagger a group, spring for something that should feel alive.
9. **Accessibility and cohesion.** Reduced-motion and pointer gating, and tuning to the product's personality.

The same shape applies outside motion. Deleting a card, a divider, or a badge is the first move to consider, not the last.

## Considered and rejected

Record 2 to 5 candidates you inspected and deliberately did not flag, each with the reason: the owning rule permits the current implementation, the evidence is thin, the project's convention is defensible rather than merely established, or the change would add complexity without user benefit.

This section is what separates a review from a wishlist. Restraint that stays invisible reads as an omission. Do not invent filler; if the scope contains fewer borderline candidates, include the ones that exist and say so.

## Verification

Run the safe checks the project offers, and inspect the rendered interface whenever runtime behavior or visual judgment decides the result. Report the exact command or interaction and what you observed.

For motion, replay at a fraction of speed and step frame by frame. For gestures, use real hardware. For keyboard paths, complete the flow without touching the mouse.

When a check cannot be run, label it not verified and say what remains. Never convert a verification gap into a finding, and never imply a surface was inspected when it was not.

## Output format

Findings go in one table, ordered by severity and then by reach. Never a list of separate before and after lines.

| Severity | Location | Before | After | Why |
| --- | --- | --- | --- | --- |
| High | `src/Dialog.tsx:42` | `<button><XIcon /></button>` | Add `aria-label="Close"`, mark the icon `aria-hidden` | The icon-only control has no accessible name |
| Medium | `src/Toast.tsx:18` | `transition: all 400ms ease-in` | `transition: transform 240ms var(--ease-out), opacity 160ms var(--ease-out)` | `all` animates layout by accident, and `ease-in` delays the frame being watched |
| Low | `src/Card.tsx:28` | `rounded-xl` on both card and inner button at `p-2` | `rounded-2xl` outer, `rounded-lg` inner | Nested corners should be concentric |

The **Why** cell names the rule and its cost to the person using the interface, not the rule alone.

Then the rejected-candidates table, then verification, then the verdict.

Treat a review as read-only. Do not edit source unless the person also asked for the fixes; when they do, keep the report as the change scope and re-run verification afterward.

## Verdict

End with exactly one, and never soften it:

- **Block.** One or more high findings remain.
- **Needs changes.** Only medium or low findings remain.
- **Approve.** No actionable findings remain, and the claimed coverage was verified.

List any unverified check beside the verdict.

## Common review failures

| Failure | Fix |
| --- | --- |
| One row per occurrence of the same root cause | Consolidate into one row listing every location |
| A finding with no location | Cite the file and line, or the exact screen and component |
| A visual claim inferred only from source | Inspect the rendered state, or mark it not verified |
| Unbounded low-impact polish | Respect the cap; drop low findings from a quick pass |
| Silent gaps in coverage | Show what was actually inspected, and why anything was skipped |
| No rejected candidates | Include them; restraint has to be visible to count |
| A review that quietly edits code | Stay read-only unless fixes were requested |
| Approval with actionable findings open | Use needs changes or block |
| A blocking trigger held back by the cap | List triggers first and state what the cap excluded |
| Every legacy issue in a touched file reported | Keep pre-existing findings separate, capped, and out of the verdict |