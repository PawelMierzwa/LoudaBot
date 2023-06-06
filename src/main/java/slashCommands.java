import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaPlayer.GuildMusicManager;
import lavaPlayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("ConstantConditions")
public class slashCommands extends ListenerAdapter {

    public boolean isSearchResult;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        //noinspection EnhancedSwitchMigration
        switch (event.getName())
        {
            case "help":
                help(event);
                break;
            case "ban":
                Member member = event.getOption("user").getAsMember(); // the "user" option is required so it doesn't need a null-check here
                User user = event.getOption("user").getAsUser();
                ban(event, user, member);
                break;
            case "user":
                user(event, event.getOption("user").getAsUser());
                break;
            case "say":
                say(event, event.getOption("content").getAsString()); // content is required so no null-check here
                break;
            case "prune": // 2 stage command with a button prompt
                prune(event);
                break;
            case "join": //vc shit ye
                join(event);
                break;
            case "leave":
                leave(event);
                break;
            case "play":
                String song = event.getOption("song").getAsString();
                play(event, song);
                break;
            case "stop":
                stop(event);
                break;
            case "clear":
                clear(event);
                break;
            case "skip":
                skip(event);
                break;
            case "resume":
                resume(event);
                break;
            case "nowplaying":
                np(event);
                break;
            case "queue":
                q(event);
                break;
            case "loop":
                loop(event, event.getOption("loop").getAsBoolean(), event.getOption("type").getAsInt());
                break;
            case "roll":
                roll(event, event.getOption("max_number"), event.getOption("amount"));
                break;
            case "coinflip":
                coinflip(event);
                break;
        }
    }

    private void roll(SlashCommandInteractionEvent event, OptionMapping max_number, OptionMapping amountOption) {
        Random rollsRand = new Random();

        int amount = amountOption == null
                ? 1 // default 1
                : Math.min(5, Math.max(1, amountOption.getAsInt()));

        int max = max_number == null
                ? 12 // default 12
                : Math.min(100, Math.max(6, amountOption.getAsInt()));

        if (amount <= 10){
            event.getChannel().sendTyping().queue();
            for (int i = 1; i <= amount; i++) {
                int roll = rollsRand.nextInt(max) + 1;
                event.getChannel().sendMessage("Dice rolled: **" + roll + "/" + max_number + "**").queue();
                try {
                    wait(1000);
                } catch (InterruptedException ignored) {
                }
                }
            }
    }

    private void coinflip(SlashCommandInteractionEvent event){
        Random coinRand = new Random();

        int coinflip = coinRand.nextInt(9) + 1;
        if (coinflip >= 5) {
            event.reply("Heads").queue();
        } else {
            event.reply("Tails").queue();
        }
    }

    private void loop(SlashCommandInteractionEvent event, boolean loop, int type) {
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

    private void user(SlashCommandInteractionEvent event, User user) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/ HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            String BotName = event.getGuild().getSelfMember().getEffectiveName();
            Member SearchedUser = event.getGuild().getMember(user);

            EmbedBuilder userEmbed = new EmbedBuilder();

            userEmbed.setTitle("User Information");
            userEmbed.setColor(SearchedUser.getColor());
            userEmbed.addField("Name: ", user.getName() + "#" + user.getDiscriminator(), false);
            userEmbed.addField("Nickname: ", SearchedUser.getEffectiveName(),false);
            userEmbed.addField("Online status: ", SearchedUser.getOnlineStatus().toString(), false);
            if (SearchedUser.hasTimeJoined()){
                userEmbed.addField("Joined the server on:", SearchedUser.getTimeJoined().format(formatter), true);
            } else {
                userEmbed.addField("Joined the server on:", "Can't provide", true);
            }
            if (SearchedUser.isTimedOut()){
                userEmbed.addField("Timeout ends on: ",SearchedUser.getTimeOutEnd().format(formatter),true);
            } else {
                userEmbed.addField("Is timed out: ", "No", true);
            }
            userEmbed.addField("Mutual Servers with " + BotName, String.valueOf(user.getMutualGuilds().size()),false);
            userEmbed.addField("Number of badges: ", String.valueOf(user.getFlags().size()), true);
            userEmbed.addField("Created account on: ", user.getTimeCreated().format(formatter), false);
            userEmbed.setThumbnail(user.getEffectiveAvatarUrl());
            userEmbed.setFooter(simpleDateFormat.format(date));

            event.replyEmbeds(userEmbed.build()).queue();
    }

    private void help(SlashCommandInteractionEvent event) {
        EmbedBuilder helpEmbed = new EmbedBuilder();
        helpEmbed.setTitle("Commands list");
        helpEmbed.setColor(Color.pink);
        helpEmbed.setDescription("Prefix is **" + Main.prefix + "**");
        helpEmbed.addField("Ping", "Pong!", true);
        helpEmbed.addField("Random", "Sends random number", true);
        helpEmbed.addField("ServerInfo", "Provides current server information", true);
        helpEmbed.addField("RandomEmoji", "Sends random emoji.", true);
        helpEmbed.addField("Doge", "Sends random doge image.", true);

        helpEmbed.addBlankField(false);
        helpEmbed.addField("Slash commands (/)", " ", false);

        helpEmbed.addField("Play", "Plays provided song.", true);
        helpEmbed.addField("Queue", "Shows 20 songs from the queue.", true);
        helpEmbed.addField("Stop", "Pauses current song.", true);
        helpEmbed.addField("Resume", "Resumes stopped song.", true);
        helpEmbed.addField("Nowplaying", "Shows current playing song.", true);
        helpEmbed.addField("Join", "Joins the channel.", true);
        helpEmbed.addField("Leave", "Leave the channel.", true);
        helpEmbed.addField("Skip", "Skips current playing song.", true);
        helpEmbed.addField("Loop", "Makes current song play indefinitely", true);
        helpEmbed.addField("Clear", "Clear the queue.", true);
        helpEmbed.addField("Say", "Makes the bot say what you tell it to.", true);
        helpEmbed.addField("User", "Shows information about provided user.", true);
        helpEmbed.addField("Roll", "Rolls a dice", true);
        helpEmbed.addField("Coinflip", "Flips a coin", true);
        helpEmbed.addField("Ban", "Bans a user from this server. Requires permissions.", true);
        helpEmbed.addField("Prune", "Prune number of messages from the channel. Requires Permissions.", true);

        event.replyEmbeds(helpEmbed.build()).queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void q(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            event.reply("Queue is currently empty.").queue();
            return;
        }

        int trackCount = (Math.min(queue.size(), 20));
        List<AudioTrack> trackList = new ArrayList<>(queue);

        EmbedBuilder queInfo = new EmbedBuilder();
        queInfo.setTitle("**Now playing: **");

        AudioPlayer audioPlayer = musicManager.audioPlayer;
        AudioTrack trackNow = audioPlayer.getPlayingTrack();
        try {
            AudioTrackInfo infoNow = trackNow.getInfo();
            queInfo.appendDescription("`" + infoNow.title + " by ")
                    .appendDescription(infoNow.author + "` [`" + formatTime(trackNow.getDuration()) + "`]\n")
                    .appendDescription("**Current Queue:**\n");

            for (int i = 0; i < trackCount; i++) {
                AudioTrack track = trackList.get(i);
                AudioTrackInfo info = track.getInfo();

                queInfo.appendDescription("**#")
                        .appendDescription(String.valueOf(i + 1))
                        .appendDescription("** `")
                        .appendDescription(String.valueOf(info.title))
                        .appendDescription("` [`")
                        .appendDescription(formatTime(track.getDuration()))
                        .appendDescription("`]\n");
            }

            if (trackList.size() > trackCount) {
                queInfo.appendDescription("And `")
                        .appendDescription(String.valueOf(trackList.size() - trackCount))
                        .appendDescription("` more...");
            }

            event.replyEmbeds(queInfo.build()).queue();
        } catch (NullPointerException e) {
            System.out.println("gigaerror");
        }
    }
    private void np(SlashCommandInteractionEvent event) {
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
        AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
            event.reply("There is no track playing at this moment.").queue();
            return;
        }

       AudioTrackInfo info = track.getInfo();

        event.reply(info.uri).queue();
    }

    private void resume(SlashCommandInteractionEvent event) {
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

        if (!musicManager.scheduler.player.isPaused()) {
            event.reply("Current track isn't stopped!").queue();
            return;
        }

        musicManager.scheduler.player.setPaused(false);
        event.reply("Current track has been resumed.").queue();
    }

    private void stop(SlashCommandInteractionEvent event) {
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

        if (musicManager.scheduler.player.isPaused()) {
            event.reply("Current track is already stopped!").queue();
            return;
        }

        musicManager.scheduler.player.setPaused(true);

        event.reply("The player has been stopped.").queue();
    }

    private void skip(SlashCommandInteractionEvent event) {
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

        musicManager.scheduler.nextTrack();
        event.reply("Skipped the current track").queue();
    }

    private void clear(SlashCommandInteractionEvent event) {
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
        musicManager.scheduler.player.setPaused(true);
        musicManager.scheduler.queue.clear();

        event.reply("The queue has been cleared.").queue();
    }

    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void play(SlashCommandInteractionEvent event, String song) {
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
        PlayerManager.getINSTANCE().loadAndPlay(event, event.getTextChannel(), link);

    }

    private void leave(SlashCommandInteractionEvent event) {
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

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(guild);

        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();

        audioManager.closeAudioConnection();
        event.reply("Left the voice channel").setEphemeral(true).queue();
    }

    private void join(SlashCommandInteractionEvent event) {
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

    public static int ChooseResult;

    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        // users can spoof this id so be careful what you do with this
        String[] id = event.getComponentId().split(":"); // this is the custom id we specified in our button
        String authorId = id[0];
        String type = id[1];
        // When storing state like this is it is highly recommended to do some kind of verification that it was generated by you, for instance a signature or local cache
        if (!authorId.equals(event.getUser().getId()))
            return;
        event.deferEdit().queue(); // acknowledge the button was clicked, otherwise the interaction will fail

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getTextChannel().getGuild());

        Date date = new Date();

        EmbedBuilder playEmbed = new EmbedBuilder();

        switch (type) {
            case "1" -> {
                ChooseResult = 1;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(0).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getTextChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "2" -> {
                ChooseResult = 2;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(1).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getTextChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "3" -> {
                ChooseResult = 3;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(2).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getTextChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "4" -> {
                ChooseResult = 4;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(3).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getTextChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
            case "5" -> {
                ChooseResult = 5;
                playEmbed.appendDescription("**Adding Track: " + PlayerManager.bestResults.get(4).getInfo().title + "**\n");
                playEmbed.appendDescription("Requested by: <@" + authorId + "> at " + date);
                event.getTextChannel().sendMessageEmbeds(playEmbed.build()).queue();
            }
        }
        musicManager.scheduler.queue(PlayerManager.bestResults.get(ChooseResult-1));
        event.getHook().deleteOriginal().queue();
        PlayerManager.bestResults.clear();
    }

    public void ban(SlashCommandInteractionEvent event, User user, Member member)
    {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
        InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
        hook.setEphemeral(true); // All messages here will now be ephemeral implicitly
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS))
        {
            hook.sendMessage("You do not have the required permissions to ban users from this server.").queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(Permission.BAN_MEMBERS))
        {
            hook.sendMessage("I don't have the required permissions to ban users from this server.").queue();
            return;
        }

        if (member != null && !selfMember.canInteract(member))
        {
            hook.sendMessage("This user is too powerful for me to ban.").queue();
            return;
        }

        int delDays = 0;
        OptionMapping option = event.getOption("del_days");
        if (option != null) // null = not provided
            delDays = (int) Math.max(0, Math.min(7, option.getAsLong()));
        // Ban the user and send a success response
        event.getGuild().ban(user, delDays)
                .flatMap(v -> hook.sendMessage("Banned user " + user.getAsTag()))
                .queue();
    }

    public void say(SlashCommandInteractionEvent event, String content)
    {
        event.reply(content).queue(); // This requires no permissions!
    }

    public void prune(SlashCommandInteractionEvent event)
    {
        if(!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)){
            event.reply("You do not have permissions to delete the messages.").setEphemeral(true).queue(); //replies only the user who used the command :)
        } else {
            MessageChannel channel = event.getChannel();

            OptionMapping amountOption = event.getOption("amount"); // This is configured to be optional so check for null
            int amount = amountOption == null
                    ? 10 // default 10
                    : (int) Math.min(25, Math.max(2, amountOption.getAsLong())); // enforcement: must be between 2-25

            event.getChannel().getIterableHistory()
                    .skipTo(event.getIdLong())
                    .takeAsync(amount)
                    .thenAccept(channel::purgeMessages);

            event.reply("Deleted " + amount + " messages.").setEphemeral(true).queue();
        }
    }
}
