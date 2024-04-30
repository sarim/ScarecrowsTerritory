package com.supermartijn642.scarecrowsterritory;

import com.mojang.math.Vector3f;
import com.supermartijn642.core.block.BaseBlockEntity;
import com.supermartijn642.core.block.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

/**
 * Created 11/30/2020 by SuperMartijn642
 */
public class ScarecrowBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

    private final ScarecrowType type;

    public ScarecrowBlockEntity(ScarecrowType type, BlockPos pos, BlockState state){
        super(type.blockEntityType, pos, state);
        this.type = type;
    }

    public boolean rightClick(Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(!stack.isEmpty() && stack.getItem() instanceof DyeItem){
            DyeColor color = ((DyeItem)stack.getItem()).getDyeColor();
            BlockState state = this.level.getBlockState(this.worldPosition);
            if(state.getBlock() instanceof ScarecrowBlock){
                this.level.setBlockAndUpdate(this.worldPosition,
                    this.type.blocks.get(color).defaultBlockState()
                        .setValue(HorizontalDirectionalBlock.FACING, state.getValue(HorizontalDirectionalBlock.FACING))
                        .setValue(ScarecrowBlock.BOTTOM, state.getValue(ScarecrowBlock.BOTTOM))
                        .setValue(ScarecrowBlock.WATERLOGGED, state.getValue(ScarecrowBlock.WATERLOGGED))
                );
                // other half
                BlockPos pos2 = state.getValue(ScarecrowBlock.BOTTOM) ? this.worldPosition.above() : this.worldPosition.below();
                BlockState state2 = this.level.getBlockState(pos2);
                if(state2.getBlock() instanceof ScarecrowBlock || state2.isAir() || state2.getFluidState().getType().isSame(Fluids.WATER)){
                    this.level.setBlockAndUpdate(pos2,
                        this.type.blocks.get(color).defaultBlockState()
                            .setValue(HorizontalDirectionalBlock.FACING, state.getValue(HorizontalDirectionalBlock.FACING))
                            .setValue(ScarecrowBlock.BOTTOM, !state.getValue(ScarecrowBlock.BOTTOM))
                            .setValue(ScarecrowBlock.WATERLOGGED, state2.getFluidState().getType().isSame(Fluids.WATER))
                    );
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (level.isClientSide) {
            BlockPos pos = this.worldPosition;
            BlockState state = this.level.getBlockState(pos);
            if (state.getBlock() instanceof ScarecrowBlock && state.getValue(BlockStateProperties.POWERED)) {
                boolean bottom = state.getValue(ScarecrowBlock.BOTTOM);
                double x = pos.getX() + 0.5;
                double y = pos.getY() - 0.9 + (bottom ? 1 : 0);
                double z = pos.getZ() + 0.5;
                    level.addParticle(
                            new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F),
                            x, y, z, 0.0, 0.5,0.0);
            }
        }
    }

    @Override
    protected CompoundTag writeData(){
        return null;
    }

    @Override
    protected void readData(CompoundTag compound){
    }
}
