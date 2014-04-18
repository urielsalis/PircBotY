package net.ae97.pircboty;

import com.google.common.collect.ImmutableSortedSet;
import java.util.UUID;
import net.ae97.pircboty.hooks.WaitForQueue;
import net.ae97.pircboty.hooks.events.WhoisEvent;
import net.ae97.pircboty.output.OutputUser;
import net.ae97.pircboty.snapshot.UserSnapshot;
import org.apache.commons.lang3.concurrent.AtomicSafeInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;

public class User implements Comparable<User> {

    private final PircBotY bot;
    private final UserChannelDao dao;
    private final UUID userId = UUID.randomUUID();
    private final AtomicSafeInitializer<OutputUser> output = new AtomicSafeInitializer<OutputUser>() {
        @Override
        protected OutputUser initialize() {
            return bot.getConfiguration().getBotFactory().createOutputUser(bot, User.this);
        }
    };
    private String nick;
    private String realName = "";
    private String login = "";
    private String hostmask = "";
    private String awayMessage = null;
    private boolean ircop = false;
    private String server = "";
    private int hops = 0;

    protected User(PircBotY bot, UserChannelDao dao, String nick) {
        this.bot = bot;
        this.dao = dao;
        this.nick = nick;
    }

    public OutputUser send() {
        try {
            return output.get();
        } catch (ConcurrentException ex) {
            throw new RuntimeException("Could not generate OutputChannel for " + getNick(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean isVerified() {
        try {
            bot.sendRaw().rawLine("WHOIS " + getNick() + " " + getNick());
            WaitForQueue waitForQueue = new WaitForQueue(bot);
            while (true) {
                WhoisEvent event = waitForQueue.waitFor(WhoisEvent.class);
                if (!event.getNick().equals(nick)) {
                    continue;
                }
                waitForQueue.close();
                return event.getRegisteredAs() != null && !event.getRegisteredAs().isEmpty();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Couldn't finish querying user for verified status", ex);
        }
    }

    public UserSnapshot createSnapshot() {
        return new UserSnapshot(this);
    }

    public ImmutableSortedSet<UserLevel> getUserLevels(Channel channel) {
        return getDao().getLevels(channel, this);
    }

    public ImmutableSortedSet<Channel> getChannels() {
        return getDao().getChannels(this);
    }

    public ImmutableSortedSet<Channel> getChannelsOpIn() {
        return getDao().getChannels(this, UserLevel.OP);
    }

    public ImmutableSortedSet<Channel> getChannelsVoiceIn() {
        return getDao().getChannels(this, UserLevel.VOICE);
    }

    public ImmutableSortedSet<Channel> getChannelsOwnerIn() {
        return getDao().getChannels(this, UserLevel.OWNER);
    }

    public ImmutableSortedSet<Channel> getChannelsHalfOpIn() {
        return getDao().getChannels(this, UserLevel.HALFOP);
    }

    public ImmutableSortedSet<Channel> getChannelsSuperOpIn() {
        return getDao().getChannels(this, UserLevel.SUPEROP);
    }

    @Override
    public int compareTo(User other) {
        return getNick().compareToIgnoreCase(other.getNick());
    }

    public String getServer() {
        return server;
    }

    public int getHops() {
        return hops;
    }

    public boolean isAway() {
        return awayMessage != null;
    }

    public PircBotY getBot() {
        return bot;
    }

    public UserChannelDao getDao() {
        return dao;
    }

    public UUID getUserId() {
        return userId;
    }

    public AtomicSafeInitializer<OutputUser> getOutput() {
        return output;
    }

    public String getNick() {
        return nick;
    }

    public String getRealName() {
        return realName;
    }

    public String getLogin() {
        return login;
    }

    public String getHostmask() {
        return hostmask;
    }

    public String getAwayMessage() {
        return awayMessage;
    }

    public boolean isIrcop() {
        return ircop;
    }

    protected void setNick(String nick) {
        this.nick = nick;
    }

    protected void setRealName(String realName) {
        this.realName = realName;
    }

    protected void setLogin(String login) {
        this.login = login;
    }

    protected void setHostmask(String hostmask) {
        this.hostmask = hostmask;
    }

    protected void setAwayMessage(String awayMessage) {
        this.awayMessage = awayMessage;
    }

    protected void setIrcop(boolean ircop) {
        this.ircop = ircop;
    }

    protected void setServer(String server) {
        this.server = server;
    }

    protected void setHops(int hops) {
        this.hops = hops;
    }
}