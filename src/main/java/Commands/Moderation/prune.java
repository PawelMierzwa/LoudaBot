package Commands.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class prune {
    public static void pruneCommand(SlashCommandInteractionEvent event)
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
