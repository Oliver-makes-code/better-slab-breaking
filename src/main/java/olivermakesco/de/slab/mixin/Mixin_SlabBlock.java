package olivermakesco.de.slab.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import olivermakesco.de.slab.Mod;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(SlabBlock.class)
public class Mixin_SlabBlock extends Block {
	@Shadow
	@Final
	public static EnumProperty<SlabType> TYPE;

	public Mixin_SlabBlock(Settings settings) {
		super(settings);
		throw new IllegalAccessError();
	}

	private static final HashMap<BlockPos, BlockState> statesToSet = new HashMap<>();

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(TYPE) != SlabType.DOUBLE) {
			super.onBreak(world,pos,state,player);
			return;
		}
		var cast = player.raycast(100,0,false);
		var y = cast.getPos().y;
		y %= 1;
		y += 1;
		y %= 1;
		var top = y > 0.5;
		if (top)
			statesToSet.put(pos,state.with(TYPE, SlabType.BOTTOM));
		else statesToSet.put(pos,state.with(TYPE, SlabType.TOP));
		super.onBreak(world, pos, state.with(TYPE, SlabType.BOTTOM), player);
	}

	@Override
	public void onBroken(WorldAccess worldAccess, BlockPos pos, BlockState state) {
		var newState = statesToSet.get(pos);
		if (newState == null) {
			super.onBroken(worldAccess,pos,state);
			return;
		}
		statesToSet.remove(pos);

		if (!(worldAccess instanceof World world)) {
			super.onBroken(worldAccess,pos,state);
			return;
		}

		world.setBlockState(pos, newState);
		super.onBroken(worldAccess, pos, newState);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, state.with(TYPE, SlabType.BOTTOM), blockEntity, stack);
	}
}
