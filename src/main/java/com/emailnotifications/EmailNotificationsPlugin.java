package com.emailnotifications;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.mail.*;
import net.runelite.client.callback.ClientThread;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NotificationFired;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Slf4j
@PluginDescriptor(
		name = "Email Notifications",
		description = "Sends an email on notification event.",
		tags = {"email", "notification"},
		loadWhenOutdated = true
)
public class EmailNotificationsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EmailNotificationsConfig config;

	@Provides
	EmailNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EmailNotificationsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onNotificationFired(NotificationFired notificationFired) {
		log.info(notificationFired.getMessage());
		sendMail(notificationFired.getMessage());
	}

	public void sendMail(String message) {
		log.info("sendMail() function called.");

		final String fromEmail = config.email(); // requires valid gmail id
		final String fromPassword = config.password(); // correct password for gmail id
		final String toEmail = config.email();


		String title;
		Player player = client.getLocalPlayer();
		if (player == null) {
			title = "RuneLite";
		} else {
			String name = player.getName();
			if (Strings.isNullOrEmpty(name)) {
				title = "RuneLite";
			} else {
				title = "RuneLite - " + name;
			}
		}


		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, fromPassword);
			}
		};
		Session session = Session.getInstance(props, auth);
		log.info("Session okay.");

		ExecutorService executor = Executors.newSingleThreadExecutor();

		try {
			executor.submit(() ->
			{
				try {
					EmailUtil.sendEmail(session, toEmail, title, message);
				} catch (Exception e) {
					log.info(e.toString());
				}
			});
			executor.shutdownNow();
		} catch (Exception e) {
			log.info("Exception with new thread.");
		}

	};

}
