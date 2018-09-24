package signs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import signs.packets.SignOpener;
import signs.packets.Version;

public class Signs extends JavaPlugin implements Listener {
	
	SignOpener SO;
	
	@Override
	public void onEnable() {
		SO = Version.getVersion(Version.v1_8_R2, 0, 100, 0, "","^^^^^^^^^^^^^^", "Enter your", "username here");
		
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
		String[] result = e.getSignResult();
		String line1 = result[0] + "&f";
		String name = ChatColor.translateAlternateColorCodes('&', line1);
		
		if(name.length() > 16) {
			e.getPlayer().sendMessage("Name too long please only use 16 characters");
			return;
		}
		
		if(name.length() < 5) {
			e.getPlayer().sendMessage("Name too short please use 3-16 characters");
			return;
		}
		
		e.getPlayer().setDisplayName(name);
		e.getPlayer().setPlayerListName(name);
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
