package com.mrbysco.cursedloot.blocks;

import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.util.InvHelper;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class BaseChestBlock extends AbstractChestBlock<BaseChestBlockEntity> implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
	private final Supplier<BlockEntityType<? extends BaseChestBlockEntity>> tileEntityTypeSupplier = CursedRegistry.BASE_CHEST_BLOCK_ENTITY::get;

	public BaseChestBlock(BlockBehaviour.Properties builder) {
		super(builder, CursedRegistry.BASE_CHEST_BLOCK_ENTITY::get);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	public DoubleBlockCombiner.NeighborCombineResult<? extends BaseChestBlockEntity> getWrapper(BlockState state, Level world, BlockPos pos, boolean override) {
		BiPredicate<LevelAccessor, BlockPos> biPredicate;
		if (override) {
			biPredicate = (p_226918_0_, p_226918_1_) -> false;
		} else {
			biPredicate = BaseChestBlock::isBlocked;
		}

		return DoubleBlockCombiner.combineWithNeigbour(tileEntityTypeSupplier.get(), BaseChestBlock::getMergerType, BaseChestBlock::getDirectionToAttached, FACING, state, world, pos, biPredicate);
	}

	public static DoubleBlockCombiner.BlockType getMergerType(BlockState blockState) {
		return DoubleBlockCombiner.BlockType.SINGLE;
	}

	public static Direction getDirectionToAttached(BlockState state) {
		Direction direction = state.getValue(FACING);
		return direction.getCounterClockWise();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BaseChestBlockEntity(pos, state);
	}

	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		BaseChestInventory baseChestInventory = InvHelper.getChestInventory(player, worldIn);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (baseChestInventory != null && blockEntity instanceof BaseChestBlockEntity) {
			BlockPos blockpos = pos.above();
			if (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos)) {
				return InteractionResult.sidedSuccess(worldIn.isClientSide);
			} else if (worldIn.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				BaseChestBlockEntity baseChestBlockEntityEntity = (BaseChestBlockEntity) blockEntity;
				baseChestInventory.setChestBlockEntity(baseChestBlockEntityEntity);
				if (baseChestInventory.getContainerSize() == 27) {
					player.openMenu(new SimpleMenuProvider((id, inventory, playerIn) -> ChestMenu.threeRows(id, inventory, baseChestInventory),
							baseChestBlockEntityEntity.getDefaultName()));
				} else if (baseChestInventory.getContainerSize() == 54) {
					player.openMenu(new SimpleMenuProvider((id, inventory, playerIn) -> ChestMenu.sixRows(id, inventory, baseChestInventory),
							baseChestBlockEntityEntity.getDefaultName()));
				}
				PiglinAi.angerNearbyPiglins(player, true);
				return InteractionResult.CONSUME;
			}
		} else {
			return InteractionResult.sidedSuccess(worldIn.isClientSide);
		}
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@OnlyIn(Dist.CLIENT)
	public static DoubleBlockCombiner.Combiner<BaseChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity lid) {
		return new DoubleBlockCombiner.Combiner<BaseChestBlockEntity, Float2FloatFunction>() {
			public Float2FloatFunction acceptDouble(BaseChestBlockEntity blockEntity, BaseChestBlockEntity blockEntity1) {
				return (angle) -> Math.max(blockEntity.getOpenNess(angle), blockEntity1.getOpenNess(angle));
			}

			public Float2FloatFunction acceptSingle(BaseChestBlockEntity blockEntity) {
				return blockEntity::getOpenNess;
			}

			public Float2FloatFunction acceptNone() {
				return lid::getOpenNess;
			}
		};
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	public static boolean isBlocked(LevelAccessor world, BlockPos pos) {
		return isBelowSolidBlock(world, pos) || isCatSittingOn(world, pos);
	}

	private static boolean isBelowSolidBlock(BlockGetter reader, BlockPos worldIn) {
		BlockPos blockpos = worldIn.above();
		return reader.getBlockState(blockpos).isRedstoneConductor(reader, blockpos);
	}

	private static boolean isCatSittingOn(LevelAccessor world, BlockPos pos) {
		List<Cat> list = world.getEntitiesOfClass(Cat.class, new AABB((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)));
		if (!list.isEmpty()) {
			for (Cat catentity : list) {
				if (catentity.isInSittingPose()) {
					return true;
				}
			}
		}

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level world, BlockPos pos, boolean override) {
		return DoubleBlockCombiner.Combiner::acceptNone;
	}

	public BlockEntityType<? extends BaseChestBlockEntity> blockEntityType() {
		return this.blockEntityType.get();
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153055_, BlockState state, BlockEntityType<T> blockEntityType) {
		return p_153055_.isClientSide ? createTickerHelper(blockEntityType, this.blockEntityType(), BaseChestBlockEntity::lidAnimateTick) : null;
	}
}