package utils;

import dev.arbjerg.lavalink.client.LavalinkClient;
import lavalink.GuildMusicManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JDAListener extends ListenerAdapter {
    private static final long DUNCTE = 191231307290771456L;

    private static final Logger LOG = LoggerFactory.getLogger(JDAListener.class);

    public static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public static LavalinkClient client;

    public static GuildMusicManager getOrCreateMusicManager(long guildId) {
        synchronized(JDAListener.class) {
            var mng = JDAListener.musicManagers.get(guildId);

            if (mng == null) {
                mng = new GuildMusicManager(guildId, JDAListener.client);
                JDAListener.musicManagers.put(guildId, mng);
            }

            return mng;
        }
    }

    public JDAListener(LavalinkClient client) {
        JDAListener.client = client;
    }
}
