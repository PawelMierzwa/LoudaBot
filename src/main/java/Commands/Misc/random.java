package Commands.Misc;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class random {
    public static void randomNumberCommand(SlashCommandInteractionEvent event, OptionMapping max) {
        int maxNumber = max.getAsInt();
        Random rand = new Random();
        int random = rand.nextInt(maxNumber) + 1;
        event.reply("Your random number is: " + random).queue();
    }
}