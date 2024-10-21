package Commands.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import utils.JDAListener;
import utils.UserData;

import java.util.concurrent.TimeUnit;

public class nowplaying {
    private static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void nowplayingCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()){
            event.reply("You have to be in the voice channel!").setEphemeral(true).queue();
            return;
        }

        Member self = event.getMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()){
            event.reply("Bot is not in any voice channel :thinking:").setEphemeral(true).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            event.reply("You need to be in the same channel as the bot!").setEphemeral(true).queue();
            return;
        }

        final var link = JDAListener.client.getOrCreateLink(event.getGuild().getIdLong());
        final var player = link.getCachedPlayer();

        if (player == null) {
            event.reply("There is no track playing at this moment.").queue();
            return;
        }

        final var track = player.getTrack();
        if (track == null) {
            event.reply("Nothing playing currently!").queue();
            return;
        }

        final var info = track.getInfo();
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(info.getTitle());
        embedBuilder.setUrl(info.getUri());
        embedBuilder.setDescription(
                "Playing from: **%s**\nAuthor: **%s**\nRequested by: <@%s>".formatted(
                        info.getSourceName(),
                        info.getAuthor(),
                        track.getUserData(UserData.class).requester()
                )
        );
        embedBuilder.appendDescription("Position: %s, Debug: %s".formatted(formatTime(info.getPosition()), info.getPosition()));
        String footer = info.isStream() ? "LIVE" : "Duration: %s/%s".formatted(formatTime(info.getPosition()), formatTime(info.getLength()));
        embedBuilder.setImage(info.getArtworkUrl());
        embedBuilder.setFooter(footer, null);
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
