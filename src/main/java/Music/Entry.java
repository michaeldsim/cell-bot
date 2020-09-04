package Music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.User;

public class Entry {

	AudioTrack track;
	User user;

	public Entry(AudioTrack track, User user) {
		this.track = track;
		this.user = user;
	}

	public AudioTrack getTrack() {
		return track;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return " `[" + getTimestamp(track.getDuration()) + "]`" + track.getInfo().title;

	}

	private static String getTimestamp(long milliseconds) {
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

		if (hours > 0)
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		else
			return String.format("%02d:%02d", minutes, seconds);
	}

}
