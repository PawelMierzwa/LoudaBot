package Commands.Goofy;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class say {
    public static void sayCommand(SlashCommandInteractionEvent event, String content)
    {
        event.reply(content).queue();
    }
}
