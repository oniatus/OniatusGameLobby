package org.terasology.oniatussmallgames.world;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

import static org.terasology.world.chunks.ChunkConstants.CHUNK_REGION;

public class LobbyRasterizer implements WorldRasterizer {

    public static final int FLOOR_HEIGHT = 1;

    private Block glass;

    @Override
    public void initialize() {
        glass = CoreRegistry.get(BlockManager.class).getBlock("OniatusSmallGames:simpleGlass");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        if (chunk.getChunkWorldOffsetY() == 0) {
            for (int x = CHUNK_REGION.minX(); x <= CHUNK_REGION.maxX(); x++) {
                for (int z = CHUNK_REGION.minZ(); z <= CHUNK_REGION.maxZ(); z++) {
                    chunk.setBlock(x, 0, z, glass);
                }
            }
        }
    }
}
