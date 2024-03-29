package com.github.timepsilon.comet.misc;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Random;

public class TextManager {

    private static final Random random = new Random();
    private static final String demiurgeIntro = ChatColor.DARK_AQUA + "[DEMIURGE] ";


    public static void SamExplains(int line) {
        switch (line) {
            //start
            case 0 -> Bukkit.getOnlinePlayers().forEach(Player -> sendSamTextToPlayer(Player,ChatColor.GREEN + "[ALERTE] Comète à l'approche de son périgée. Une importante quantité d'énergie enveloppe la planète.",true));
            case 1 -> sendSamTextToPlayer(ChatColor.GREEN + "L'énergie donne forme à de puissantes créatures. Merci de survivre et d'en éliminer un maximum afin de ne pas impacter le cours de la bourse");
            case 6 -> sendSamTextToPlayer(ChatColor.GREEN + "[ASTUCE] : Un arc permet de tirer des flèches");

            //phase 1
            case 10 -> sendSamTextToPlayer(ChatColor.GREEN + "Périgée -80min. Le champ diffus énergétique continue d'augmenter");
            case 15 -> sendSamTextToPlayer(ChatColor.GREEN + "Nous vous rappelons que mourir est strictement prohibé");
            case 24 -> sendSamTextToPlayer(ChatColor.GREEN + "[PUB] : Avec la nouvelle paire d'Elytras™ Soaring Skies, parcourez le ciel à des vitesses inégalées");

            //phase 2
            case 30 -> sendSamTextToPlayer(ChatColor.GREEN + "Périgée -60min. Des débris commencent à entrer la haute atmosphère");
            case 36 -> sendSamTextToPlayer(ChatColor.GREEN + "[INFO] : En raison du nombre élevé de morts, les impots ont été augmenté de +400%");
            case 47 -> sendSamTextToPlayer(ChatColor.GREEN + "[SCIENCE] : Les phénomènes célestes sont connus pour attirer de nombreuses créatures, des morts-vivants aux gentils lapins");

            //phase 3
            case 50 -> sendSamTextToPlayer(ChatColor.GREEN + "Périgée -40min. Les insectes commencent à s'agiter");
            case 56 -> sendSamTextToPlayer(ChatColor.GREEN + "[ADMINISTRATION] : Nous vous rappelons que chaques morts nécessitent la rédaction d'un dossier à votre charge");
            case 65 -> sendSamTextToPlayer(ChatColor.GREEN + "[CITATION] : 'Connaissez l'ennemi et connaissez-vous vous-même ; en cent batailles vous ne courrez jamais aucun danger'");

            //phase 4
            case 70 -> sendSamTextToPlayer(ChatColor.GREEN + "Périgée -20min. L'énergie augmente exponentiellement");
            case 76 -> sendSamTextToPlayer(ChatColor.GREEN + "[INFO] : Communication reçue : " + ChatColor.AQUA + ChatColor.MAGIC + "Yaldabaoth à l'approche");
            case 78 -> sendSamTextToPlayer(ChatColor.GREEN + "[ASTUCE] : Choisissez un dieu et priez");

            //pre phase 5
            case 80 -> sendSamTextToPlayer(ChatColor.GREEN + "Triangulation en cours...");
            case 83 -> sendSamTextToPlayer(ChatColor.GREEN + "Pics d'énergies détectés. Il est recommandé de vous préparer à un combat");
            case 85 -> sendSamTextToPlayer(ChatColor.GREEN + "Périgée -5min");
            //phase 5
            case 90 -> sendSamTextToPlayer(ChatColor.GREEN + "Comète à son périgée!");
        }
    }

