package Commands.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class chkuser {
    public static void userCommand(SlashCommandInteractionEvent event, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/ HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String BotName = event.getGuild().getSelfMember().getEffectiveName();
        Member SearchedUser = event.getGuild().getMember(user);

        EmbedBuilder userEmbed = new EmbedBuilder();

        userEmbed.setTitle("User Information");
        userEmbed.setColor(SearchedUser.getColor());
        userEmbed.addField("Name: ", user.getName(), false);
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
}
