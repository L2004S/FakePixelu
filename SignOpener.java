package signs;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class SignOpener implements Listener{
	
	HashMap<Player, ChannelDuplexHandler> injections = new HashMap<>();
	
	double x;
	double y;
	double z;

	String line1;
	String line2;
	String line3;
	String line4;

	String[] result;

	public SignOpener(double x, double y, double z, String line1, String line2, String line3, String line4) {

		/*
		 * Saving all the variables into the Sign specific class
		 */

		this.x = x;
		this.y = y;
		this.z = z;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.line4 = line4;
	}

	public void openSign(Player player) {
		System.out.println("Opening sign for " + player.getName());
		Block signBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), x, y, z));

		if(!(signBlock.getState() instanceof Sign)) {
			System.out.println("Something went wrong?");
			signBlock.setType(Material.WALL_SIGN);
		}
		
		Sign sign = (Sign) signBlock.getState();
		sign.setLine(0, line1);
		sign.setLine(1, line2);
		sign.setLine(2, line3);
		sign.setLine(3, line4);
		sign.update();

		PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(new BlockPosition(x, y, z));
		sendPacket(packet, player);

	}
	
	public void inject(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
			
			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) throws Exception {
				
				if(packet instanceof PacketPlayInUpdateSign) {
					PacketPlayInUpdateSign pius = (PacketPlayInUpdateSign) packet;
				
					System.out.println("READ>> " + pius.b().toString());
				
				}
				
				super.write(context, packet, channelPromise);
			}
		};
		
		ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
		pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
	}

	@EventHandler
	public void playerClosedSign(SignChangeEvent se) {
		if(se.getBlock().getLocation().equals(new Location(se.getPlayer().getWorld(), x, y, z))) {
			System.out.println("Result is line1: " + se.getLines()[0] + "  line2:" + se.getLines()[1] + "  line3:" + se.getLines()[2] + "  line4:" + se.getLines()[3]);
			result = se.getLines();
			se.setCancelled(true);

			/*
			 * Calls event you should be listening to
			 */
			SignFinishedEvent e = new SignFinishedEvent(se.getPlayer().getName(), result);
			Bukkit.getServer().getPluginManager().callEvent(e);

		}
	}

	private void sendPacket(Packet<?> packet, Player p) {
		/*
		 * Getting the player connection and sending the packet
		 */
		PlayerConnection playerConnection = ((CraftPlayer)p).getHandle().playerConnection;
		playerConnection.sendPacket(packet);
	}

}