    public static void sendSamTextToPlayer(Player p, String s, boolean alert) {
        p.sendMessage(Component.text(CustomItems.PDAText + s));
        if (alert) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.PLAYERS,1,1);
            Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BIT,SoundCategory.PLAYERS,1,1),5);
        } else {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS,1,1);
        }

    }

    public static void sendSamTextToPlayer(Player p, String s) {
        sendSamTextToPlayer(p,s,false);
    }

    public static void sendSamTextToPlayer(String s, boolean alert) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendSamTextToPlayer(p, s, alert);
        }
    }

    public static void sendSamTextToPlayer(String s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendSamTextToPlayer(p,s,false);
        }
    }

    public static void demiurgeTalk(String s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            demiurgeTalk(p,s);
        }
    }

    public static void demiurgeTalk(Player p,String s) {
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, SoundCategory.HOSTILE, 1, 0.3f);
        p.sendMessage(Component.text(demiurgeIntro + ChatColor.AQUA + s));
    }

    public static void demiurgeLore(int line) {
        switch (line) {
            //spawn
            case 0 -> demiurgeTalk("Vos âmes impures seront miennes, mortels");
            //phase 1
            case 1 -> demiurgeTalk("Je suis le Demiurge, Dieu du Cosmos. Prosternez-vous mortels");
            case 2 -> demiurgeTalk("Présomptueux. Vous osez vous en prendre à moi?");
            case 3 -> demiurgeTalk("Vos conflits, vos désirs, je suis là pour y mettre fin");
            case 4 -> demiurgeTalk("Je vous observe tous depuis longtemps... Guerres, pouvoir, trahisons... Si curieux...");
            case 5 -> demiurgeTalk("Mais autre chose m'a intrigué : Votre pouvoir de création et destruction");
            case 6 -> demiurgeTalk("Un pouvoir égal à celui d'un Dieu, gaspillé de la sorte");
            case 7 -> demiurgeTalk("Et maintenant que la Comète leur a donné forme, les âmes des damnés vous tuerons");
            case 8 -> demiurgeTalk("Et vous reviendrez. Pour à nouveau mourir. Encore et encore");
            case 9 -> demiurgeTalk("Jusqu'à ce qu'il ne reste plus rien");
            //phase 3
            case 10 -> demiurgeTalk("J'admire votre résistance. Malheureusement, elle sera perdue avec votre âme");
            case 11 -> demiurgeTalk("Né dans l'obscurité, dénué de toute matière");
            case 12 -> demiurgeTalk("L'univers était parfaitement ordonné, libre de toute imperfection.");
            case 13 -> demiurgeTalk("Jusqu'à ce que vous arriviez");
            case 14 -> demiurgeTalk("Votre avarice, votre gourmandise, votre orgueil...");
            case 15 -> demiurgeTalk("Tous vos défauts, venant tacher ma toile cosmique");
            case 16 -> demiurgeTalk("Si faibles, si instables, si mortels...");
            case 17 -> {
                String title = ChatColor.DARK_RED + "ALORS POURQUOI NE VOULEZ VOUS PAS MOURIR!!!";
                StringBuilder finalText = new StringBuilder(ChatColor.DARK_RED + "" + title.charAt(0) + "" + title.charAt(1));
                for (int j = 2; j < title.length(); j++) {
                    if (random.nextFloat()<0.40) {
                        finalText.append(ChatColor.MAGIC).append(title.charAt(j)).append(ChatColor.RESET).append(ChatColor.DARK_RED);
                    } else {
                        finalText.append(title.charAt(j));
                    }
                }
                demiurgeTalk(finalText.toString());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.AMBIENT_BASALT_DELTAS_MOOD, SoundCategory.HOSTILE, 1, 1);
                }
            }

            //mort?
            case 18 -> demiurgeTalk("Comment? Votre détermination dépasserait la mienne...");
            case 19 -> demiurgeTalk("Qui l'aurait cru. Des mortels capables de vaincre un Dieu...");
            case 20 -> demiurgeTalk("Vous gagnez. Pour l'instant.");
            case 21 -> demiurgeTalk("Non...");
            case 22 -> demiurgeTalk("Vous ne gagnerez pas si facilement");
            case 23 -> demiurgeTalk("Vous viendrez à regretter d'avoir osé défier un dieu!");

            //TODO phase 5
        }
    }
}
