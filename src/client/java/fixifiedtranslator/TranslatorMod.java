package fixifiedtranslator;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class TranslatorMod implements ClientModInitializer {
	public static Config config;
	
	// Создаём кастомную категорию для KeyBinding через Identifier
	private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(
			Identifier.of("fixifiedtranslator", "chattranslator")
	);
	
	private static final KeyBinding configKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.chattranslator.config",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_KP_ADD,
					CATEGORY
			)
	);

	@Override
	public void onInitializeClient() {
		config = Config.load();
		
		// Устанавливаем язык интерфейса
		applyUILanguage();

		// Регистрируем обработчик сообщений чата
		ChatMessageTranslator.register();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			// Старая команда /translatemessage
			dispatcher.register(
					ClientCommandManager.literal("translatemessage")
							.then(
									ClientCommandManager.argument("message", StringArgumentType.greedyString())
											.executes(context -> {
												String message = StringArgumentType.getString(context, "message");
												FabricClientCommandSource source = context.getSource();
												if (config.preferredLanguage == null || config.preferredLanguage.isEmpty()) {
													source.sendError(Text.translatable("chattranslator.error.preferred_language_not_set"));
													return 0;
												}
												try {
													String sourceLang = config.manualSourceLanguage ? config.sourceLanguage : null;
													String translated = TranslationService.translate(
															message,
															sourceLang,
															config.preferredLanguage,
															config.apiKey,
															config.isPro
													);
													source.sendFeedback(Text.translatable("chattranslator.translation.translated", translated));
												} catch (Exception e) {
													source.sendError(Text.translatable("chattranslator.translation.failed", e.getMessage()));
												}
												return 1;
											})
							)
			);
			
			// Новая команда /trans - перевод И отправка в чат
			dispatcher.register(
					ClientCommandManager.literal("trans")
							.then(
									ClientCommandManager.argument("text", StringArgumentType.greedyString())
											.executes(context -> {
												String text = StringArgumentType.getString(context, "text");
												FabricClientCommandSource source = context.getSource();
												
												String serviceName = TranslationService.getServiceName(config.apiKey);
												
												if (config.preferredLanguage == null || config.preferredLanguage.isEmpty()) {
													source.sendError(Text.literal("§cError: Target language not set!"));
													return 0;
												}
												
												try {
													String sourceLang = config.manualSourceLanguage ? config.sourceLanguage : null;
													String translated = TranslationService.translate(
															text,
															sourceLang,
															config.preferredLanguage,
															config.apiKey,
															config.isPro
													);
													
													// Отправляем перевод В ЧАТ ВСЕМ
													net.minecraft.client.MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(translated);
													
													// Показываем подтверждение игроку
													source.sendFeedback(Text.literal("")
														.append(Text.literal("§7[§b🌐 " + serviceName + "§7] §f" + text + "\n"))
														.append(Text.literal("§7→ §a" + translated))
													);
												} catch (Exception e) {
													source.sendError(Text.literal("§cTranslation failed: " + e.getMessage()));
												}
												return 1;
											})
							)
			);
			
			// Команда /transru - быстрый перевод на русский
			dispatcher.register(
					ClientCommandManager.literal("transru")
							.then(
									ClientCommandManager.argument("text", StringArgumentType.greedyString())
											.executes(context -> {
												String text = StringArgumentType.getString(context, "text");
												FabricClientCommandSource source = context.getSource();
												
												String serviceName = TranslationService.getServiceName(config.apiKey);
												
												try {
													String translated = TranslationService.translate(
															text,
															null,
															"RU",
															config.apiKey,
															config.isPro
													);
													
													source.sendFeedback(Text.literal("")
														.append(Text.literal("§7[§b🇷🇺 " + serviceName + "§7]§r\n"))
														.append(Text.literal("§eОригинал: §f" + text + "\n"))
														.append(Text.literal("§eПеревод: §a" + translated))
													);
												} catch (Exception e) {
													source.sendError(Text.literal("§cОшибка перевода: " + e.getMessage()));
												}
												return 1;
											})
							)
			);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (configKey.wasPressed()) {
				client.setScreen(new ConfigScreen());
			}
		});
	}
	
	/**
	 * Применяет язык интерфейса
	 */
	private void applyUILanguage() {
		if (config.uiLanguage == null || config.uiLanguage.isEmpty()) {
			config.uiLanguage = "EN";
		}
		// Minecraft автоматически подхватит язык из config/uiLanguage
		// Для принудительной смены нужно перезагрузить ресурсы
	}
	
	/**
	 * Перезагружает язык интерфейса
	 */
	public static void reloadUI() {
		if (config != null && config.uiLanguage != null) {
			// Переключаем язык Minecraft
			var client = net.minecraft.client.MinecraftClient.getInstance();
			if (client != null && client.options != null) {
				// Сохраняем настройки
				client.options.write();
			}
		}
	}
}
