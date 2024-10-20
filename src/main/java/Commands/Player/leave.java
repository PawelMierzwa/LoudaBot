package Commands.Player;

import lavalink.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import utils.JDAListener;

public class leave {
    public static void leaveCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()){
            event.reply("You have to be in the voice channel!").setEphemeral(true).queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(!selfVoiceState.inAudioChannel()){
            event.reply("Bot is not in any voice channel :thinking:").setEphemeral(true).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            event.reply("You need to be in the same channel as the bot!").setEphemeral(true).queue();
            return;
        }
        AudioManager audioManager = event.getGuild().getAudioManager();

        Guild guild = event.getGuild();

        GuildMusicManager musicManager = JDAListener.getOrCreateMusicManager(guild.getIdLong());

        musicManager.stop();
        musicManager.scheduler.queue.clear();

        audioManager.closeAudioConnection();
        event.reply("Left the voice channel").setEphemeral(true).queue();
    }

}
