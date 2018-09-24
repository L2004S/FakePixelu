package signs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ListenerExample implements Listener{

	@EventHandler
	public void onSignResult(SignFinishedEvent e) {
		String[] result = e.getSignResult();
		String line1 = result[0];
		String line2 = result[1];
		String line3 = result[2];
		String line4 = result[3];
		
		System.out.println(line1);
		System.out.println(line2);
		System.out.println(line3);
		System.out.println(line4);
		System.out.println(e.getPlayerName());
	}
	
}
