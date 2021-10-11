package com.mrbysco.cursedloot.blocks.inventory;

import com.mrbysco.cursedloot.init.CursedWorldData;
import com.mrbysco.cursedloot.tileentity.BaseChestTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class BaseChestInventory extends Inventory {
	private BaseChestTile associatedChest;

	public BaseChestInventory(int slots) {
		super(slots);
	}

	public void setChestTileEntity(BaseChestTile chestTileEntity) {
		this.associatedChest = chestTileEntity;
	}

	public void fromTag(ListNBT p_70486_1_) {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			this.setItem(i, ItemStack.EMPTY);
		}

		for(int k = 0; k < p_70486_1_.size(); ++k) {
			CompoundNBT compoundnbt = p_70486_1_.getCompound(k);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.getContainerSize()) {
				this.setItem(j, ItemStack.of(compoundnbt));
			}
		}

	}

	public ListNBT createTag() {
		ListNBT listnbt = new ListNBT();

		for(int i = 0; i < this.getContainerSize(); ++i) {
			ItemStack itemstack = this.getItem(i);
			if (!itemstack.isEmpty()) {
				CompoundNBT compoundnbt = new CompoundNBT();
				compoundnbt.putByte("Slot", (byte)i);
				itemstack.save(compoundnbt);
				listnbt.add(compoundnbt);
			}
		}

		return listnbt;
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean stillValid(PlayerEntity player) {
		return (this.associatedChest == null || this.associatedChest.canBeUsed(player)) && super.stillValid(player);
	}

	public void startOpen(PlayerEntity player) {
		if (this.associatedChest != null) {
			this.associatedChest.openChest();
		}

		super.startOpen(player);
	}

	public void stopOpen(PlayerEntity player) {
		if (this.associatedChest != null) {
			this.associatedChest.closeChest();
		}


		super.stopOpen(player);
		if(!player.level.isClientSide) {
			CursedWorldData.get(player.level).setDirty();
		}
		this.associatedChest = null;
	}

	public boolean addItemStackToInventory(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		int slot = getFirstEmptyStack();
		if(slot >= 0) {
			setItem(slot, stack.copy());
			stack.setCount(0);
			return true;
		} else {
			return false;
		}
	}

	public int getFirstEmptyStack() {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			if (this.getItem(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}
}
