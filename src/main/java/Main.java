import Commands.Player.play;
import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.*;
import dev.arbjerg.lavalink.client.loadbalancing.builtin.VoiceRegionPenaltyProvider;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Config;
import utils.JDAListener;
import utils.ready;
import Commands.slashCommands;

import java.util.List;
import java.util.Optional;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;
import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;


public class Main extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int SESSION_INVALID = 4006;

    private static JDAListener listener;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws InterruptedException {
        final LavalinkClient client = new LavalinkClient(
                Helpers.getUserIdFromToken(Config.get("TOKEN"))
        );

        client.getLoadBalancer().addPenaltyProvider(new VoiceRegionPenaltyProvider());

        registerLavalinkListeners(client);
        registerLavalinkNodes(client);

        listener = new JDAListener(client);

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
                .setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(client))
                .build();
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

        client.on(WebSocketClosedEvent.class).subscribe((event) -> {
            if (event.getCode() == SESSION_INVALID) {
                final var guildId = event.getGuildId();
                final var guild = jda.getGuildById(guildId);

                if (guild == null) {
                    return;
                }

                final var connectedChannel = guild.getSelfMember().getVoiceState().getChannel();

                // somehow
                if (connectedChannel == null) {
                    return;
                }

                jda.getDirectAudioController().reconnect(connectedChannel);
            }
        });
    }

    private static void registerLavalinkNodes(LavalinkClient client) {
        List.of(
                client.addNode(
                        new NodeOptions.Builder()
                                .setName("localhost")
                                .setServerUri("ws://localhost")
                                .setPassword("chujek123")
                                .build()
                )
        ).forEach((node) -> {
            node.on(TrackStartEvent.class).subscribe((event) -> {
                final LavalinkNode node1 = event.getNode();

                LOG.trace(
                        "{}: track started: {}",
                        node1.getName(),
                        event.getTrack().getInfo()
                );
            });
        });
    }

    private static void registerLavalinkListeners(LavalinkClient client) {
        client.on(ReadyEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' is ready, session id is '{}'!",
                    node.getName(),
                    event.getSessionId()
            );
        });

        client.on(StatsEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' has stats, current players: {}/{} (link count {})",
                    node.getName(),
                    event.getPlayingPlayers(),
                    event.getPlayers(),
                    client.getLinks().size()
            );
        });

        client.on(TrackStartEvent.class).subscribe((event) -> {
            Optional.ofNullable(JDAListener.musicManagers.get(event.getGuildId())).ifPresent(
                    (mng) -> mng.scheduler.onTrackStart(event.getTrack())
            );
        });

        client.on(TrackEndEvent.class).subscribe((event) -> {
            Optional.ofNullable(JDAListener.musicManagers.get(event.getGuildId())).ifPresent(
                    (mng) -> mng.scheduler.onTrackEnd(event.getTrack(), event.getEndReason())
            );
        });

        client.on(EmittedEvent.class).subscribe((event) -> {
            if (event instanceof TrackStartEvent) {
                LOG.info("Track start event");
            }

            final var node = event.getNode();

            LOG.info(
                    "Node '{}' emitted event: {}",
                    node.getName(),
                    event
            );
        });
    }
}