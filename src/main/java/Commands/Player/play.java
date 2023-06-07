package Commands.Player;

import lavaPlayer.GuildMusicManager;
import lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URL;
import java.util.Date;

public class play extends ListenerAdapter {

    public static boolean isSearchResult;

    private static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void playCommand(SlashCommandInteractionEvent event, String song) {
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

        Member selfMember = event.getGuild().getSelfMember();
        AudioChannel audioChannel = memberVoiceState.getChannel();

        if (!selfMember.hasPermission(audioChannel, Permission.VOICE_CONNECT)) {
            event.reply("Can't join the channel, permission denied.").setEphemeral(true).queue();
            return;
        }

        String link;

        if (!isValidURL(song)) {
            link = "ytsearch:" + song;
            isSearchResult = true;
        } else {
            link = String.join(" ", song);
            isSearchResult = false;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.openAudioConnection(memberVoiceState.getChannel());
        PlayerManager.getINSTANCE().loadAndPlay(event, link);
    }

    public static int ChooseResult;

    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        // users can spoof this id (!)
        String[] id = event.getComponentId().split(":"); // custom id specified in the button
        String authorId = id[0]; // the interacting user
        String type = id[1]; // i cant remember what the hell is this xd
        if (!authorId.equals(event.getUser().getId()))
            return;
        event.deferEdit().queue(); // acknowledge the button was clicked, otherwise the interaction will fail

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        Date date = new Date();

        EmbedBuilder playEmbed = new EmbedBuilder();

        switch (type) {
            case "1" -> {
                ChooseResult = 1;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(0).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getMessageChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "2" -> {
                ChooseResult = 2;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(1).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getMessageChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "3" -> {
                ChooseResult = 3;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(2).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getMessageChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "4" -> {
                ChooseResult = 4;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(3).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getMessageChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "5" -> {
                ChooseResult = 5;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(4).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getMessageChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
        }
        musicManager.scheduler.queue(PlayerManager.bestResults.get(ChooseResult-1));
        event.getHook().deleteOriginal().queue();
        PlayerManager.bestResults.clear();
    }
}
