package com.emailnotifications;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("emailnotifications")
public interface EmailNotificationsConfig extends Config
{
	@ConfigItem(
			keyName = "email",
			name = "Email address",
			description = "The email address of the sender and recipient."
	)
	default String email() { return ""; }

	@ConfigItem(
			keyName = "password",
			name = "Email password",
			description = "The email password of the sender.",
			secret = true
	)
	default String password() { return ""; }

	@ConfigItem(
			keyName = "moreInfo",
			name = "More Info",
			description = "Click the link to see more information to resolve your errors",
			warning = "Click \"No\" to prevent editing of link."
	)
	default String moreInfo() { return "https://github.com/boiwantlearncode/email-notifications#readme"; }
}
