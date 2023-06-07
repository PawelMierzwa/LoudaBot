package Commands.Player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lavaPlayer.GuildMusicManager;
import lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class loop {
    public static void loopCommand(SlashCommandInteractionEvent event, boolean loop, int type) {
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

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.reply("There is no track playing at this moment.").queue();
            return;
        }

        if (musicManager.scheduler.repeating && loop || musicManager.scheduler.repeating2 && loop){
            event.reply("Loop is already on!").setEphemeral(true).queue();
            return;
        }
        if (!musicManager.scheduler.repeating && !loop  || !musicManager.scheduler.repeating2 && !loop){
            event.reply("Loop is already off!").setEphemeral(true).queue();
            return;
        }

        if (type == 2) {
            musicManager.scheduler.repeating2 = loop;
            event.reply("Loop **all** is " + (loop ? "**on**" : "**off**")).queue();
        } else {
            musicManager.scheduler.repeating = loop;
            event.reply("Loop **one** is " + (loop ? "**on**" : "**off**")).queue();
        }
    }
}
