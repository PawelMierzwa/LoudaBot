package lavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public static List<AudioTrack> bestResults = new ArrayList<>();

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandInteractionEvent event, String trackUrl) {
        if (event.getGuild() == null) return;
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                event.reply("Adding track: " + track.getInfo().title).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                if (playlist.isSearchResult()){
                    EmbedBuilder chooseEmbed = new EmbedBuilder();
                    chooseEmbed.setTitle("**Choose the song**");

                    for (int i = 1; i <= 10; i++){
                        bestResults.add(tracks.get(i-1));
                    }
                    String uId = event.getUser().getId();
                    StringSelectMenu.Builder builder = StringSelectMenu.create(uId + ":choose-song");

                    int index = 1;
                    for (AudioTrack result: bestResults){
                        builder.addOption(index + ". " + result.getInfo().title, String.valueOf(index));
                        index++;
                    }

                    event.deferReply().addEmbeds(chooseEmbed.build())
                            .addActionRow(builder.build())
                            .addActionRow(
                                    Button.danger(uId + ":delete", "Delete")
                            ).queue();
                    chooseEmbed.clear();
                } else {
                    event.reply("Adding to queue: " + playlist.getName() + ".\nSize of playlist: " + tracks.size()).queue();
                    for (final AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }
                }
            }

            @Override
            public void noMatches() {
                event.reply("No songs found!").setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.reply("Failed to load the song!").setEphemeral(true).queue();
                System.out.println("loadFailed: " + exception.severity + "\n" + exception.getMessage());
            }
        });
    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}