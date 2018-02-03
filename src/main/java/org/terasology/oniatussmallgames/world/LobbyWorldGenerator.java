package org.terasology.oniatussmallgames.world;

import org.terasology.engine.SimpleUri;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "LobbyWorldGenerator", displayName = "Lobby World Generator")
public class LobbyWorldGenerator extends BaseFacetedWorldGenerator {

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public LobbyWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        return new WorldBuilder(worldGeneratorPluginLibrary).setSeaLevel(0).addRasterizer(new LobbyRasterizer());
    }

    @Override
    public Vector3f getSpawnPosition(EntityRef entity) {
        return new Vector3f(0, LobbyRasterizer.FLOOR_HEIGHT + 1, 0);
    }
}
