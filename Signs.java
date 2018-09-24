package signs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Signs extends JavaPlugin implements Listener {
	
	SignOpener SO = new SignOpener(0, 100, 0, "Fuck", "YOU", "", "nibba");
	
	@Override
	public void onEnable() {
		
		for(Player p : Bukkit.getOnlinePlayers())
			SO.inject(p);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(SO, this);
	}
	
	@EventHandler
	public void SignFinished(SignFinishedEvent e) {
		System.out.println("ok it finished?");
	}
	
	@EventHandler
	public void onJoin(PlayerInteractEvent e) {
		SO.openSign(e.getPlayer());
		System.out.println("Should have opened the SO");
	}

}
