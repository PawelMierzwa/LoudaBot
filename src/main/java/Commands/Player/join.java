package Commands.Player;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class join {
    public static void joinCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()){
            event.reply("You have to be in the voice channel!").setEphemeral(true).queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(selfVoiceState.inAudioChannel()){
            event.reply("Bot is already in the voice channel!").setEphemeral(true).queue();
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        AudioChannel audioChannel = memberVoiceState.getChannel();

        if (selfMember.hasPermission(audioChannel, Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(audioChannel);
            event.reply("Joined the voice channel").setEphemeral(true).queue();
        } else {
            event.reply("Can't join the channel, permission denied.").setEphemeral(true).queue();
        }
    }
}
