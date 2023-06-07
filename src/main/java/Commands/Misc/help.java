package Commands.Misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class help {
    public static void helpCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder helpEmbed = new EmbedBuilder();
        helpEmbed.setTitle("**Slash commands (/)**");
        helpEmbed.setColor(Color.pink);

        helpEmbed.addField("__                                                                                                                          __", "**Music Player**", false);
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
        helpEmbed.addField("__                                                                                                                          __", "**Miscellaneous**", false);
        helpEmbed.addField("Say", "Makes the bot say what you tell it to.", true);
        helpEmbed.addField("Ping", "Pong!", true);
        helpEmbed.addField("Random", "Sends random number", true);
        helpEmbed.addField("RandomEmoji", "Sends random emoji.", true);
        helpEmbed.addField("Doge", "Sends random doge image.", true);
        helpEmbed.addField("Roll", "Rolls a dice", true);
        helpEmbed.addField("Coinflip", "Flips a coin", true);
        helpEmbed.addField("__                                                                                                                          __", "**Moderation**", false);
        helpEmbed.addField("User", "Shows information about provided user.", true);
        helpEmbed.addField("ServerInfo", "Provides current server information", true);
        helpEmbed.addField("Ban", "Bans a user from this server. Requires permissions.", true);
        helpEmbed.addField("Prune", "Prune number of messages from the channel. Requires Permissions.", true);


        event.replyEmbeds(helpEmbed.build()).queue();
    }
}
