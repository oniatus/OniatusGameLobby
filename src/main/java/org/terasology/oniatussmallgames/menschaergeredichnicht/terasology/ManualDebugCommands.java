package org.terasology.oniatussmallgames.menschaergeredichnicht.terasology;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.math.geom.Vector3i;

@RegisterSystem
public class ManualDebugCommands extends BaseComponentSystem {

    @Command
    public void spawnBoard(@Sender EntityRef sender, @CommandParam("x") int x, @CommandParam("y") int y, @CommandParam("z") int z) {
        sender.send(new GenerateBoardBlocksEvent(new Vector3i(x, y, z)));
    }
}
