/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of PircBotY.
 *
 * PircBotY is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * PircBotY is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * PircBotY. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircboty.hooks.events;

import javax.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.pircboty.Channel;
import org.pircboty.PircBotY;
import org.pircboty.User;
import org.pircboty.hooks.Event;
import org.pircboty.hooks.types.GenericUserModeEvent;

/**
 * Called when a user (possibly us) gets owner status granted in a channel. Note
 * that this isn't supported on all servers or may be used for something else
 * <p>
 * This is a type of mode change and therefor is also dispatched in a
 * {@link org.PircBotY.hooks.events.ModeEvent}
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerEvent<T extends PircBotY> extends Event<T> implements GenericUserModeEvent<T> {

    @Getter(onMethod = @_(
            @Override))
    protected final Channel channel;
    @Getter(onMethod = @_(
            @Override))
    protected final User user;
    @Getter(onMethod = @_(
            @Override))
    protected final User recipient;
    protected final boolean isOwner;

    /**
     * Default constructor to setup object. Timestamp is automatically set to
     * current time as reported by {@link System#currentTimeMillis() }
     *
     * @param channel The channel in which the mode change took place.
     * @param user The user that performed the mode change.
     * @param recipient The nick of the user that got owner status.
     */
    public OwnerEvent(T bot, @NonNull Channel channel, @NonNull User user, @NonNull User recipient, boolean isOwner) {
        super(bot);
        this.channel = channel;
        this.user = user;
        this.recipient = recipient;
        this.isOwner = isOwner;
    }

    /**
     * Gets the status of the mode change
     *
     * @return True if founder status was given, false if removed
     * @deprecated Use isOwner, this method being called isFounder is a bug.
     * Will be removed in future versions
     */
    @Deprecated
    public boolean isFounder() {
        return isOwner;
    }

    /**
     * Respond by send a message in the channel to the user that set the mode
     * (<b>Warning:</b> not to the user that got owner status!) in
     * <code>user: message</code> format
     *
     * @param response The response to send
     */
    @Override
    public void respond(@Nullable String response) {
        getChannel().send().message(getUser(), response);
    }
}
