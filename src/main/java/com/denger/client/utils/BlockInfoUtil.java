package com.denger.client.utils;

import javax.annotation.concurrent.*;

import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import com.google.common.base.*;

import java.awt.*;

@Immutable
public class BlockInfoUtil
{
    private final Color color;
    private final BlockPos pos;
    private final Block block;
    
    public BlockInfoUtil(final BlockPos pos,Block b, Color color) {
        this.pos = pos;
        this.block = b;
        this.color = color;
    }
    
    public BlockInfoUtil(final int x, final int y, final int z,Block b, Color color) {
        this(new BlockPos(x, y, z),b, color);
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BlockInfoUtil that = (BlockInfoUtil)o;
        return Objects.equal((Object)this.pos, (Object)that.pos);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.pos });
    }
}
