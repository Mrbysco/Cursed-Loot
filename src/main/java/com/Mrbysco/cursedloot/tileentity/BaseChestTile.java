package com.mrbysco.cursedloot.tileentity;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import com.mrbysco.cursedloot.util.InvHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = IChestLid.class
)
public class BaseChestTile extends TileEntity implements IChestLid, ITickableTileEntity {
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    /**
     * A counter that is incremented once each tick. Used to determine when to recompute ; this is done every 200 ticks
     * (but staggered between different chests). However, the new value isn't actually sent to clients when it is
     * changed.
     */
    private int ticksSinceSync;

    public BaseChestTile() {
        super(CursedRegistry.BASE_CHEST_TILE.get());
    }

    public ITextComponent getDefaultName() {
        return new TranslationTextComponent(Reference.MOD_ID + ":container.base_chest");
    }

    public void tick() {
        ++this.ticksSinceSync;
        this.prevLidAngle = this.lidAngle;
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        float f = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            this.world.playSound((PlayerEntity)null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += f;
            } else {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f1 = 0.5F;
            if (this.lidAngle < f1 && f2 >= f1) {
                double d3 = (double)i + 0.5D;
                double d2 = (double)k + 0.5D;
                this.world.playSound((PlayerEntity)null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
        checkForPlayer();
    }

    public void checkForPlayer() {
        if(!world.isRemote && world.getGameTime() % 20 == 0) {
            List<PlayerEntity> players = new ArrayList<>(world.getPlayers());
            players.removeIf((playerEntity -> getPos().distanceSq(playerEntity.getPosition()) > 100));
            for(PlayerEntity player : players) {
                PlayerInventory inv = player.inventory;
                for(int i = 0; i < inv.mainInventory.size(); i++) {
                    if(!inv.getStackInSlot(i).isEmpty()) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if(stack.hasTag() && stack.getTag() != null) {
                            CompoundNBT tag = stack.getTag();
                            if(tag.getBoolean(CurseTags.REMAIN_HIDDEN.getCurseTag())) {
                                if(tag.contains(CurseTags.HIDDEN_TAG)) {
                                    List<ItemStack> revealedStacks = CurseHelper.revealStacks(stack, tag);
                                    if (!revealedStacks.isEmpty()) {
                                        for (int s = 0; s < revealedStacks.size(); s++) {
                                            if (s == 0) {
                                                inv.setInventorySlotContents(i, revealedStacks.get(s));
                                            } else {
                                                if (!inv.addItemStackToInventory(revealedStacks.get(s))) {
                                                    player.dropItem(revealedStacks.get(s), false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(tag.getBoolean(CurseTags.DESTROY_ITEM.getCurseTag())) {
                                int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);
                                if(directionalSlot != -1) {
                                    inv.setInventorySlotContents(directionalSlot, ItemStack.EMPTY);
                                }

                                ItemStack stack2 = stack.copy();
                                CompoundNBT tag2 = CurseHelper.removeCurse(stack2.getTag());
                                stack2.setTag(tag2);

                                inv.setInventorySlotContents(i, stack2);
                            }
                            if(tag.getBoolean(CurseTags.ITEM_BECOMES_THIS.getCurseTag())) {
                                int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);

                                ItemStack stack2 = stack.copy();
                                CompoundNBT tag2 = CurseHelper.removeCurse(stack2.getTag());
                                stack2.setTag(tag2);
                                if(directionalSlot != -1) {
                                    ItemStack stack3 = stack2.copy();
                                    if(stack2.getCount() > 4) {
                                        int newCount = world.rand.nextInt(4);
                                        if(newCount == 0) {
                                            newCount = 1;
                                        }
                                        stack3.setCount(newCount);
                                    }
                                    inv.setInventorySlotContents(directionalSlot, stack3);
                                }
                                inv.setInventorySlotContents(i, stack2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    public void remove() {
        this.updateContainingBlockInfo();
        super.remove();
    }

    public void openChest() {
        ++this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, CursedRegistry.BASE_CHEST.get(), 1, this.numPlayersUsing);
    }

    public void closeChest() {
        --this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, CursedRegistry.BASE_CHEST.get(), 1, this.numPlayersUsing);
    }

    public boolean canBeUsed(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}