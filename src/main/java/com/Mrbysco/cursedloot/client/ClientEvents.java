package com.mrbysco.cursedloot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.info.CurseLocation;
import com.mrbysco.cursedloot.util.info.CursePos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public class ClientEvents {

    @SubscribeEvent
    public void setToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundNBT tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            CurseHelper.addLore(event.getToolTip(), tag);
        }
    }

    @SubscribeEvent
    public void tooltipEvent(RenderTooltipEvent event) {
//        Minecraft mc = Minecraft.getInstance();
//        if(mc.player == null)
//            return;
//
//        ItemStack stack = event.getStack();
//        FontRenderer fontRenderer = mc.fontRenderer;
//        int fontHeight = fontRenderer.FONT_HEIGHT + 1;
//
//        CompoundNBT tags = stack.hasTag() && stack.getTag() != null ? stack.getTag() : new CompoundNBT();
    }

    @SubscribeEvent
    public void tooltipEvent(RenderTooltipEvent.PostText event) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack stack = event.getStack();
        FontRenderer fontRenderer = mc.fontRenderer;
        int fontHeight = fontRenderer.FONT_HEIGHT + 1;

        CompoundNBT tags = stack.hasTag() && stack.getTag() != null ? stack.getTag() : new CompoundNBT();

        if(!tags.isEmpty() && CurseHelper.hasCurse(tags)) {
            RenderSystem.enableBlend();

            CurseLocation curseTextureInfo = CurseHelper.getIconLocation(tags);
            if(curseTextureInfo != null) {
                ResourceLocation icon = curseTextureInfo.getResource();
                CursePos texturePos = curseTextureInfo.getPosition();

                mc.getTextureManager().bindTexture(icon);
                int middle = (fontRenderer.getStringWidth(stack.getDisplayName().getUnformattedComponentText()))/2;

                int posX = event.getX() + middle;
                int posY = event.getY() + 14;

                List<ITextComponent> tooltips = stack.getTooltip((PlayerEntity) null, ITooltipFlag.TooltipFlags.NORMAL);
                if(!tooltips.isEmpty()) {
                    for(int i = 0; i < tooltips.size(); i++) {
                        if(tooltips.get(i).equals(Reference.emptyComponent)) {
                            int location = i - 1;
                            posY = event.getY() + (mc.gameSettings.advancedItemTooltips ? 9 + fontHeight/2 + (fontHeight * 2) : 9 + fontHeight/2) + (fontHeight * location);
                            break;
                        }
                    }

                    GuiUtils.drawTexturedModalRect(posX, posY, texturePos.getPosX(), texturePos.getPosY(), 32, 32, 1);
                }
            }
        }
    }
}
