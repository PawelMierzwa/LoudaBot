package Commands.Player;

import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.protocol.v4.TrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import utils.JDAListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class queue {

    private static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void queueCommand(SlashCommandInteractionEvent event) {
        final var link = JDAListener.client.getOrCreateLink(event.getGuild().getIdLong());
        final var mngr = JDAListener.getOrCreateMusicManager(event.getGuild().getIdLong());
        final var musicManager = mngr.scheduler;
        if (musicManager.queue.isEmpty()) {
            event.reply("Queue is currently empty.").queue();
            return;
        }

        int trackCount = (Math.min(musicManager.queue.size(), 20));
        List<Track> trackList = new ArrayList<>(musicManager.queue);

        EmbedBuilder queInfo = new EmbedBuilder();
        queInfo.setTitle("**Now playing: **");

        Optional<LavalinkPlayer> audioPlayer = mngr.getPlayer();
        Track trackNow = link.getCachedPlayer().getTrack();
        try {
            TrackInfo infoNow = trackNow.getInfo();
            queInfo.appendDescription("`" + infoNow.getTitle() + " by ")
                    .appendDescription(infoNow.getAuthor() + "` [`" + formatTime(infoNow.getPosition()) + '/' + formatTime(infoNow.getLength()) + "`]\n")
                    .appendDescription("**Current Queue:**\n");

            for (int i = 0; i < trackCount; i++) {
                Track track = trackList.get(i);
                TrackInfo info = track.getInfo();

                queInfo.appendDescription("**#")
                        .appendDescription(String.valueOf(i + 1))
                        .appendDescription("** `")
                        .appendDescription(info.getTitle())
                        .appendDescription("` [`")
                        .appendDescription(formatTime(info.getLength()))
                        .appendDescription("`]\n");
            }

            if (trackList.size() > trackCount) {
                queInfo.appendDescription("And `")
                        .appendDescription(String.valueOf(trackList.size() - trackCount))
                        .appendDescription("` more...");
            }

            event.replyEmbeds(queInfo.build()).queue();
        } catch (NullPointerException e) {
            System.out.println("gigaerror");
        }
    }
}
