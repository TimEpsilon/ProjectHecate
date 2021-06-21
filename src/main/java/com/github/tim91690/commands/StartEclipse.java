package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.boss.NPC;
import com.github.tim91690.eclipse.mobs.boss.ScarletRabbit;
import com.github.tim91690.eclipse.mobs.boss.Shadows;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            NPC npc = new NPC(p.getLocation(),"test","ewogICJ0aW1lc3RhbXAiIDogMTYyNDIzNzkwODA1OCwKICAicHJvZmlsZUlkIiA6ICJkMzkxZmJlNTZlNWI0NjJiODM5NDM5NDkxZWNkZmNmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaW05MTY5MCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2YyZDY2ZGYwYzc5MWRkZWQzNmViM2RiOWVhZGIzYTE0NTVmYTNkNWY3OGI4YmNiNzBjZjJhMWUyNDdjNzJhIgogICAgfQogIH0KfQ==",
                    "Q8MAZnTk4nQtHinpDxBI/5PT0gezcxVXYyS8TRSaJjhOu5DNWZ/CMfzS0+GwOZ84QkQHkYqQxNfwCCN26fHUH21FdcTzC1H3L+mKSwigjsb5o+WKuIj29T5Fu5OMtvcWkA5JVptAh/h5k2cw+b41gqvJ7v4HnTBlvKOZWRck+3rE/e+0LC8KYTSsQjzoxN9GXatUo6JxxYVUFgAtV9tDnZlx17Xx0BMbEosmZuLX5HjpmK/R2Tur07oWN9WqlBMgT2irOEp/L7Isw2KKCdXiIWfIpcyc9CkqtGszqbylZuMk1UULn9nwR43TrkMcUAzKQAz1U0CX8Grl2IzVPbbRoSSLzjdwE1X0PO9qqg5ybyGoyyGt9QWoepfQ7KD6Lz/9AB74Ui3K5tsAmulBJNhoT3E9b4gSLVMmnNIeRBPMqVSB4O0udJ85XablRipOlXnWqc6Z7H0mxZDPMbtxDyHEuKgtijTwP4rvTivkdcqShl+9SJE/1nBlFUVG7Q9xNfffnAXMYq/qiEY8FsUXGQKX4TldGQiXhTyS96i3fDSAEKlD2/urlv6nhJtTBnJhAmWbs1br8cHje586cS5WXB0CDqbiSr14nkD2MSVCnrnHs6kFONpBuJ0Jal3VNeUeDQdPdfdxxSi1vJwx7zmJSmYupfSauHr6KXn9fmUtHsGxpFc=");
            npc.spawn();
            npc.show(p);

            return true;
        }
        return false;
    }
}
