package lavalink;


import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import dev.arbjerg.lavalink.protocol.v4.TrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import utils.UserData;

import java.util.List;

public class AudioLoader extends AbstractAudioLoadResultHandler {
    private final SlashCommandInteractionEvent event;
    private final GuildMusicManager mngr;

    public AudioLoader(SlashCommandInteractionEvent event, GuildMusicManager mngr) {
        this.event = event;
        this.mngr = mngr;
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded result) {
        final Track track = result.getTrack();

        var userData = new UserData(event.getUser().getIdLong());

        track.setUserData(userData);

        this.mngr.scheduler.enqueue(track);

        final var trackTitle = track.getInfo().getTitle();

        event.getHook().sendMessage("Added to queue: " + trackTitle).queue();
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded result) {
        final int trackCount = result.getTracks().size();
        event.getHook().sendMessage("Adding " + trackCount + " tracks to the queue from " + result.getInfo().getName() + "!").queue();
        this.mngr.scheduler.enqueuePlaylist(result.getTracks());
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult result) {
        final List<Track> tracks = result.getTracks();

        if (tracks.isEmpty()) {
            event.getHook().sendMessage("No tracks found!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Search results");
        for (Track track : tracks) {
            final TrackInfo info = track.getInfo();
            embed.appendDescription("**#" + (tracks.indexOf(track) + 1) + "** `")
                    .appendDescription(info.getTitle())
                    .appendDescription("` by ")
                    .appendDescription(info.getAuthor())
                    .appendDescription("\n");
        }

        event.getHook().sendMessageEmbeds(embed.build()).queue();

        final Track firstTrack = tracks.getFirst();

        event.getHook().sendMessage("Adding to queue: " + firstTrack.getInfo().getTitle()).queue();

        this.mngr.scheduler.enqueue(firstTrack);
    }

    @Override
    public void noMatches() {
        event.getHook().sendMessage("No matches found for your input!").queue();
    }

    @Override
    public void loadFailed(@NotNull LoadFailed result) {
        event.getHook().sendMessage("Failed to load the track! " + result.getException().getMessage()).queue();
    }
}