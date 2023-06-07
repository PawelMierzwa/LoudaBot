package Commands.Goofy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class doge {
    public static void dogeCommand(SlashCommandInteractionEvent event) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

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
        event.replyEmbeds(dogeEmbed.build()).queue();
    }
}
