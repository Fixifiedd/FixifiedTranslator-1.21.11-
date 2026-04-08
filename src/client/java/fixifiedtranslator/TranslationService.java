package fixifiedtranslator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Универсальный сервис перевода
 * 
 * Приоритет сервисов:
 * 1. DeepL API (если введён API ключ) - лучшее качество
 * 2. Google Translate (бесплатный, без ключа) - fallback
 * 
 * Google Translate использует неофициальный веб-API endpoint.
 * Работает без ключа, ~5000 символов за запрос.
 */
public class TranslationService {
    
    // Google Translate unofficial endpoint
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single";
    
    /**
     * Переводит текст с автоопределением сервиса
     * 
     * @param text Текст для перевода
     * @param sourceLang Исходный язык (null для автоопределения)
     * @param targetLang Целевой язык
     * @param apiKey API ключ DeepL (может быть пустым)
     * @param isPro DeepL Pro аккаунт
     * @return Переведённый текст
     */
    public static String translate(String text, String sourceLang, String targetLang, String apiKey, boolean isPro) throws Exception {
        // Если есть DeepL API ключ - используем DeepL (лучше качество)
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            try {
                return DeepLApi.translate(text, sourceLang, targetLang, apiKey, isPro);
            } catch (Exception e) {
                // Если DeepL упал - пробуем Google
                System.err.println("DeepL failed, falling back to Google: " + e.getMessage());
                return translateWithGoogle(text, sourceLang, targetLang);
            }
        }
        
        // Иначе используем бесплатный Google Translate
        return translateWithGoogle(text, sourceLang, targetLang);
    }
    
    /**
     * Перевод через Google Translate (бесплатный, без ключа)
     * Использует unofficial web API
     */
    private static String translateWithGoogle(String text, String sourceLang, String targetLang) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        // Определяем языки
        String source = (sourceLang != null && !sourceLang.isEmpty()) ? sourceLang : "auto";
        String target = targetLang != null ? targetLang : "en";
        
        // Конвертируем коды языков в формат Google
        source = convertLangCode(source);
        target = convertLangCode(target);
        
        // Кодируем текст
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        
        // Создаём URL с параметрами
        String urlString = GOOGLE_TRANSLATE_URL + 
            "?client=gtx" +
            "&sl=" + source +
            "&tl=" + target +
            "&dt=t" +
            "&q=" + encodedText;
        
        // Отправляем запрос
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        
        if (responseCode != 200) {
            throw new Exception("Google Translate error: HTTP " + responseCode);
        }
        
        // Читаем ответ
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        
        // Парсим JSON ответ
        // Формат: [[["перевод","оригинал",null,null,1]],null,"detected_lang",null,null,null,[[...]]]
        JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
        
        if (jsonArray.size() == 0 || jsonArray.get(0).isJsonNull()) {
            throw new Exception("No translation found");
        }
        
        // Собираем перевод из массива
        StringBuilder translatedText = new StringBuilder();
        JsonArray sentences = jsonArray.get(0).getAsJsonArray();
        
        for (int i = 0; i < sentences.size(); i++) {
            JsonArray sentence = sentences.get(i).getAsJsonArray();
            if (sentence.size() > 0 && !sentence.get(0).isJsonNull()) {
                translatedText.append(sentence.get(0).getAsString());
            }
        }
        
        return translatedText.toString();
    }
    
    /**
     * Конвертирует коды языков в формат Google
     * Например: "EN-GB" -> "en", "ZH-HANS" -> "zh-CN"
     */
    private static String convertLangCode(String lang) {
        if (lang == null || lang.isEmpty()) {
            return "auto";
        }
        
        // Приводим к нижнему регистру
        String lower = lang.toLowerCase();
        
        // Конвертируем специальные коды
        switch (lower) {
            case "en-gb":
            case "en-us":
                return "en";
            case "zh-hans":
                return "zh-CN";
            case "zh-hant":
                return "zh-TW";
            case "pt-br":
                return "pt";
            case "pt-pt":
                return "pt";
            default:
                return lower;
        }
    }
    
    /**
     * Возвращает название текущего сервиса
     */
    public static String getServiceName(String apiKey) {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return "DeepL";
        }
        return "Google Translate";
    }
    
    /**
     * Тестирует работает ли Google Translate
     */
    public static boolean testGoogleTranslate() {
        try {
            String result = translateWithGoogle("test", "en", "en");
            return result != null && !result.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
