package com.github.timepsilon.comet.structure;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.*;

public class RitualArena {

    public static void spawnRitualArena(boolean future) {
        Location loc = ConfigManager.getLoc();

        if (!future) {
            pasteSchematic("ritual_past.schem",loc);
        } else {
            pasteSchematic("ritual_future.schem",loc);
        }
    }

    public static void spawnPylon(Location loc,boolean isActive) {
        if (isActive) {
            pasteSchematic("pylon_active.schem",loc);
        } else {
            pasteSchematic("pylon.schem",loc);
        }
    }

    public static void openBarrier() {
        Location loc = ConfigManager.getLoc();

        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(loc.getWorld());

            try (EditSession es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1)) {

                es.enableQueue();
                es.makeCylinder(BlockVector3.at(loc.getX(),loc.getY()+13,loc.getZ()),new BaseBlock(BlockTypes.AIR),50,50,false);
                es.flushQueue();

                loc.clone().add(0,10,18).getBlock().setType(Material.EMERALD_BLOCK);
                loc.clone().add(0,10,-18).getBlock().setType(Material.EMERALD_BLOCK);
                loc.clone().add(18,10,0).getBlock().setType(Material.EMERALD_BLOCK);
                loc.clone().add(-18,10,0).getBlock().setType(Material.EMERALD_BLOCK);
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

                Mask mask = new BlockTypeMask(clipboard, BlockTypes.LAPIS_BLOCK);
                mask = Masks.negate(mask);

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                        .ignoreAirBlocks(false)
                        .maskSource(mask)
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
