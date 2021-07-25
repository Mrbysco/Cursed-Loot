package com.mrbysco.cursedloot.util;

import com.mrbysco.cursedloot.CursedLoot;
import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import com.mrbysco.cursedloot.init.CursedWorldData;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;

public class InvHelper {

	@Nullable
	public static BaseChestInventory getChestInventory(Player player, Level worldIn) {
		if(worldIn.isClientSide || player instanceof FakePlayer) {
			return null;
		} else {
			if(player.getTeam() != null) {
				return CursedWorldData.get(worldIn).getInventoryFromTeam(player.getTeam().getName());
			} else {
				return CursedWorldData.get(worldIn).getInventoryFromUUID(player.getUUID());
			}
		}
	}

	public static boolean isSide(int inventorySize, int slot) {
		int rows = getRowCount(inventorySize);
		return checkLeft(rows, slot) || checkRight(rows, slot);
	}

	public static boolean checkLeft(int rows, int slot) {
		boolean isLeft = false;
		for(int i = 0; i < rows; i++) {
			int x = i*9;
			if(x == slot) {
				isLeft = true;
				break;
			}
		}
		return isLeft;
	}

	public static boolean checkRight(int rows, int slot) {
		boolean isRight = false;
		for(int i = 0; i < rows; i++) {
			int x = (i*9)+8;
			if(x == slot) {
				isRight = true;
				break;
			}
		}
		return isRight;
	}

	public static int getRowCount(int maxSize) {
		return maxSize++/9;
	}

	public static boolean isTop(int slot) {
		return slot >= 0 && slot <= 17;
	}

	public static int getFirstEmptyTopStack(NonNullList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); ++i) {
            if (((ItemStack)inventory.get(i)).isEmpty() && !isTop(i)) {
                return i;
            }
        }

        return -1;
    }
	
	public static int getFirstEmptySideStack(NonNullList<ItemStack> inventory) {
		for (int i = 0; i < inventory.size(); ++i) {
			if (((ItemStack)inventory.get(i)).isEmpty() && !isSide(inventory.size(), i)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static int getDirectionalSlotNumber(ItemStack currentStack, int currentSlot) {
		CompoundTag tag = currentStack.getTag();
		if(tag != null) {
			if(tag.getBoolean(Reference.PREFIX + "north")) {
				if(currentSlot >= 9 && currentSlot <= 17) {
					return -1;
				} else {
					if(currentSlot <= 8) {
						return currentSlot + 27;
					} else {
						return currentSlot - 9;
					}
				}
			} else if(tag.getBoolean(Reference.PREFIX + "northeast")) {
				if(currentSlot >= 9 && currentSlot <= 17 && currentSlot != 17) {
					return -1;
				} else {
					if(currentSlot <= 8) {
						return currentSlot + 28;
					} else {
						return currentSlot - 8;
					}
				}
			} else if(tag.getBoolean(Reference.PREFIX + "east")) {
				if(currentSlot != 8 && currentSlot != 17 && currentSlot != 26 && currentSlot != 35) {
					return currentSlot + 1;
				} else {
					return -1;
				}
			} else if(tag.getBoolean(Reference.PREFIX + "southeast")) {
				if(currentSlot <= 8) {
					return -1;
				} else {
					if(currentSlot <= 35 && currentSlot >= 28) {
						return currentSlot - 26;
					} else {
						return currentSlot + 10;
					}
				}
			} else if(tag.getBoolean(Reference.PREFIX + "south")) {
				if(currentSlot <= 8) {
					return -1;
				} else {
					if(currentSlot >= 27 && currentSlot <= 35) {
						return currentSlot - 27;
					} else {
						return currentSlot + 9;
					}
				}
			} else if(tag.getBoolean(Reference.PREFIX + "southwest")) {
				if(currentSlot <= 8) {
					return -1;
				} else {
					if(currentSlot <= 35 && currentSlot >= 28) {
						return currentSlot - 28;
					} else {
						return currentSlot + 8;
					}
				}
			} else if(tag.getBoolean(Reference.PREFIX + "west")) {
				if(currentSlot != 0 && currentSlot != 9 && currentSlot != 18 && currentSlot != 27) {
					return currentSlot - 1;
				} else {
					return -1;
				}
			} else if(tag.getBoolean(Reference.PREFIX + "northwest")) {
				if(currentSlot >= 9 && currentSlot <= 17 && currentSlot != 9) {
					return -1;
				} else {
					if(currentSlot <= 8) {
						return currentSlot + 26;
					} else {
						return currentSlot - 10;
					}
				}
			}
		}
		CursedLoot.logger.debug("Error finding directional slot for " + currentSlot);
		return -1;
	}
	
	public static void loadItem(CompoundTag tag, NonNullList<ItemStack> list) {
        ListTag nbttaglist = tag.getList(CurseTags.HIDDEN_TAG, 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundTag tagCompound = nbttaglist.getCompound(i);
            int j = tagCompound.getByte("Slot") & 255;

            if (j < list.size()) {
                list.set(j, ItemStack.of(tagCompound));
            }
        }
    }
}
