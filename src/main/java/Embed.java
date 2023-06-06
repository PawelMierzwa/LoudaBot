import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Embed extends ListenerAdapter {


    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/ HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(Main.prefix + "serverinfo")) {
            EmbedBuilder servEmbed = new EmbedBuilder();
            servEmbed.setColor(Color.GREEN);
            servEmbed.setTitle("Current server information");
            servEmbed.setThumbnail(e.getGuild().getIconUrl());
            servEmbed.addField("Name", "**" + e.getGuild().getName() + "**", false);
            try{
                String ownerName = Objects.requireNonNull(e.getGuild().getOwner()).getEffectiveName();
                servEmbed.addField("Owner", "**" + ownerName + "**", false);
            } catch (NullPointerException exception) {
                servEmbed.addField("Owner", "**None**", false);
            }

            servEmbed.addField("Created on", "**" + e.getGuild().getTimeCreated().format(formatter) + "**", false);
            servEmbed.addField("Members count", "**" + e.getGuild().getMemberCount() + "**", true);
            servEmbed.addField("Booster count", "**" + e.getGuild().getBoostCount() + "**", true);
            servEmbed.addField("Boost Tier", "**" + e.getGuild().getBoostTier() + "**", true);
            servEmbed.addField("Explicit content level", "**" + e.getGuild().getExplicitContentLevel() + "**", false);
            servEmbed.addField("Verification Level", "**" + e.getGuild().getVerificationLevel() + "**", false);


            servEmbed.addField("Channels",
                    "Text channels: " + "**" + e.getGuild().getTextChannels().size() + "**" +
                    "\nVoice channels: " + "**" + e.getGuild().getVoiceChannels().size() + "**" +
                    "\nCategories: " + "**" + e.getGuild().getCategories().size() + "**", false);
            try {
                //noinspection ConstantConditions
                servEmbed.addField("AFK Channel: ", e.getGuild().getAfkChannel().getName(), false);
            } catch (NullPointerException exception) {
                servEmbed.addField("AFK Channel: ", "**None**", false);
            }
            servEmbed.setFooter(simpleDateFormat.format(date));
            e.getChannel().sendMessageEmbeds(servEmbed.build()).queue();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "doge")) {
            String[] dogeImg = {
                    "https://i.pinimg.com/originals/f8/af/98/f8af98c78198528ebc79a414fae051a9.jpg",
                    "https://getmemetemplates.com/wp-content/uploads/2020/06/Doggo-38.jpg",
                    "https://i.kym-cdn.com/entries/icons/original/000/013/564/doge.jpg",
                    "https://i.kym-cdn.com/photos/images/newsfeed/001/582/307/98c.jpg",
                    "https://www.meme-arsenal.com/memes/b282bb90aa06c62a7450acaf4a182ad1.jpg",
                    "https://www.meme-arsenal.com/memes/3bc173ad281f1baa9902f5c4b8de480d.jpg",
                    "https://hg1.funnyjunk.com/pictures/My+additions+to+dogelore+big1improved+kid+doge+with+propeller+beaniebig1_71500b_7366988.jpg",
                    "https://www.cryptonewsz.com/wp-content/uploads/2019/02/Dogecoin-DOGE-Price-Analysis-Feb.06.jpg"
            };
            Random dogeRand = new Random();
            Color dogeYellow = new Color(255, 204, 153);

            int dogeImgRand = dogeRand.nextInt(7);
            EmbedBuilder dogeEmbed = new EmbedBuilder();
            dogeEmbed.setColor(dogeYellow);
            dogeEmbed.setImage(dogeImg[dogeImgRand]);
            dogeEmbed.setFooter(simpleDateFormat.format(date));
            e.getChannel().sendMessageEmbeds(dogeEmbed.build()).queue();
        }
    }
}
