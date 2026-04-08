package fixifiedtranslator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    
    private TextFieldWidget apiKeyField;
    private ButtonWidget proButton;
    private ButtonWidget testButton;
    private TextFieldWidget resultField;
    private String actualApiKey;
    private CyclingButtonWidget<String> chatTargetLanguageButton;
    private CyclingButtonWidget<String> sourceLanguageButton;
    
    // Элементы ручного перевода
    private TextFieldWidget translateInputField;
    private ButtonWidget translateButton;
    private TextFieldWidget translateResultField;
    
    // Вкладки
    private ButtonWidget tab1Button;
    private ButtonWidget tab2Button;
    private int currentTab = 0;
    
    // Кнопка языка интерфейса (правый нижний угол)
    private ButtonWidget uiLanguageButton;
    
    private static final List<String> LANGUAGES = Arrays.asList(
            "AR", "BG", "CS", "DA", "DE", "EL", "EN", "ES", "FI", "FR",
            "HU", "ID", "IT", "JA", "KO", "NL", "PL", "PT", "RO", "RU",
            "SK", "SV", "TR", "UK", "ZH"
    );
    
    private static final List<String> UI_LANGUAGES = Arrays.asList("EN", "RU", "FR", "IT", "ES", "DE");
    
    public ConfigScreen() {
        super(Text.translatable("fixifiedtranslator.config.title"));
    }
    
    @Override
    protected void init() {
        createTabs();
        createCurrentTabWidgets();
        createUILanguageButton();
    }
    
    private void createUILanguageButton() {
        // Кнопка в правом нижнем углу
        this.uiLanguageButton = ButtonWidget.builder(
                getUILanguageText(),
                button -> {
                    // Циклическое переключение языков
                    int currentIndex = UI_LANGUAGES.indexOf(TranslatorMod.config.uiLanguage);
                    int nextIndex = (currentIndex + 1) % UI_LANGUAGES.size();
                    TranslatorMod.config.uiLanguage = UI_LANGUAGES.get(nextIndex);
                    TranslatorMod.config.save();
                    
                    // Обновляем текст кнопки
                    button.setMessage(getUILanguageText());
                    
                    // Перезагружаем экран для применения языка
                    this.client.setScreen(new ConfigScreen());
                })
            .dimensions(this.width - 75, this.height - 25, 65, 20)
            .build();
        this.addDrawableChild(this.uiLanguageButton);
    }
    
    private Text getUILanguageText() {
        String lang = TranslatorMod.config.uiLanguage;
        String flag = switch (lang) {
            case "RU" -> "🇷🇺";
            case "FR" -> "🇫🇷";
            case "IT" -> "🇮🇹";
            case "ES" -> "🇪🇸";
            case "DE" -> "🇩🇪";
            default -> "🇬🇧";
        };
        return Text.literal(flag + " " + lang);
    }
    
    private void createTabs() {
        int tabY = 10;
        
        // Вкладка 1: Автоперевод чата
        this.tab1Button = ButtonWidget.builder(
                Text.literal("💬 Chat"),
                button -> {
                    this.currentTab = 0;
                    this.clearAndInit();
                })
            .dimensions(10, tabY, 60, 20)
            .build();
        this.addDrawableChild(this.tab1Button);
        
        // Вкладка 2: Ручной перевод
        this.tab2Button = ButtonWidget.builder(
                Text.literal("🔤 /trans"),
                button -> {
                    this.currentTab = 1;
                    this.clearAndInit();
                })
            .dimensions(75, tabY, 60, 20)
            .build();
        this.addDrawableChild(this.tab2Button);
        
        // Подсвечиваем активную
        if (currentTab == 0) {
            this.tab1Button.active = false;
            this.tab2Button.active = true;
        } else {
            this.tab1Button.active = true;
            this.tab2Button.active = false;
        }
    }
    
    private void createCurrentTabWidgets() {
        if (currentTab == 0) {
            createChatTab();
        } else {
            createTransTab();
        }
    }
    
    /**
     * Вкладка 1: Настройки автоперевода чата
     */
    private void createChatTab() {
        int centerX = this.width / 2 - 100;
        int y = 45;
        
        // Показываем какой сервис используется
        String serviceName = TranslationService.getServiceName(TranslatorMod.config.apiKey);
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Service: §b§l" + serviceName + (serviceName.equals("Google Translate") ? " §e(Free)" : "")),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 15;
        
        // API Key (опционально)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7DeepL API Key (§eoptional§7):"),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 12;
        
        this.apiKeyField = new TextFieldWidget(this.textRenderer, centerX, y, 200, 20, Text.literal("Leave empty for Google Translate (Free)"));
        this.actualApiKey = TranslatorMod.config.apiKey;
        this.apiKeyField.setText(this.actualApiKey.isEmpty() ? "" : "******");
        this.apiKeyField.setMaxLength(100);
        this.apiKeyField.setChangedListener(text -> this.actualApiKey = text.equals("******") ? this.actualApiKey : text);
        this.addDrawableChild(this.apiKeyField);
        y += 25;
        
        // Pro/Free
        this.proButton = ButtonWidget.builder(
                Text.literal(TranslatorMod.config.isPro ? "§b§lDeepL Pro" : "§7§lDeepL Free"),
                button -> {
                    TranslatorMod.config.isPro = !TranslatorMod.config.isPro;
                    button.setMessage(Text.literal(TranslatorMod.config.isPro ? "§b§lDeepL Pro" : "§7§lDeepL Free"));
                })
            .dimensions(centerX, y, 95, 20)
            .build();
        this.addDrawableChild(this.proButton);
        
        // Test
        this.testButton = ButtonWidget.builder(
                Text.literal("§aTest"),
                button -> {
                    try {
                        TranslationService.translate("test", null, "EN", this.actualApiKey.trim(), TranslatorMod.config.isPro);
                        this.resultField.setText("§a✓ " + serviceName + " OK");
                    } catch (Exception e) {
                        this.resultField.setText("§c✗ Error");
                    }
                })
            .dimensions(centerX + 105, y, 95, 20)
            .build();
        this.addDrawableChild(this.testButton);
        y += 25;
        
        // Result
        this.resultField = new TextFieldWidget(this.textRenderer, centerX, y, 200, 16, Text.literal(""));
        this.resultField.setEditable(false);
        this.resultField.setMaxLength(100);
        this.addDrawableChild(this.resultField);
        y += 30;
        
        // Target Language
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Target Language:"),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 12;
        
        this.chatTargetLanguageButton = CyclingButtonWidget.<String>builder(
                code -> Text.literal(code),
                (java.util.function.Supplier<String>) () -> TranslatorMod.config.preferredLanguage
            )
            .values(LANGUAGES)
            .build(centerX, y, 200, 20, Text.literal(""),
                (button, value) -> TranslatorMod.config.preferredLanguage = value
            );
        this.addDrawableChild(this.chatTargetLanguageButton);
        y += 35;
        
        // Source Language
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Source (Auto = detect):"),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 12;
        
        this.sourceLanguageButton = CyclingButtonWidget.<String>builder(
                code -> Text.literal(code),
                (java.util.function.Supplier<String>) () -> TranslatorMod.config.sourceLanguage
            )
            .values(LANGUAGES)
            .build(centerX, y, 200, 20, Text.literal(""),
                (button, value) -> {
                    TranslatorMod.config.sourceLanguage = value;
                    TranslatorMod.config.manualSourceLanguage = true;
                });
        
        if (!TranslatorMod.config.manualSourceLanguage) {
            this.sourceLanguageButton.active = false;
        }
        this.addDrawableChild(this.sourceLanguageButton);
        y += 28;
        
        // Toggle Auto/Manual source
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(TranslatorMod.config.manualSourceLanguage ? "§e§lManual" : "§b§lAuto Detect"),
                button -> {
                    TranslatorMod.config.manualSourceLanguage = !TranslatorMod.config.manualSourceLanguage;
                    button.setMessage(Text.literal(TranslatorMod.config.manualSourceLanguage ? "§e§lManual" : "§b§lAuto Detect"));
                    this.sourceLanguageButton.active = TranslatorMod.config.manualSourceLanguage;
                })
            .dimensions(centerX, y, 200, 20)
            .build()
        );
        y += 35;
        
        // Save
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§a§l💾 Save & Close"),
                button -> {
                    TranslatorMod.config.apiKey = this.actualApiKey.trim();
                    TranslatorMod.config.save();
                    TranslatorMod.reloadUI();
                    this.client.setScreen(null);
                })
            .dimensions(centerX, y, 200, 20)
            .build()
        );
    }
    
    /**
     * Вкладка 2: Ручной перевод (/trans)
     */
    private void createTransTab() {
        int centerX = this.width / 2 - 100;
        int y = 45;
        
        // Описание
        String serviceName = TranslationService.getServiceName(TranslatorMod.config.apiKey);
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Using: §b" + serviceName),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 15;
        
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7Type §e/trans <text>§7 in chat"),
                button -> {})
            .dimensions(centerX, y, 200, 10)
            .build()
        );
        y += 25;
        
        // Input
        this.translateInputField = new TextFieldWidget(this.textRenderer, centerX, y, 200, 60, Text.literal("Enter text..."));
        this.translateInputField.setMaxLength(1000);
        this.translateInputField.setDrawsBackground(true);
        this.addDrawableChild(this.translateInputField);
        y += 68;
        
        // Translate button
        this.translateButton = ButtonWidget.builder(
                Text.literal("§b§l🌐 Translate"),
                button -> {
                    String text = this.translateInputField.getText().trim();
                    if (text.isEmpty()) {
                        this.translateResultField.setText("§cEnter text!");
                        return;
                    }
                    
                    try {
                        String sourceLang = TranslatorMod.config.manualSourceLanguage ? 
                            TranslatorMod.config.sourceLanguage : null;
                        String translated = TranslationService.translate(
                            text,
                            sourceLang,
                            TranslatorMod.config.preferredLanguage,
                            TranslatorMod.config.apiKey,
                            TranslatorMod.config.isPro
                        );
                        this.translateResultField.setText("§a" + translated);
                    } catch (Exception e) {
                        this.translateResultField.setText("§c" + e.getMessage());
                    }
                })
            .dimensions(centerX, y, 200, 20)
            .build();
        this.addDrawableChild(this.translateButton);
        y += 28;
        
        // Result
        this.translateResultField = new TextFieldWidget(this.textRenderer, centerX, y, 200, 80, Text.literal(""));
        this.translateResultField.setEditable(false);
        this.translateResultField.setMaxLength(5000);
        this.translateResultField.setDrawsBackground(true);
        this.addDrawableChild(this.translateResultField);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Заголовок
        context.drawCenteredTextWithShadow(this.textRenderer, 
            "§l§bFixifiedTranslator §fv1.3.0", 
            this.width / 2, 12, 0x00FFFF);
        
        // Подсказка про язык
        context.drawTextWithShadow(this.textRenderer, 
            "§7Click to change UI language →", 
            this.width - 250, this.height - 21, 0x888888);
    }
}
