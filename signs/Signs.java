package signs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Signs extends JavaPlugin implements Listener {
	
	SignOpener SO = new SignOpener(0, 100, 0, "This", "is", "", "annoying");
	
	@Override
	public void onEnable() {
		
		for(Player p : Bukkit.getOnlinePlayers())
			SO.inject(p);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(SO, this);
	}
	
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers())
			SO.unInject(p);
	}
	
	@EventHandler
	public void SignFinished(SignFinishedEvent e) {
		System.out.println("ok it finished?");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		SO.inject(e.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		SO.unInject(e.getPlayer());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		SO.openSign(e.getPlayer());
	}

}
