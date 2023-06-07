package Commands.Goofy;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Random;

public class coinflip {
    public static void coinflipCommand(SlashCommandInteractionEvent event){
        Random coinRand = new Random();

        int coinflip = coinRand.nextInt(9) + 1;
        if (coinflip >= 5) {
            event.reply("Heads").queue();
        } else {
            event.reply("Tails").queue();
        }
    }
}
