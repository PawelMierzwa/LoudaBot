package Commands.Player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaPlayer.GuildMusicManager;
import lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class queue {

    private static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void queueCommand(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            event.reply("Queue is currently empty.").queue();
            return;
        }

        int trackCount = (Math.min(queue.size(), 20));
        List<AudioTrack> trackList = new ArrayList<>(queue);

        EmbedBuilder queInfo = new EmbedBuilder();
        queInfo.setTitle("**Now playing: **");

        AudioPlayer audioPlayer = musicManager.audioPlayer;
        AudioTrack trackNow = audioPlayer.getPlayingTrack();
        try {
            AudioTrackInfo infoNow = trackNow.getInfo();
            queInfo.appendDescription("`" + infoNow.title + " by ")
                    .appendDescription(infoNow.author + "` [`" + formatTime(trackNow.getDuration()) + "`]\n")
                    .appendDescription("**Current Queue:**\n");

            for (int i = 0; i < trackCount; i++) {
                AudioTrack track = trackList.get(i);
                AudioTrackInfo info = track.getInfo();

                queInfo.appendDescription("**#")
                        .appendDescription(String.valueOf(i + 1))
                        .appendDescription("** `")
                        .appendDescription(String.valueOf(info.title))
                        .appendDescription("` [`")
                        .appendDescription(formatTime(track.getDuration()))
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
