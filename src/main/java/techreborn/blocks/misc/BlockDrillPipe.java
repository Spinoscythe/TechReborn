package techreborn.blocks.misc;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import techreborn.init.TRContent;
import techreborn.utils.WorldHelper;

public class BlockDrillPipe extends Block {
	VoxelShape shape;

	public BlockDrillPipe() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.WOOD).strength(0, 0).sounds(BlockSoundGroup.METAL));


		// Pipe shape for outline
		shape = VoxelShapes.cuboid(6 / 16D, 0, 6 / 16D, 10 / 16D, 1, 10 / 16D);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);

		if(world.isClient){
			return;
		}

		// Remove origin block, add it to pipe count
		world.setBlockState(pos,Blocks.AIR.getDefaultState());
		int pipeCount = 1;

		// Now we remove every pipe connected above and below it
		BlockPos next;

		// Above
		next = WorldHelper.getBlockAlongY(pos, 1, TRContent.DRILL_PIPE, world);
		while(next != null){
			world.setBlockState(next, Blocks.AIR.getDefaultState());
			pipeCount++;

			next = WorldHelper.getBlockAlongY(pos, 1, TRContent.DRILL_PIPE, world);
		}

		// Below
		next = WorldHelper.getBlockAlongY(pos, -1, TRContent.DRILL_PIPE, world);
		while(next != null){
			world.setBlockState(next, Blocks.AIR.getDefaultState());
			pipeCount++;

			next = WorldHelper.getBlockAlongY(pos, -1, TRContent.DRILL_PIPE, world);
		}


		ItemStack itemStack = new ItemStack(TRContent.DRILL_PIPE.asItem(), pipeCount);
		if(!player.inventory.insertStack(itemStack)){
			// Inventory's full, drop remaining at player's feet
			ItemEntity itemEntity = new ItemEntity(world,player.getX(), player.getY(), player.getZ(), itemStack);
			world.spawnEntity(itemEntity);
		}
	}


	// Allow users to build it up, why not
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

		if(state.getBlock() == TRContent.DRILL_PIPE){
			ItemStack holding = player.inventory.getStack(player.inventory.selectedSlot);
			if(holding.getItem() == TRContent.DRILL_PIPE.asItem()){
				BlockPos addPos = WorldHelper.getBlockAlongY(pos,1, Blocks.AIR, false, TRContent.DRILL_PIPE, world);
				if(addPos != null){
					world.setBlockState(addPos,TRContent.DRILL_PIPE.getDefaultState());
					holding.decrement(1);
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 100, 1,false);
				}
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}
}
