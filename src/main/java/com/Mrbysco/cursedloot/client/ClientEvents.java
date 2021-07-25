package com.mrbysco.cursedloot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.info.CurseLocation;
import com.mrbysco.cursedloot.util.info.CursePos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag.Default;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.util.List;

public class ClientEvents {

    @SubscribeEvent
    public void setToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            CurseHelper.addLore(event.getToolTip(), tag);
        }
    }

    @SubscribeEvent
    public void tooltipEvent(RenderTooltipEvent.PostText event) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getMatrixStack();
        if(mc.player == null)
            return;

        ItemStack stack = event.getStack();
        CompoundTag tags = stack.hasTag() && stack.getTag() != null ? stack.getTag() : new CompoundTag();

        if(!tags.isEmpty() && CurseHelper.hasCurse(tags)) {
            Font fontRenderer = mc.font;
            int fontHeight = fontRenderer.lineHeight + 1;

            RenderSystem.enableBlend();

            CurseLocation curseTextureInfo = CurseHelper.getIconLocation(tags);
            if(curseTextureInfo != null) {
                ResourceLocation icon = curseTextureInfo.getResource();
                CursePos texturePos = curseTextureInfo.getPosition();

                mc.getTextureManager().bindForSetup(icon);
                int middle = (fontRenderer.width(stack.getHoverName().getContents()))/2;

                int posX = event.getX() + middle;
                int posY = event.getY() + 14;

                List<Component> tooltips = stack.getTooltipLines((Player) null, Default.ADVANCED);
                if(!tooltips.isEmpty()) {
                    for(int i = 0; i < tooltips.size(); i++) {
                        Component component = tooltips.get(i);
                        if(component.equals(Reference.emptyComponent)) {
                            int location = i - 1;
                            posY = event.getY() + (fontHeight * location);
                            if(mc.options.advancedItemTooltips) {
                                posY += (int)(fontHeight * 1.5);
                            } else {
                                posY -= 4;
                            }
                            if(event.getWidth() < 200) {
                                posY -= fontHeight;
                            }
                            break;
                        }
                    }

                    GuiUtils.drawTexturedModalRect(poseStack, posX, posY, texturePos.getPosX(), texturePos.getPosY(), 32, 32, 1);
                }
            }
        }
    }
}
