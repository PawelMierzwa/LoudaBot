import Commands.Goofy.*;
import Commands.Misc.ping;
import Commands.Misc.random;
import Commands.Moderation.ban;
import Commands.Moderation.chkuser;
import Commands.Moderation.prune;
import Commands.Moderation.serverinfo;
import Commands.Player.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@SuppressWarnings("ConstantConditions")
public class slashCommands extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        //noinspection EnhancedSwitchMigration
        switch (event.getName())
        {
            case "ban":
                Member member = event.getOption("user").getAsMember(); // the "user" option is required so it doesn't need a null-check here
                User user = event.getOption("user").getAsUser();
                ban.banCommand(event, user, member);
                break;
            case "user":
                chkuser.userCommand(event, event.getOption("user").getAsUser());
                break;
            case "say":
                say.sayCommand(event, event.getOption("content").getAsString()); // content is required so no null-check here
                break;
            case "prune": // 2 stage command with a button prompt
                prune.pruneCommand(event);
                break;
            case "join": //vc shit ye
                join.joinCommand(event);
                break;
            case "leave":
                leave.leaveCommand(event);
                break;
            case "play":
                String song = event.getOption("song").getAsString();
                play.playCommand(event, song);
                break;
            case "stop":
                stop.stopCommand(event);
                break;
            case "clear":
                clear.clearCommand(event);
                break;
            case "skip":
                skip.skipCommand(event);
                break;
            case "resume":
                resume.resumeCommand(event);
                break;
            case "nowplaying":
                nowplaying.nowplayingCommand(event);
                break;
            case "queue":
                queue.queueCommand(event);
                break;
            case "loop":
                loop.loopCommand(event, event.getOption("loop").getAsBoolean(), event.getOption("type").getAsInt());
                break;
            case "roll":
                diceroll.rollCommand(event, event.getOption("max_number"), event.getOption("amount"));
                break;
            case "coinflip":
                coinflip.coinflipCommand(event);
                break;
            case "randomemoji":
                remoji.randomemojiCommand(event);
                break;
            case "ping":
                ping.pingCommand(event);
                break;
            case "randomnumber":
                random.randomNumberCommand(event, event.getOption("max"));
                break;
            case "randomdoge":
                doge.dogeCommand(event);
                break;
            case "serverinfo":
                serverinfo.serverInfoCommand(event);
                break;
        }
    }
}