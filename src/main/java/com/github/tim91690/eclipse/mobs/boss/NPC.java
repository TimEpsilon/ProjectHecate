package com.github.tim91690.eclipse.mobs.boss;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPC {
    private Location location;
    private String name;
    private GameProfile gameProfile;
    private EntityPlayer entityPlayer;
    private String texture;
    private String signature;

    public NPC(Location location, String name,String texture,String sign) {
        this.location = location;
        this.name = name;
        this.texture = texture;
        this.signature = sign;


    }

    public void spawn() {
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        this.gameProfile = new GameProfile(UUID.randomUUID(),this.name);
        this.gameProfile.getProperties().put("textures",new Property("textures",texture,signature));

        this.entityPlayer = new EntityPlayer(minecraftServer,worldServer,this.gameProfile);

        this.entityPlayer.setLocation(this.location.getX(),this.location.getY(),this.location.getZ(),this.location.getYaw(),this.location.getPitch());
    }

    public void show(Player player) {
        PlayerConnection playerConnection = ((CraftPlayer)player).getHandle().b;

        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a,entityPlayer));
        playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
    }

}
