# 🌍 FixifiedTranslator

> **Advanced chat translator for Minecraft Fabric 1.21.11**  
> Translate any message with one click! Supports 35+ languages via Google Translate (free) or DeepL API.

---

## ✨ Features

### 🆓 **Works Out of the Box**
- **No API key required!** Uses Google Translate for free translation
- Install and start translating immediately

### 💬 **Auto-Translation**
- Every chat message is automatically translated to your chosen language
- `/msg` whispers are translated too
- Translation shown in **`{pink text}`** below the original message

### 🔘 **One-Click Translate Button**
- A `[🌐 Translate]` button appears on every message
- Click to get an instant translation if the auto-translation didn't catch it

### 🔤 **Commands**
| Command | Description |
|---------|-------------|
| `/trans <text>` | Translate text and send to chat |
| `/transru <text>` | Quick translation to Russian |

### 🌍 **Multi-Language UI**
Interface available in 6 languages:
- 🇬🇧 English
- 🇷🇺 Русский
- 🇫🇷 Français
- 🇮🇹 Italiano
- 🇪🇸 Español
- 🇩🇪 Deutsch

### ⚙️ **Translation Services**
| Service | Cost | API Key | Quality |
|---------|------|---------|---------|
| **Google Translate** | 🆓 Free | Not needed | Good |
| **DeepL API** | 💰 Free/Paid | Required | Excellent |

---

## 📥 Installation

1. **Download** the latest `.jar` from [Releases](https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-/releases)
2. **Install** [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11
3. **Install** [Fabric API](https://modrinth.com/mod/fabric-api)
4. **Drop** `fixifiedtranslator-*.jar` into your `mods` folder
5. **Launch** Minecraft and enjoy!

### Dependencies
- ✅ [Fabric Loader](https://fabricmc.net/use/installer/) (≥0.18.4)
- ✅ [Fabric API](https://modrinth.com/mod/fabric-api)
- ✅ Minecraft 1.21.11
- ✅ Java 21+

---

## ⚙️ Configuration

Open settings via **Mod Menu** or press the configured keybinding.

### Settings
- **API Key** (optional) — Enter DeepL API key for higher quality. Leave empty to use Google Translate (free).
- **Target Language** — Choose the language you want messages translated to.
- **Source Language** — Auto-detect or manually set the source language.
- **UI Language** — Change the mod's interface language.

---

## 🎮 How It Works

### In-Game Chat
```
Steve: Hello, how are you?
§d§l{Привет, как дела?} [🇷🇺 Перевести]
```

### Whisper /msg
```
Steve whispers to you: Good morning!
§d§l{Доброе утро!} [🇷🇺 Перевести]
```

### Manual Translation
```
/trans Hello world
[🌐 Google Translate] Hello world
→ Привет мир
(sent to chat for everyone)
```

---

## 🛠 Building from Source

```bash
# Clone the repository
git clone https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-.git
cd FixifiedTranslator-1.21.11-

# Build the mod
./gradlew build

# The .jar file will be in build/libs/
```

**Requirements:** Java 21+

---

## 🌐 Supported Languages

AR, BG, CS, DA, DE, EL, EN, ES, ET, FI, FR, HU, ID, IT, JA, KO, LT, LV, NL, PL, PT, RO, RU, SK, SL, SV, TR, UK, ZH (+ variants like EN-GB, PT-BR, etc.)

---

## 📜 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

## 💖 Credits

**Author:** [Fixified_](https://github.com/Fixifiedd)  
**Translation Engine:** Google Translate (unofficial API) & DeepL API  
**Framework:** [Fabric](https://fabricmc.net/)

---

## 🐛 Bug Reports & Feature Requests

Found a bug or have a suggestion?  
👉 [Open an Issue](https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-/issues)

---

**Enjoy translating!** 🌍✨
