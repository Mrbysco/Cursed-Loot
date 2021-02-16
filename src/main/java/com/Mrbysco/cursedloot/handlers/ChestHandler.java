package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChestHandler {	
	
	public static final String baseChestTag = Reference.PREFIX + "gotBaseChest";
	
	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		
		if(!player.world.isRemote) {
			CompoundNBT playerData = player.getPersistentData();

			if(!playerData.getBoolean(baseChestTag)) {
				player.inventory.addItemStackToInventory(new ItemStack(CursedRegistry.BASE_CHEST.get()));
				playerData.putBoolean(baseChestTag, true);
			}
		}
	}
	
	private static CompoundNBT getTag(CompoundNBT tag, String key) {
		if(tag == null || !tag.contains(key)) {
			return new CompoundNBT();
		}
		return tag.getCompound(key);
	}
	
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
//			if (event.player.world.getWorldTime() % 20 == 0) {
//				EntityPlayer player = event.player;
//				World world = player.world;
//				Chunk playerChunk = world.getChunk(player.getPosition());
//				ArrayList<Chunk> chunks = new ArrayList<>();
//				int radius = 1;
//				for (int x = playerChunk.x - radius; x <= playerChunk.x + radius; x++) {
//					for (int z = playerChunk.z - radius; z <= playerChunk.z + radius; z++) {
//						if(world.isChunkGeneratedAt(x, z))
//							chunks.add(world.getChunk(x, z));
//					}
//				}
//
//				for(Chunk chunk : chunks) {
//					Map<BlockPos, TileEntity> tileMap = chunk.getTileEntityMap();
//					for (Map.Entry<BlockPos, TileEntity> entry : tileMap.entrySet()) {
//						if(entry.getValue() instanceof TileEntityBaseChest) {
//							executeCurses(player);
//						}
//					}
//				}
//			}
		}
	}

	public void executeCurses(PlayerEntity playerIn) {

	}
	
	@SubscribeEvent
	public void breakChest(BlockEvent.BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		if(event.getWorld().getBlockState(event.getPos()).getBlock() == CursedRegistry.BASE_CHEST.get()) {
//			if(!(player instanceof FakePlayer))
//			{
//				NBTTagCompound entityData = player.getEntityData();
//				if(entityData.hasKey(Reference.baseChestLocation))
//				{
//					World world = event.getWorld();
//					BlockPos pos = event.getPos();
//					if(entityData.getLong(Reference.baseChestLocation) == pos.toLong())
//					{
//						entityData.removeTag(Reference.baseChestLocation);
//						if(world.getTileEntity(pos) instanceof TileEntityBaseChest)
//						{
//							TileEntityBaseChest tile = (TileEntityBaseChest) world.getTileEntity(pos);
//							ForgeChunkManager.unforceChunk(tile.getTicket(), world.getChunk(pos).getPos());
//						}
//					}
//					else
//					{
//						player.sendMessage(new TextComponentTranslation("cursedloot:basechest.destroy.cancel"));
//						event.setCanceled(true);
//					}
//				}
//			}
//			else
//			{
//				event.setCanceled(true);
//			}
		}
	}
	
	@SubscribeEvent
	public void placeChest(EntityPlaceEvent event) {
		if(event.getEntity() instanceof PlayerEntity && event.getPlacedBlock().getBlock() == CursedRegistry.BASE_CHEST.get()) {
//			EntityPlayer player = (EntityPlayer)event.getEntity();
//			if(!(player instanceof FakePlayer))
//			{
//				NBTTagCompound entityData = player.getEntityData();
//				if(entityData.hasKey(Reference.baseChestLocation))
//				{
//					World world = event.getWorld();
//					BlockPos pos = event.getPos();
//					BlockPos chestPos = BlockPos.fromLong(entityData.getLong(Reference.baseChestLocation));
//					if(chestPos.equals(pos))
//					{
//						event.setCanceled(true);
//						player.inventory.markDirty();
//						player.sendMessage(new TextComponentTranslation("cursedloot:basechest.already", new Object[] {TextFormatting.RED + chestPos.toString()}));
//					}
//					else
//					{
//						event.setCanceled(false);
//					}
//				}
//			}
//			else
//			{
//				event.setCanceled(true);
//			}
		}
		
	}
}
