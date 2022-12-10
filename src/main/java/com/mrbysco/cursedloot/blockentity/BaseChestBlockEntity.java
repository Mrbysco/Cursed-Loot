package com.mrbysco.cursedloot.blockentity;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaseChestBlockEntity extends BlockEntity implements LidBlockEntity {
	private static final int EVENT_SET_OPEN_COUNT = 1;

	private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			BaseChestBlockEntity.playSound(level, pos, SoundEvents.CHEST_OPEN);
		}

		protected void onClose(Level level, BlockPos pos, BlockState state) {
			BaseChestBlockEntity.playSound(level, pos, SoundEvents.CHEST_CLOSE);
		}

		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155364_, int p_155365_) {
			BaseChestBlockEntity.this.signalOpenCount(level, pos, state, p_155364_, p_155365_);
		}

		protected boolean isOwnContainer(Player player) {
			if (!(player.containerMenu instanceof ChestMenu)) {
				return false;
			} else {
				return true;
//                Container container = ((ChestMenu)player.containerMenu).getContainer();
//                return container == BaseChestBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(BaseChestBlockEntity.this);
			}
		}
	};
	private final ChestLidController chestLidController = new ChestLidController();

	public BaseChestBlockEntity(BlockPos pos, BlockState state) {
		this(CursedRegistry.BASE_CHEST_BLOCK_ENTITY.get(), pos, state);
	}

	public BaseChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public Component getDefaultName() {
		return Component.translatable(Reference.MOD_PREFIX + "container.base_chest");
	}

	public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, BaseChestBlockEntity blockEntity) {
		blockEntity.chestLidController.tickLid();
	}

	static void playSound(Level level, BlockPos pos, SoundEvent event) {
		double x = (double) pos.getX() + 0.5D;
		double y = (double) pos.getY() + 0.5D;
		double z = (double) pos.getZ() + 0.5D;
		level.playSound((Player) null, x, y, z, event, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}

	public boolean triggerEvent(int id, int type) {
		if (id == 1) {
			this.chestLidController.shouldBeOpen(type > 0);
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}

	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public float getOpenNess(float p_59080_) {
		return this.chestLidController.getOpenness(p_59080_);
	}

	public static int getOpenCount(BlockGetter getter, BlockPos pos) {
		BlockState blockstate = getter.getBlockState(pos);
		if (blockstate.hasBlockEntity()) {
			BlockEntity blockentity = getter.getBlockEntity(pos);
			if (blockentity instanceof BaseChestBlockEntity) {
				return ((BaseChestBlockEntity) blockentity).openersCounter.getOpenerCount();
			}
		}

		return 0;
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int p_155336_, int p_155337_) {
		Block block = state.getBlock();
		level.blockEvent(pos, block, 1, p_155337_);
	}

	public boolean canBeUsed(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}
}