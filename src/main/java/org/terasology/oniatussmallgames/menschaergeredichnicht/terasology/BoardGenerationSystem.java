package org.terasology.oniatussmallgames.menschaergeredichnicht.terasology;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

@RegisterSystem
public class BoardGenerationSystem extends BaseComponentSystem {

    @In
    private WorldProvider worldProvider;
    @In
    private BlockManager blockManager;

    @ReceiveEvent
    public void onGenerateBoardBlocks(GenerateBoardBlocksEvent event, EntityRef entity){
        Vector3i delta = event.getUpperLeftCorner();
        for (BoardWorldLookup.BoardBlock boardBlock : BoardWorldLookup.getAllBoardBlocks()) {
            Block block = blockManager.getBlock(boardBlock.getBlockUri());
            Vector3i position = new Vector3i(delta).addX(boardBlock.getDx()).addZ(boardBlock.getDz());
            worldProvider.setBlock(position, block);
        }

    }
}
