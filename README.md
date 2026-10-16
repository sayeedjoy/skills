# zerox-skills

A collection of [Claude Code](https://docs.claude.com/en/docs/claude-code) skills by Sayeed Joy.

## Skills

| Skill | What it does |
| --- | --- |
| [`easy-bangla-skill`](easy-bangla-skill/SKILL.md) | Translates or rewrites any material into natural, easy-to-read Bangla, as if Joy were explaining it. Keeps every fact, number, name and level of certainty from the source, and leaves common English terms (`API`, `software`, `workforce`) in English where a forced translation would sound stiff. |
| [`m3-expressive-android`](m3-expressive-android/SKILL.md) | Designs and builds Android UIs in the Material 3 Expressive, Pixel-style look with Jetpack Compose. Covers dynamic color, rounded Google Sans Flex type, tonal surfaces with zero elevation, grouped list cards, shape-morphing pill buttons and springy motion. Ships drop-in `Theme.kt` and `ExpressiveComponents.kt` templates plus screen blueprints. |

### m3-expressive-android contents

```
m3-expressive-android/
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
cp -r zerox-skills/m3-expressive-android ~/.claude/skills/
cp -r zerox-skills/easy-bangla-skill ~/.claude/skills/
```

**For one project only**, copy them into that project's `.claude/skills/` folder instead.

Claude Code picks up new skills automatically. Claude uses a skill when your request matches its description, or you can call one directly with `/<skill-name>`, for example `/m3-expressive-android`.

## Usage examples

- *"Translate this article into easy Bangla."*
- *"Rewrite this formal Bangla notice so anyone can understand it."*
- *"Build a habit tracker app in Compose with a Pixel-style look."*
- *"Redesign my settings screen in Material 3 Expressive."*
