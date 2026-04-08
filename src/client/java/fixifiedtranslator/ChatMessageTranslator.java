package fixifiedtranslator;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Обработчик сообщений чата - АВТОМАТИЧЕСКИ переводит сообщения
 * Формат показа:
 *   Оригинал сообщения
 *   {розовый перевод}
 *   [кнопка для ручного перевода]
 */
public class ChatMessageTranslator {
    
    public static void register() {
        // Перехватываем сообщения и показываем с переводом
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (TranslatorMod.config == null) {
                return true;
            }
            
            String plainText = message.getString();
            
            // Пропускаем слишком короткие или системные
            if (plainText.length() < 3 || plainText.startsWith("/") || plainText.startsWith("[")) {
                return true;
            }
            
            // Извлекаем текст для перевода
            String textToTranslate = extractTextForTranslation(plainText);
            
            if (textToTranslate.length() < 2) {
                return true;
            }
            
            // Пытаемся перевести синхронно
            String translated = tryTranslate(textToTranslate);
            
            // Показываем сообщение
            if (translated != null && !translated.equals(textToTranslate)) {
                // Есть перевод - показываем оригинал + перевод + кнопку
                showWithTranslation(plainText, translated, textToTranslate);
            } else {
                // Нет перевода - показываем только с кнопкой
                showWithButton(plainText, textToTranslate);
            }
            
            // Блокируем оригинальное сообщение
            return false;
        });
    }
    
    /**
     * Пытается перевести текст (синхронно)
     */
    private static String tryTranslate(String text) {
        try {
            return TranslationService.translate(
                text,
                null, // Автоопределение
                TranslatorMod.config.preferredLanguage,
                TranslatorMod.config.apiKey,
                TranslatorMod.config.isPro
            );
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Показывает сообщение с переводом
     */
    private static void showWithTranslation(String original, String translated, String textForButton) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        if (client == null || client.inGameHud == null || client.inGameHud.getChatHud() == null) {
            return;
        }
        
        // Строка 1: Оригинальное сообщение
        client.inGameHud.getChatHud().addMessage(Text.literal(original));
        
        // Строка 2: {перевод розовым} + кнопка
        MutableText line2 = Text.literal("");
        
        // {перевод} розовым
        MutableText translationText = Text.literal("§d§l{" + translated + "}")
            .styled(style -> style
                .withColor(Formatting.LIGHT_PURPLE)
                .withBold(true)
            );
        line2.append(translationText);
        
        // Кнопка
        line2.append(createTranslateButton(textForButton));
        
        client.inGameHud.getChatHud().addMessage(line2);
    }
    
    /**
     * Показывает сообщение только с кнопкой
     */
    private static void showWithButton(String original, String textForButton) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        if (client == null || client.inGameHud == null || client.inGameHud.getChatHud() == null) {
            return;
        }
        
        MutableText result = Text.literal("");
        result.append(Text.literal(original));
        result.append(createTranslateButton(textForButton));
        
        client.inGameHud.getChatHud().addMessage(result);
    }
    
    /**
     * Создаёт кнопку [Перевести]
     */
    private static MutableText createTranslateButton(String textToTranslate) {
        String translateCommand = "/transru " + textToTranslate;
        String buttonText = getTranslateButtonText();
        String hoverText = getTranslateHoverText();
        
        return Text.literal(" " + buttonText)
            .styled(style -> style
                .withClickEvent(new ClickEvent.RunCommand(translateCommand))
                .withHoverEvent(new HoverEvent.ShowText(Text.literal(hoverText)))
                .withColor(Formatting.GREEN)
                .withBold(true)
            );
    }
    
    /**
     * Извлекает текст для перевода из сообщения
     * Для /msg: "Player whispers to you: Hello" -> "Hello"
     * Для чата: весь текст
     */
    private static String extractTextForTranslation(String text) {
        // Для /msg извлекаем текст после ":"
        int colonIndex = text.lastIndexOf(':');
        if (colonIndex > 0 && colonIndex < text.length() - 1) {
            return text.substring(colonIndex + 1).trim();
        }
        
        // Для "whispers" формата
        int whisperIndex = text.toLowerCase().indexOf("whispers");
        if (whisperIndex > 0) {
            String afterWhisper = text.substring(whisperIndex + 8).trim();
            if (afterWhisper.startsWith("to you:") || afterWhisper.startsWith("to you")) {
                int colon = afterWhisper.indexOf(':');
                if (colon > 0 && colon < afterWhisper.length() - 1) {
                    return afterWhisper.substring(colon + 1).trim();
                }
            }
            return afterWhisper;
        }
        
        return text;
    }
    
    private static String getTranslateButtonText() {
        if (TranslatorMod.config == null) return "[🌐 Translate]";
        
        return switch (TranslatorMod.config.uiLanguage) {
            case "RU" -> "[🇷🇺 Перевести]";
            case "FR" -> "[🇫🇷 Traduire]";
            case "IT" -> "[🇮🇹 Traduci]";
            case "ES" -> "[🇪🇸 Traducir]";
            case "DE" -> "[🇩🇪 Übersetzen]";
            default -> "[🌐 Translate]";
        };
    }
    
    private static String getTranslateHoverText() {
        if (TranslatorMod.config == null) return "Click to translate";
        
        return switch (TranslatorMod.config.uiLanguage) {
            case "RU" -> "Нажми для перевода";
            case "FR" -> "Cliquez pour traduire";
            case "IT" -> "Clicca per tradurre";
            case "ES" -> "Haz clic para traducir";
            case "DE" -> "Klicken zum Übersetzen";
            default -> "Click to translate";
        };
    }
}
