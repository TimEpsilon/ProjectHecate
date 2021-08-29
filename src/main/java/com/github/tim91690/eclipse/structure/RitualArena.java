package com.github.tim91690.eclipse.structure;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.ConfigManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;

public class RitualArena {

    public static void spawnRitualArena(boolean future) {
        int[] coord = ConfigManager.getCoord();
        World overworld = null;
        for (World world : Bukkit.getWorlds()) if(world.getEnvironment().compareTo(World.Environment.NORMAL) == 0) overworld = world;
        Location loc = new Location(overworld,coord[0],coord[1]-7,coord[2]);

        if (!future) {
            pasteSchematic("ritual_past.schem",loc);
        } else {
            pasteSchematic("ritual_future.schem",loc);
        }
    }

    private static void pasteSchematic(String name, Location loc) {
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(loc.getWorld());

        InputStream is = EventManager.getPlugin().getResource(name);
        BuiltInClipboardFormat format = BuiltInClipboardFormat.SPONGE_SCHEMATIC;

        try (ClipboardReader reader = format.getReader(is)) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1)) {

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                        .ignoreAirBlocks(false)
                        .copyBiomes(true)
                        .build();

                try {
                    Operations.complete(operation);
                    editSession.close();

                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
