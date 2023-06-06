import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ready extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot is ready");
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent ev) {
        String prefix = "!!";
//        String prefix = Config.get("PREFIX");
        String raw = ev.getMessage().getContentRaw();

//        if (raw.equalsIgnoreCase(prefix + "shutdown") && ev.getAuthor().getId().equals(Config.get("OWNER_ID"))) {
        if (raw.equalsIgnoreCase(prefix + "shutdown") && ev.getAuthor().getId().equals("305757797658787841")) {
            ev.getJDA().shutdown();
            BotCommons.shutdown(ev.getJDA());
        }
    }
}
