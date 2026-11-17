# UI/UX skill routing

Read this reference for UI/UX work. Start with `think-like-design-eng`
(declared name: `think-like-a-design-engineer`), then select the smallest set of
supporting skills that covers the task. Read each selected skill's `SKILL.md`
before applying it. These supporting skills were verified in the local skill
installation; use the current catalog to resolve their paths and availability.

## Design and implementation

| Need | Skill | When to use it |
| --- | --- | --- |
| Primary design judgment | `think-like-design-eng` / `think-like-a-design-engineer` | Every UI/UX task: decide intent, hierarchy, copy, states, reuse, and verification. |
| New aesthetic direction | `frontend-design` | Creating a new interface or deliberately reshaping its visual identity. |
| Web interface implementation | `frontend-ui-engineering` | Building accessible, responsive components, layouts, and application states. |
| Surface and icon polish | `better-ui` | Refining radius, optical alignment, shadows, icon states, and motion values within the product's system. |
| Interaction and motion judgment | `emil-design-eng` | Component behavior, hover and press feedback, popovers, tooltips, transitions, and gestures when no narrower skill fits. |
| Library selection | `pick-ui-library` | Choosing a missing frontend capability after inspecting existing dependencies and primitives. |
| shadcn components | `shadcn` | Projects using shadcn/ui, registries, presets, or `components.json`. |
| React component architecture | `vercel-composition-patterns` | Designing reusable React APIs or refactoring prop proliferation and composition. |
| React/Next.js implementation quality | `vercel-react-best-practices` | Writing or reviewing React/Next.js components, data fetching, rendering, and bundle behavior. |
| Android interfaces | `m3-expressive-android` | Jetpack Compose/Kotlin screens with Material 3 Expressive or Pixel-style requirements. Do not impose web conventions on native UI. |

Reuse project primitives and tokens before choosing a new library. Preserve
existing identity for polish and audits; use `frontend-design` when a new visual
direction is actually in scope. Add decorative features only when requested.

## UI checks and audits

| Need | Skill | Evidence or boundary |
| --- | --- | --- |
| Existing-product design audit | `improve-ui` | Trace the governing design system, prove findings, and prepare selected implementation plans. Product source stays read-only; follow its limits on rendered inspection and plan files. |
| Web UI/UX code review | `web-design-guidelines` | Retrieve its current guidelines, inspect the scoped UI files, and provide actionable file-and-line findings. |
| Accessibility audit or fixes | `accessibility` | Check keyboard use, accessible names, focus, semantics, contrast, zoom, and assistive-technology behavior as applicable. |
| Browser runtime verification | `browser-testing-with-devtools` | Exercise important states and flows, responsive widths, keyboard navigation, console/network behavior, and rendered output. Requires the browser tooling described by the skill. |
| Broad website quality audit | `web-quality-audit` | Combine runtime evidence and source inspection across performance, accessibility, SEO, and best practices when a broad audit is requested. |
| Web loading and responsiveness | `performance` | Investigate measured page-speed and loading problems. |
| Core Web Vitals | `core-web-vitals` | Diagnose LCP, INP, or CLS using relevant lab or field evidence. |
| Web compatibility and quality | `best-practices` | Review security, compatibility, or code-quality concerns when requested or warranted. |
| Search visibility | `seo` | Address search metadata, crawlability, structured data, or indexing when in scope; this is separate from visual design. |
| Final code review | `open-code-review-delegate` with `code-review-and-quality` | Review changed code against requirements and applicable UI checks; retain OCR delegation's rules, finding format, and coverage accounting. |

Run the relevant UI checks during implementation rather than saving them all for
the end. Code review does not prove rendered quality, and screenshots do not
prove keyboard or interaction correctness. Distinguish measured findings from
source-only hypotheses. If tooling is unavailable, state what remains unverified
and use the skill's supported fallback without inventing runtime evidence.

For audit-only work, keep product source unchanged. If fixes are also authorized,
perform them in the implementation stage using the relevant build skills;
respect the audit skill's own boundaries. Consolidate overlapping findings by
root cause rather than producing separate reports for every selected skill.
