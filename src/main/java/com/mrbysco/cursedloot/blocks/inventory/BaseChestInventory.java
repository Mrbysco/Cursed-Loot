package com.mrbysco.cursedloot.blocks.inventory;

import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.init.CursedWorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BaseChestInventory extends SimpleContainer {
	private BaseChestBlockEntity associatedChest;

	public BaseChestInventory(int slots) {
		super(slots);
	}

	public void setChestBlockEntity(BaseChestBlockEntity chestBlockEntity) {
		this.associatedChest = chestBlockEntity;
	}

	public void fromTag(ListTag listTag) {
		for(int i = 0; i < this.getContainerSize(); ++i) {
			this.setItem(i, ItemStack.EMPTY);
		}

		for(int k = 0; k < listTag.size(); ++k) {
			CompoundTag compoundTag = listTag.getCompound(k);
			int j = compoundTag.getByte("Slot") & 255;
			if (j >= 0 && j < this.getContainerSize()) {
				this.setItem(j, ItemStack.of(compoundTag));
			}
		}

	}

	public ListTag createTag() {
		ListTag listTag = new ListTag();

		for(int i = 0; i < this.getContainerSize(); ++i) {
			ItemStack itemstack = this.getItem(i);
			if (!itemstack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte)i);
				itemstack.save(compoundTag);
				listTag.add(compoundTag);
			}
		}

		return listTag;
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean stillValid(Player player) {
		return (this.associatedChest == null || this.associatedChest.canBeUsed(player)) && super.stillValid(player);
	}

	public void startOpen(Player player) {
		if (this.associatedChest != null) {
			this.associatedChest.startOpen(player);
		}

		super.startOpen(player);
	}

	public void stopOpen(Player player) {
		if (this.associatedChest != null) {
			this.associatedChest.stopOpen(player);
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
