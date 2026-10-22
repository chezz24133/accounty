package com.accounty.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.session.Session;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderAccountStatus(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Session session = client.getSession();

        if (session == null) return;

        // Verify if account is MSA/Mojang authenticated (Premium) vs Offline/Legacy
        Session.AccountType type = session.getAccountType();
        boolean isPremium = (type == Session.AccountType.MOJANG || type == Session.AccountType.MSA);

        String username = session.getUsername();
        Text statusText;
        if (isPremium) {
            statusText = Text.literal("Account: " + username + " [")
                    .append(Text.literal("PREMIUM").formatted(Formatting.GREEN, Formatting.BOLD))
                    .append("]");
        } else {
            statusText = Text.literal("Account: " + username + " [")
                    .append(Text.literal("OFFLINE").formatted(Formatting.RED, Formatting.BOLD))
                    .append("]");
        }

        TextRenderer font = client.textRenderer;

        // Positioned at the bottom-left, right above the vanilla Minecraft version string
        int x = 2;
        int y = ((TitleScreen)(Object)this).height - 22;

        context.drawTextWithShadow(font, statusText, x, y, 0xFFFFFFFF);
    }
}
