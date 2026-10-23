package com.accounty.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderAccountStatus(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        User session = client.getUser();

        if (session == null) return;

        User.Type type = session.getType();
        boolean isPremium = (type == User.Type.MOJANG || type == User.Type.MSA);

        String username = session.getName();
        Component statusText;
        if (isPremium) {
            statusText = Component.literal("Account: " + username + " [")
                    .append(Component.literal("PREMIUM").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD))
                    .append("]");
        } else {
            statusText = Component.literal("Account: " + username + " [")
                    .append(Component.literal("OFFLINE").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                    .append("]");
        }

        Font font = client.font;
        int x = 2;
        int y = ((TitleScreen)(Object)this).height - 22;

        graphics.drawString(font, statusText, x, y, 0xFFFFFFFF, true);
    }
}
