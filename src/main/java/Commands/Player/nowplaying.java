package Commands.Player;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import utils.JDAListener;
import utils.UserData;

public class nowplaying {
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
        event.reply(
                "Currently playing: %s\nDuration: %s/%s\nRequester: <@%s>".formatted(
                        info.getTitle(),
                        player.getPosition(),
                        info.getLength(),
                        track.getUserData(UserData.class).requester()
                )
        ).queue();
    }
}
