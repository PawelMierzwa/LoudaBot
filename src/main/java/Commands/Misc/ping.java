package Commands.Misc;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ping {
    public static void pingCommand(SlashCommandInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        long time = System.currentTimeMillis();
        channel.sendMessage("Pong!")
                        .queue(response->response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time)
                                .queue());
    }
}