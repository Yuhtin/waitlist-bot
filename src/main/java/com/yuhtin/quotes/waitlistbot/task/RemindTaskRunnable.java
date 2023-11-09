package com.yuhtin.quotes.waitlistbot.task;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.model.RemindUser;
import com.yuhtin.quotes.waitlistbot.repository.RemindRepository;
import lombok.AllArgsConstructor;

import java.util.TimerTask;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class RemindTaskRunnable extends TimerTask {

    private final WaitlistBot instance;

    @Override
    public void run() {
        for (RemindUser remindUser : RemindRepository.instance().selectAll()) {
            TaskHelper.runAsync(() -> {
                if (remindUser.remindMillis() > System.currentTimeMillis()) return;

                instance.getJda().retrieveUserById(remindUser.userId()).queue(user -> {
                    user.openPrivateChannel().queue(channel -> {
                        String text = instance.getConfig().getRemindMessage()
                                .replace("%user%", user.getAsMention())
                                .replace("%waitlistchannel%", String.valueOf(instance.getConfig().getWaitlistChatChannelId()));

                        channel.sendMessage(text).queue();
                        instance.getLogger().info("→ Sent remind message to " + user.getName() + " successfully!");
                    });
                });

                RemindRepository.instance().delete(remindUser.userId());
            });
        }
    }
}
