import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Random;

public class ChatCommands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(Main.prefix + "ping")) {
            event.getChannel().sendTyping().queue();
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!")
                    .queue(response -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                    .queue());
        }

        if (args.length == 1 && args[0].equalsIgnoreCase(Main.prefix + "random")) {
            event.getChannel().sendMessage("To get your random number type !random [max number]").queue();
        } else if (args.length == 2 && args[0].equalsIgnoreCase(Main.prefix + "random")) {
            String maxNumber = args[1];
            Random rand = new Random();
            int random = rand.nextInt(Integer.parseInt(maxNumber)) + 1;
            event.getChannel().sendMessage("Your random number is: " + random).queue();
            event.getChannel().sendTyping().queue();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "randomemoji")){
            Random randEmojiArray = new Random();
            String[] emojiArray = {":sunglasses:", ":flushed:", ":heart:", ":eyes:", ":slight_smile:", ":woozy_face:", ":cold_face:", ":ok_hand:", ":cowboy:", ":nerd:", ":blush:", ":disguised_face:", ":+1:", ":frog:", ":smiling_face_with_tear:"};

            int randEmoji = randEmojiArray.nextInt(14);
            event.getChannel().sendMessage(emojiArray[randEmoji]).queue();
        }

        if (args.length == 1 && args[0].equalsIgnoreCase('+' + "piesek")){
            Random rand = new Random();
            int doggoRand = rand.nextInt(2);
            String[] doggoArray = {"https://external-preview.redd.it/B1TNwwu-3XpC87Hw7pwTChczz4YnDYp5Kj0YMHFmwdk.png?auto=webp&s=5b9cdf3bd1554eca6d9312791856143c9d84a9f2", "https://i.pinimg.com/736x/39/79/8c/39798c7502ec08bc40285fc63f2c5846.jpg"};
            EmbedBuilder siema = new EmbedBuilder();
            siema.setImage(doggoArray[doggoRand]);
            event.getChannel().sendMessageEmbeds(siema.build()).queue();
        }
        if (args.length == 1 && args[0].equalsIgnoreCase('+' + "kotek")){
            Random rand = new Random();
            int elgatoRand = rand.nextInt(3);
            String[] elgatoArray = {"http://memecrunch.com/image/5172f502afa96f6a00000006.png?w=400", "https://img.memecdn.com/nazi-cat-is-nazi_o_983843.jpg", "https://i.ytimg.com/vi/MW4x-K24JIQ/hqdefault.jpg"};
            EmbedBuilder siema2 = new EmbedBuilder();
            siema2.setImage(elgatoArray[elgatoRand]);
            event.getChannel().sendMessageEmbeds(siema2.build()).queue();
        }
        if (args.length == 1 && args[0].equalsIgnoreCase('+' + "małpka")){
            EmbedBuilder siema3 = new EmbedBuilder();
            siema3.setImage("http://rexcurry.net/pledge_of_allegiance_trained_monkey.jpg");
            event.getChannel().sendMessageEmbeds(siema3.build()).queue();
        }
    }
}
