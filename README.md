# 🌍 FixifiedTranslator

> **Продвинутый переводчик чата для Minecraft Fabric 1.21.11**  
> Переводи любые сообщения в один клик! Поддержка 35+ языков через Google Translate (бесплатно) или DeepL API.

---

> **Advanced chat translator for Minecraft Fabric 1.21.11**  
> Translate any message with one click! Supports 35+ languages via Google Translate (free) or DeepL API.

---

## 🇷🇺 Описание (Russian)

### ✨ Возможности

#### 🆓 **Работает сразу после установки**
- **API ключ не нужен!** Использует Google Translate бесплатно
- Установи и сразу переводи сообщения

#### 💬 **Автоперевод**
- Каждое сообщение в чате автоматически переводится на выбранный язык
- Личные сообщения `/msg` тоже переводятся
- Перевод показывается в **`{розовом тексте}`** под оригиналом

#### 🔘 **Кнопка перевода**
- На каждом сообщении появляется кнопка `[🇷🇺 Перевести]`
- Нажми для мгновенного перевода

#### 🔤 **Команды**
| Команда | Описание |
|---------|----------|
| `/trans <текст>` | Перевести текст и отправить в чат |
| `/transru <текст>` | Быстрый перевод на русский |

#### 🌍 **Многоязычный интерфейс**
Доступно на 6 языках:
- 🇬🇧 English
- 🇷🇺 Русский
- 🇫🇷 Français
- 🇮🇹 Italiano
- 🇪🇸 Español
- 🇩🇪 Deutsch

#### ⚙️ **Сервисы перевода**
| Сервис | Стоимость | API ключ | Качество |
|--------|-----------|----------|----------|
| **Google Translate** | 🆓 Бесплатно | Не нужен | Хорошее |
| **DeepL API** | 💰 Бесплатно/Платно | Нужен | Отличное |

### 📥 Установка

1. **Скачай** последний `.jar` из [Releases](https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-/releases)
2. **Установи** [Fabric Loader](https://fabricmc.net/use/installer/) для Minecraft 1.21.11
3. **Установи** [Fabric API](https://modrinth.com/mod/fabric-api)
4. **Перетащи** `fixifiedtranslator-*.jar` в папку `mods`
5. **Запусти** Minecraft и наслаждайся!

### 🛠 Сборка из исходников

```bash
# Клонировать репозиторий
git clone https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-.git
cd FixifiedTranslator-1.21.11-

# Собрать мод
./gradlew build

# .jar файл будет в build/libs/
```

### 📋 Поддерживаемые языки

AR, BG, CS, DA, DE, EL, EN, ES, ET, FI, FR, HU, ID, IT, JA, KO, LT, LV, NL, PL, PT, RO, RU, SK, SL, SV, TR, UK, ZH (+ варианты: EN-GB, PT-BR, и т.д.)

---

## 🇬🇧 Description (English)

### ✨ Features

#### 🆓 **Works Out of the Box**
- **No API key required!** Uses Google Translate for free translation
- Install and start translating immediately

#### 💬 **Auto-Translation**
- Every chat message is automatically translated to your chosen language
- `/msg` whispers are translated too
- Translation shown in **`{pink text}`** below the original message

#### 🔘 **One-Click Translate Button**
- A `[🌐 Translate]` button appears on every message
- Click to get an instant translation if the auto-translation didn't catch it

#### 🔤 **Commands**
| Command | Description |
|---------|-------------|
| `/trans <text>` | Translate text and send to chat |
| `/transru <text>` | Quick translation to Russian |

#### 🌍 **Multi-Language UI**
Interface available in 6 languages:
- 🇬🇧 English
- 🇷🇺 Русский
- 🇫🇷 Français
- 🇮🇹 Italiano
- 🇪🇸 Español
- 🇩🇪 Deutsch

#### ⚙️ **Translation Services**
| Service | Cost | API Key | Quality |
|---------|------|---------|---------|
| **Google Translate** | 🆓 Free | Not needed | Good |
| **DeepL API** | 💰 Free/Paid | Required | Excellent |

### 📥 Installation

1. **Download** the latest `.jar` from [Releases](https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-/releases)
2. **Install** [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.11
3. **Install** [Fabric API](https://modrinth.com/mod/fabric-api)
4. **Drop** `fixifiedtranslator-*.jar` into your `mods` folder
5. **Launch** Minecraft and enjoy!

### 🛠 Building from Source

```bash
# Clone the repository
git clone https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-.git
cd FixifiedTranslator-1.21.11-

# Build the mod
./gradlew build

# The .jar file will be in build/libs/
```

### 🌐 Supported Languages

AR, BG, CS, DA, DE, EL, EN, ES, ET, FI, FR, HU, ID, IT, JA, KO, LT, LV, NL, PL, PT, RO, RU, SK, SL, SV, TR, UK, ZH (+ variants like EN-GB, PT-BR, etc.)

---

## 🎮 Как это работает / How It Works

### 🇷🇺 В игре:
```
Steve: Hello, how are you?
§d§l{Привет, как дела?} [🇷🇺 Перевести]
```

### 🇬🇧 In-Game:
```
Steve: Hello, how are you?
§d§l{Привет, как дела?} [🇷🇺 Перевести]
```

### 🇷🇺 Личные сообщения / Whisper /msg:
```
Steve шепчет вам: Доброе утро!
§d§l{Good morning!} [🇷🇺 Перевести]
```

### 🇬🇧 Manual Translation:
```
/trans Hello world
[🌐 Google Translate] Hello world
→ Привет мир
(sent to chat for everyone)
```

---

## ⚙️ Настройки / Configuration

Открой настройки через **Mod Menu** или нажми привязанную клавишу.

### 🇷🇺 Настройки:
- **API ключ** (необязательно) — Введи ключ DeepL для лучшего качества. Оставь пустым для Google Translate (бесплатно).
- **Язык перевода** — Выбери язык, на который переводить сообщения.
- **Исходный язык** — Автоопределение или ручной выбор.
- **Язык интерфейса** — Смени язык интерфейса мода.

### 🇬🇧 Settings:
- **API Key** (optional) — Enter DeepL API key for higher quality. Leave empty to use Google Translate (free).
- **Target Language** — Choose the language you want messages translated to.
- **Source Language** — Auto-detect or manually set the source language.
- **UI Language** — Change the mod's interface language.

---

## 📜 Лицензия / License

Этот проект лицензирован по **MIT License**. Подробности в [LICENSE](LICENSE).

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

## 💖 Авторы / Credits

**Автор / Author:** [Fixified_](https://github.com/Fixifiedd)  
**Движок перевода / Translation Engine:** Google Translate (unofficial API) & DeepL API  
**Фреймворк / Framework:** [Fabric](https://fabricmc.net/)

---

## 🐛 Ошибки и предложения / Bug Reports & Feature Requests

Нашёл ошибку или есть предложение? / Found a bug or have a suggestion?  
👉 [Создать Issue / Open an Issue](https://github.com/Fixifiedd/FixifiedTranslator-1.21.11-/issues)

---

**Приятного перевода! / Enjoy translating!** 🌍✨
