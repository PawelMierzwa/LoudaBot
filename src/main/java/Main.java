import Commands.Player.play;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;


public class Main extends ListenerAdapter {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IllegalArgumentException {
        JDA jda = JDABuilder.createDefault(Config.get("TOKEN"),
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.MESSAGE_CONTENT)
                .disableCache(
                        CacheFlag.EMOJI,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS)
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        // These commands take up to an hour to be activated after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("help", "Lists all the supported commands")
        );

        commands.addCommands(
                Commands.slash("user", "Show information about a user")
                        .addOptions(new OptionData(USER, "user", "Provide user to check")
                                .setRequired(true))
        );

        commands.addCommands(
                Commands.slash("say", "Make the bot say what you tell it to")
                        .addOptions(new OptionData(STRING, "content", "What the bot should say")
                                .setRequired(true))
        );

        commands.addCommands(
                Commands.slash("play", "Play a song")
                        .addOptions(new OptionData(STRING, "song", "Search for a song you want to play (or link it)")
                                .setRequired(true))
        );

        commands.addCommands(
                Commands.slash("skip", "Skip current song.")
        );

        commands.addCommands(
                Commands.slash("queue", "Show current song queue")
        );

        commands.addCommands(
                Commands.slash("nowplaying", "Display current playing track.")
        );

        commands.addCommands(
                Commands.slash("loop", "Loop current song"
                ).addOptions(new OptionData(STRING, "type", "One - Loop only the current track, All - Loop whole queue")
                        .addChoice("one", "1")
                        .addChoice("all", "2")
                        .setRequired(true)
                ).addOptions(new OptionData(BOOLEAN, "loop", "True/False")
                        .setRequired(true))
        );

        commands.addCommands(
                Commands.slash("join", "Make the bot join the voice channel")
        );

        commands.addCommands(
                Commands.slash("leave", "Make the bot leave the voice channel")
        );

        commands.addCommands(
                Commands.slash("stop", "Stop the current song.")
        );

        commands.addCommands(
                Commands.slash("resume", "Unpauses the stopped track.")
        );

        commands.addCommands(
                Commands.slash("clear", "Clear the song queue.")
        );

        commands.addCommands(
                Commands.slash("prune", "Prune messages from this channel (Requires permissions)")
                        .addOptions(new OptionData(INTEGER, "amount", "How many messages to prune (Default 10)")
                                .setRequiredRange(2, 25))
        );

        commands.addCommands(
                Commands.slash("ban", "Ban a user from this server. Requires permission to ban users.")
                        .addOptions(new OptionData(USER, "user", "The user to ban") // USER type allows including members of the server or other users by id
                                .setRequired(true)) // This command requires a parameter
                        .addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days.")) // This is optional
        );

        commands.addCommands(
                Commands.user("Ban")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                        .setName("ban")
                        .setGuildOnly(true)
        );

        commands.addCommands(
                Commands.slash("coinflip", "Flip a coin")
        );

        commands.addCommands(
                Commands.slash("roll", "Roll a dice")
                        .addOptions(new OptionData(INTEGER, "max_number", "Highest possible number")
                                .setRequiredRange(1, 100)) //default 6
                        .addOptions(new OptionData(INTEGER, "amount", "How many rolls you want")
                                .setRequiredRange(1, 5)) //default 1
        );

        commands.addCommands(
                Commands.slash("randomemoji", "Sends a random emoji")
        );

        commands.addCommands(
                Commands.slash("ping", "Checks the latency of the response")
        );

        commands.addCommands(
                Commands.slash("randomnumber", "Responds with a random number between a given range")
                        .addOptions(new OptionData(INTEGER, "max", "Highest possible number")
                                .setRequired(true))
        );

        commands.addCommands(
                Commands.slash("randomdoge", "Sends random \"funny dog\" picture")
        );

        commands.addCommands(
                Commands.slash("serverinfo", "Shows information about a current server")
        );

        commands.queue();
        jda.updateCommands();

        jda.getPresence().setStatus(OnlineStatus.IDLE);
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.COMPETING, "Stupidity Contest"));
        jda.addEventListener(new ready());
        jda.addEventListener(new slashCommands());
        jda.addEventListener(new play());
    }
}

