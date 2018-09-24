package signs.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class SignOpener implements Listener {
	
	public abstract void openSign(Player p);
	
	public abstract void inject(Player p);
	
	public abstract void unInject(Player player);

}
