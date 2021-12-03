package com.mrbysco.cursedloot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.info.CurseLocation;
import com.mrbysco.cursedloot.util.info.CursePos;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    @SubscribeEvent
    public void setToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tag = stack.getTag();
        if (tag != null && !tag.isEmpty()) {
            CurseHelper.addLore(event.getToolTip(), tag);
        }
    }

    record CurseTooltip(ItemStack stack) implements TooltipComponent {

    }

    record CurseClientTooltip(CurseTooltip tooltip) implements ClientTooltipComponent {

        @Override
        public int getHeight() {
            return 32;
        }

        @Override
        public int getWidth(Font font) {
            return 32;
        }

        @Override
        public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int zIndex) {
            ItemStack stack = tooltip.stack;
            CompoundTag tags = stack.hasTag() && stack.getTag() != null ? stack.getTag() : new CompoundTag();
            RenderSystem.enableBlend();

            CurseLocation curseTextureInfo = CurseHelper.getIconLocation(tags);
            if(curseTextureInfo != null) {
                ResourceLocation icon = curseTextureInfo.getResource();
                CursePos texturePos = curseTextureInfo.getPosition();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, icon);

//                    int posX = x + 14;
//                    int posY = y + 14;

                GuiUtils.drawTexturedModalRect(poseStack, x, y, texturePos.getPosX(), texturePos.getPosY(), 32, 32, 1);
            }
        }
    }

    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tags = stack.hasTag() && stack.getTag() != null ? stack.getTag() : new CompoundTag();

        if(!tags.isEmpty() && CurseHelper.hasCurse(tags)) {
            event.getTooltipElements().add(Either.right(new CurseTooltip(stack)));
        }
    }
}
