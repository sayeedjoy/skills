# zerox-skills

A collection of [Claude Code](https://docs.claude.com/en/docs/claude-code) skills by Sayeed Joy (Zerox), plus a few skills from other authors that are part of the same daily setup.

## Skills

| Skill | What it does |
| --- | --- |
| [`think-like-zerox`](think-like-zerox/SKILL.md) | Zerox's default working style. Acts as a candid thinking partner: challenges assumptions, grills unclear ideas one question at a time, and moves substantial work through **Grill → Refine → Spec → Plan → Tasks → Test and implement → Review → Simplify → Ship**. It starts at the first stage that is still open, and plain questions get a direct answer. Routes each stage to a specialist skill (see [Dependencies](#dependencies)). |
| [`think-like-design-eng`](think-like-design-eng/SKILL.md) | Design-engineer judgment for anything people look at or use. Decide the intent before the pixels, name the defaults you are rejecting, build hierarchy from space and weight before size, treat copy and accessibility as part of the interface, and verify by looking at the running UI. Ships a craft checklist (motion, type, color, layout, copy, a11y) and a structured UI review method. |
| [`commit-discipline`](commit-discipline/SKILL.md) | Commit messages as one line: `type: what changed`, lowercase, no trailing period, no body by default. Types are `feat`, `fix`, `refactor`, `chore`, `docs` and `style`. When a commit does several things, the line names all of them. Never credits an AI tool. |
| [`easy-bangla-skill`](easy-bangla-skill/SKILL.md) | Translates or rewrites any material into natural, easy-to-read Bangla, as if Joy were explaining it. Keeps every fact, number, name and level of certainty from the source, and leaves common English terms (`API`, `software`, `workforce`) in English where a forced translation would sound stiff. |
| [`m3-expressive-android`](m3-expressive-android/SKILL.md) | Designs and builds Android UIs in the Material 3 Expressive, Pixel-style look with Jetpack Compose. Covers dynamic color, rounded Google Sans Flex type, tonal surfaces with zero elevation, grouped list cards, shape-morphing pill buttons and springy motion. Ships drop-in `Theme.kt` and `ExpressiveComponents.kt` templates plus screen blueprints. |

### Repository layout

```
.
├── think-like-zerox/
│   ├── SKILL.md                         # stage workflow, grilling rules, skill routing
│   ├── references/ui-ux-skills.md       # which UI/UX skill to use for which job
│   └── agents/openai.yaml               # display metadata for OpenAI-style agents
├── think-like-design-eng/
│   ├── SKILL.md                         # the design-engineer moves
│   └── references/
│       ├── craft-checklist.md           # motion, type, color, layout, copy, a11y, pre-ship
│       └── review-method.md             # evidence, severity, blocking triggers, verdict
├── commit-discipline/
│   └── SKILL.md
├── easy-bangla-skill/
│   └── SKILL.md
└── m3-expressive-android/
    ├── SKILL.md                         # workflow + design language + review checklist
    ├── assets/templates/
    │   ├── Theme.kt                     # dynamic color, rounded font, expressive motion, edge-to-edge
    │   └── ExpressiveComponents.kt      # morphing buttons, settings rows, dialogs, empty state, ...
    ├── references/
    │   ├── setup.md                     # Gradle dependencies, versions, opt-ins
    │   ├── components.md                # FAB menu, floating toolbar, icon badge palette, ...
    │   └── screens.md                   # home, settings, bottom sheet, onboarding, adaptive layout
    └── evals/evals.json                 # test prompts for the skill
```

## Installation

Clone the repo, then copy the skill folders you want into a Claude Code skills directory.

**For all your projects** (personal skills):

```bash
git clone https://github.com/sayeedjoy/zerox-skills.git
cd zerox-skills

# pick the ones you want
cp -r think-like-zerox ~/.claude/skills/
cp -r think-like-design-eng ~/.claude/skills/
cp -r commit-discipline ~/.claude/skills/
cp -r easy-bangla-skill ~/.claude/skills/
cp -r m3-expressive-android ~/.claude/skills/
```

**For one project only**, copy them into that project's `.claude/skills/` folder instead.

Claude Code picks up new skills automatically. Claude uses a skill when your request matches its description, or you can call one directly with `/<skill-name>`, for example `/m3-expressive-android`.

> **Note:** `think-like-design-eng` declares its name as `think-like-a-design-engineer` in its frontmatter, so call it with `/think-like-a-design-engineer`.

### Dependencies

`think-like-zerox` is a router. It hands each stage to another skill, and most of those skills are **not** in this repo:

- Workflow: `using-agent-skills`, `interview-me`, `idea-refine`, `spec-driven-development`, `planning-and-task-breakdown`, `incremental-implementation`, `test-driven-development`
- Review and cleanup: `open-code-review-delegate` (required for the final review), `code-review-and-quality`, `code-simplification`
- Shipping: `documentation-and-adrs`, `git-workflow-and-versioning`, `shipping-and-launch`
- UI/UX: `think-like-design-eng` (in this repo), plus the skills listed in [`ui-ux-skills.md`](think-like-zerox/references/ui-ux-skills.md)

Install the ones you need alongside it. If a skill is missing, `think-like-zerox` reports which one and which stage it affects. It does not pretend to have followed it.

The other four skills stand alone.

## Usage examples

- *"Grill me on this idea before we build anything."*
- *"Spec and plan a sync feature for my notes app."*
- *"Review this settings page. Does it feel generic?"*
- *"Write the empty state and error copy for this form."*
- *"Write a commit message for my staged changes."*
- *"Translate this article into easy Bangla."*
- *"Rewrite this formal Bangla notice so anyone can understand it."*
- *"Build a habit tracker app in Compose with a Pixel-style look."*
- *"Redesign my settings screen in Material 3 Expressive."*

## Credits

`commit-discipline` and `think-like-design-eng` are by [Zaid Mukaddam](https://github.com/zaidmukaddam), as stated in their frontmatter. The other skills are by Sayeed Joy.
