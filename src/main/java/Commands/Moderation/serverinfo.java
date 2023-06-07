package Commands.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class serverinfo {
    public static void serverInfoCommand(SlashCommandInteractionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/ HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        EmbedBuilder servEmbed = new EmbedBuilder();
        servEmbed.setColor(Color.GREEN);
        servEmbed.setTitle("Current server information");
        if (event.getGuild().getIconUrl() == null) return;
        servEmbed.setThumbnail(event.getGuild().getIconUrl());
        servEmbed.addField("Name", "**" + event.getGuild().getName() + "**", false);
        try{
            String ownerName = Objects.requireNonNull(event.getGuild().getOwner()).getEffectiveName();
            servEmbed.addField("Owner", "**" + ownerName + "**", false);
        } catch (NullPointerException exception) {
            servEmbed.addField("Owner", "**None**", false);
        }

        servEmbed.addField("Created on", "**" + event.getGuild().getTimeCreated().format(formatter) + "**", false);
        servEmbed.addField("Members count", "**" + event.getGuild().getMemberCount() + "**", true);
        servEmbed.addField("Booster count", "**" + event.getGuild().getBoostCount() + "**", true);
        servEmbed.addField("Boost Tier", "**" + event.getGuild().getBoostTier() + "**", true);
        servEmbed.addField("Explicit content level", "**" + event.getGuild().getExplicitContentLevel() + "**", false);
        servEmbed.addField("Verification Level", "**" + event.getGuild().getVerificationLevel() + "**", false);

        servEmbed.addField("Channels",
                "Text channels: " + "**" + event.getGuild().getTextChannels().size() + "**" +
                        "\nVoice channels: " + "**" + event.getGuild().getVoiceChannels().size() + "**" +
                        "\nCategories: " + "**" + event.getGuild().getCategories().size() + "**", false);
        try {
            //noinspection ConstantConditions
            servEmbed.addField("AFK Channel: ", event.getGuild().getAfkChannel().getName(), false);
        } catch (NullPointerException exception) {
            servEmbed.addField("AFK Channel: ", "**None**", false);
        }
        servEmbed.setFooter(simpleDateFormat.format(date));
        event.replyEmbeds(servEmbed.build()).queue();
    }
}
