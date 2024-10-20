package Commands.Player;

import lavalink.GuildMusicManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import utils.JDAListener;

public class skip {
    public static void skipCommand(SlashCommandInteractionEvent event) {
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

        final var audioPlayer = JDAListener.client.getOrCreateLink(event.getGuild().getIdLong()).getCachedPlayer();
        GuildMusicManager musicManager = JDAListener.getOrCreateMusicManager(event.getGuild().getIdLong());

        if (audioPlayer.getTrack() == null) {
            event.reply("There is no track playing at this moment.").queue();
            return;
        }

        musicManager.nextTrack();
        event.reply("Skipped the current track").queue();
    }
}
