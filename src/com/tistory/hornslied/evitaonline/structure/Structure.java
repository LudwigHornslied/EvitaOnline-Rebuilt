package com.tistory.hornslied.evitaonline.structure;

import java.util.Set;

import org.bukkit.World;
import org.bukkit.block.Block;

import com.tistory.hornslied.evitaonline.universe.BlockCoord;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class Structure {

	private StructureType type;
	private Template template;
	private World world;
	private BlockCoord ogBlock;
	private Set<BlockCoord> structureBlocks;
	private Town town;
	
	public Structure(StructureType type, Template template, World world, BlockCoord ogBlock, Town town) {
		this.type = type;
		this.template = template;
		this.world = world;
		this.ogBlock = ogBlock;
		this.town = town;
		
		loadBlocks();
	}
	
	private void loadBlocks() {
		for(RelativeBlockCoord coord : template.getDatas().keySet()) {
			structureBlocks.add(getByRelativeCoord(ogBlock, coord));
		}
	}
	
	private static BlockCoord getByRelativeCoord(BlockCoord blockCoord, RelativeBlockCoord relativeCoord) {
		int x = blockCoord.getX() + relativeCoord.getRelativeX();
		int y = blockCoord.getY() + relativeCoord.getRelativeY();
		int z = blockCoord.getZ() + relativeCoord.getRelativeZ();
		
		return BlockCoord.parseCoord(x, y, z);
	}
	
	public boolean hasBlock(Block block) {
		return false;
	}
}
