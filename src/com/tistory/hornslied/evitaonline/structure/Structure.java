package com.tistory.hornslied.evitaonline.structure;

import java.util.Set;

import com.tistory.hornslied.evitaonline.universe.BlockCoord;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class Structure {

	private StructureType type;
	private Template template;
	private BlockCoord ogBlock;
	private Set<BlockCoord> structureBlocks;
	private Town town;
	
	public Structure(StructureType type, Template template, BlockCoord ogBlock, Town town) {
		this.type = type;
		this.template = template;
		this.ogBlock = ogBlock;
		this.town = town;
		
		loadBlocks();
	}
	
	private void loadBlocks() {
		
	}
	
}
