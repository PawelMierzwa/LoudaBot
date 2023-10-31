package Commands.Goofy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class diceroll {
    public static void rollCommand(SlashCommandInteractionEvent event, OptionMapping max_number, OptionMapping amountOption) {
        Random rollsRand = new Random();

        int amount = 1;
        if (amountOption != null) {
            amount = amountOption.getAsInt();
        }

        int max = 6;
        if (max_number != null) {
            max = max_number.getAsInt();
        }

        event.getChannel().sendTyping().queue();

        EmbedBuilder builder = new EmbedBuilder();
        for (int i = 1; i <= amount; i++) {
            int roll = rollsRand.nextInt(max) + 1;
            builder.addField("Roll #" + i, "Dice rolled: **" + roll + "/" + max + "**", false);
        }
        event.replyEmbeds(builder.build()).queue();
    }
}
