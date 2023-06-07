package Commands.Goofy;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Random;

public class remoji {
    public static void randomemojiCommand(SlashCommandInteractionEvent event){
        Random rand = new Random();
        String[] emojiArray = {":sunglasses:", ":flushed:", ":heart:", ":eyes:", ":slight_smile:", ":woozy_face:", ":cold_face:", ":ok_hand:", ":cowboy:", ":nerd:", ":blush:", ":disguised_face:", ":+1:", ":frog:", ":smiling_face_with_tear:"};

        int randEmoji = rand.nextInt(emojiArray.length);
        event.reply(emojiArray[randEmoji]).queue();
    }
}
