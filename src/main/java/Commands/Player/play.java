package Commands.Player;

import dev.arbjerg.lavalink.client.Link;
import lavalink.AudioLoader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.JDAListener;

import java.net.URL;

public class play extends ListenerAdapter {
    private static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void playCommand(SlashCommandInteractionEvent event, String song, String source) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        var audioChannel = memberVoiceState.getChannel();
        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You have to be in the voice channel!").setEphemeral(true).queue();
            return;
        }
        if (guild.getSelfMember().getVoiceState().inAudioChannel()) {
            event.deferReply(false).queue();
        } else {
            if (event.getGuild().getSelfMember().hasPermission(audioChannel, Permission.VOICE_CONNECT)) {
                event.getJDA().getDirectAudioController().connect(audioChannel);
            } else {
                event.reply("Can't join the channel, permission denied.").setEphemeral(true).queue();
                return;
            }
        }

        String search = "";
        boolean isSearchResult;
        if (!isValidURL(song)) {
            switch (source.toLowerCase()) {
                case "ytm":
                    search = "ytmsearch:" + song;
                    isSearchResult = true;
                    break;
                case "sc":
                    search = "scsearch:" + song;
                    isSearchResult = true;
                    break;
                default:
                    search = "ytsearch:" + song;
                    isSearchResult = true;
                    break;
            }
        } else {
            search = String.join("", song);
            isSearchResult = false;
        }

        if (search.isEmpty()) {
            event.reply("Invalid source!").setEphemeral(true).queue();
            return;
        }

        final long guildId = guild.getIdLong();
        final Link link = JDAListener.client.getOrCreateLink(guildId);
        final var mngr = JDAListener.getOrCreateMusicManager(guildId);
        link.loadItem(search).subscribe(new AudioLoader(event, mngr));
    }
}

//import static lavaPlayer.PlayerManager.bestResults;
//
//public class play extends ListenerAdapter {
//
//    public static boolean isSearchResult;
//
//    private static boolean isValidURL(String urlString) {
//        try {
//            URL url = new URL(urlString);
//            url.toURI();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public static void playCommand(SlashCommandInteractionEvent event, String song) {
//        Member member = event.getMember();
//        GuildVoiceState memberVoiceState = member.getVoiceState();
//        bestResults.clear();
//
//        if (!memberVoiceState.inAudioChannel()) {
//            event.reply("You have to be in the voice channel!").setEphemeral(true).queue();
//            return;
//        }
//
//        Member self = event.getMember();
//        GuildVoiceState selfVoiceState = self.getVoiceState();
//
//        if (!selfVoiceState.inAudioChannel()) {
//            event.reply("Bot is not in any voice channel :thinking:").setEphemeral(true).queue();
//            return;
//        }
//
//        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
//            event.reply("You need to be in the same channel as the bot!").setEphemeral(true).queue();
//            return;
//        }
//
//        Member selfMember = event.getGuild().getSelfMember();
//        AudioChannel audioChannel = memberVoiceState.getChannel();
//
//        if (!selfMember.hasPermission(audioChannel, Permission.VOICE_CONNECT)) {
//            event.reply("Can't join the channel, permission denied.").setEphemeral(true).queue();
//            return;
//        }
//
//        String link;
//
//        if (!isValidURL(song)) {
//            link = "ytsearch:" + song;
//            isSearchResult = true;
//        } else {
//            link = String.join(" ", song);
//            isSearchResult = false;
//        }
//
//        AudioManager audioManager = event.getGuild().getAudioManager();
//        audioManager.openAudioConnection(memberVoiceState.getChannel());
//        PlayerManager.getINSTANCE().loadAndPlay(event, link);
//    }
//
//    String authorId;
//
//    @Override
//    public void onButtonInteraction(ButtonInteractionEvent event) {
//        String interactionId = event.getComponentId();
//        String[] id = interactionId.split(":");
//        authorId = id[0];
//        String buttonId = id[1];
//        if (!event.getUser().getId().equals(authorId)) {
//            return;
//        }
//        if (Objects.equals(buttonId, "delete")) {
//            event.getInteraction().getHook().deleteOriginal().queue();
//        }
//    }
//
//    @Override
//    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
//        String interactionId = event.getComponentId();
//        String[] id = interactionId.split(":");
//        String _authorId = id[0];
//        String selectorId = id[1];
//        String clickerId = event.getUser().getId();
//        if (selectorId.equals("choose-song") && _authorId.equals(clickerId)){
//            final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
//            Date date = new Date();
//            EmbedBuilder playEmbed = new EmbedBuilder();
//
//            List<String> selected = event.getValues();
//            int choice = Integer.parseInt(selected.get(0));
//
//            List<AudioTrack>results = bestResults;
//
//            //zmień odpowiedź na nową wiadomość z @silent, napraw delete
//            if (choice >= 1 && choice <= results.size()) {
//                playEmbed.appendDescription("**Adding Track: " + results.get(choice - 1).getInfo().title + "**\n");
//                playEmbed.appendDescription("Requested by: <@" + _authorId + "> at " + date);
//                event.editSelectMenu(null).queue();
//                /*event.getMessage().delete();
//                EmbedBuilder newEmbed = new EmbedBuilder();
//                newEmbed.appendDescription("**Adding Track: " + results.get(choice - 1).getInfo().title + "**\n");
//                newEmbed.appendDescription("Requested by: <@" + _authorId + "> at " + date);
//                event.getChannel().asTextChannel().sendMessage("@silent").addEmbeds(newEmbed.build()).queue();
//                musicManager.scheduler.queue(results.get(choice - 1));*/
//                musicManager.scheduler.queue(results.get(choice - 1));
//            }
//        }
//    }
//}