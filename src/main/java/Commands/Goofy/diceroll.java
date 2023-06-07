package Commands.Goofy;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class diceroll {
    public static void rollCommand(SlashCommandInteractionEvent event, OptionMapping max_number, OptionMapping amountOption) {
        Random rollsRand = new Random();

        int amount = amountOption.getAsInt();// default 1

        int max = max_number.getAsInt();

        if (amount <= 10) {
            event.getChannel().sendTyping().queue();
            for (int i = 1; i <= amount; i++) {
                int roll = rollsRand.nextInt(max) + 1;
                event.getChannel().sendMessage("Dice rolled: **" + roll + "/" + max_number + "**").queue();
            }
        }
    }
}
