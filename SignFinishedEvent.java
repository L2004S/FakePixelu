package signs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignFinishedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	String p;
	String[] result;
	
	public SignFinishedEvent(String p, String[] result) {
		this.p = p;
		this.result = result;
	}
	
	public String[] getSignResult() {
		return result;
	}
	
	public String getPlayerName() {
		return p;
	}
		 
	public Player getPlayer() throws NullPointerException {
		return Bukkit.getServer().getPlayer(p);
	}
	
	@Override
	public HandlerList getHandlers() {
	return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	return handlers;
	}

}
