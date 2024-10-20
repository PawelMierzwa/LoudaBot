//import dev.arbjerg.lavalink.internal.LavalinkSocket;
//import lavaPlayer.GuildMusicManager;
//import dev.arbjerg.lavalink.client.*;
//import dev.arbjerg.lavalink.client.event.*;
//import dev.arbjerg.lavalink.client.loadbalancing.RegionGroup;
//import dev.arbjerg.lavalink.client.loadbalancing.builtin.VoiceRegionPenaltyProvider;
//import net.dv8tion.jda.api.JDA;
//
//import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
//
//public class PlayerManager {
//    private static PlayerManager INSTANCE;
//
//    private final Map<Long, GuildMusicManager> musicManagers;
//    private final JdaLavalink lavalink;
//    private final String LAVALINK_PASSWORD = "your_lavalink_password"; // Configurable
//
//    public PlayerManager() {
//        this.musicManagers = new HashMap<>();
//        this.lavalink = new JdaLavalink(...BotId, this::getShard);  // Lavalink instance
//    }
//
//    private int getShard() {
//        return 0;  // Shard if you use sharding, otherwise leave 0
//    }
//
//    public LavalinkSocket connectToLavalink(JDA jda) {
//        LavalinkSocket socket = this.lavalink.addNode(new URI("ws://your_lavalink_url"), LAVALINK_PASSWORD);
//        jda.addEventListener(this.lavalink);
//        return socket;
//    }
//
//    public GuildMusicManager getMusicManager(Guild guild) {
//        return this.musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
//            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.lavalink.getLink(guild));
//
//            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
//            return guildMusicManager;
//        });
//    }
//
//    public static PlayerManager getINSTANCE() {
//        if (INSTANCE == null) {
//            INSTANCE = new PlayerManager();
//        }
//        return INSTANCE;
//    }
//}
