package signs.packets.v1_8_R2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R2.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R2.PlayerConnection;
import signs.SignFinishedEvent;
import signs.packets.SignOpener;

public class SignOpener_v1_8_R2 extends SignOpener implements Listener{
	
	double x;
	double y;
	double z;

	String line1;
	String line2;
	String line3;
	String line4;

	String[] result;

	public SignOpener_v1_8_R2(double x, double y, double z, String line1, String line2, String line3, String line4) {

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
			signBlock.setType(Material.WALL_SIGN);
		}
		
		Sign sign = (Sign) signBlock.getState();
		sign.setLine(0, line1);
		sign.setLine(1, line2);
		sign.setLine(2, line3);
		sign.setLine(3, line4);
		sign.update(true);

		PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(new BlockPosition(x, y, z));
		sendPacket(packet, player);

	}
	
	public void inject(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
			
			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
				
				if(packet instanceof PacketPlayInUpdateSign) {
					PacketPlayInUpdateSign ppius = (PacketPlayInUpdateSign) packet;
					String[] text = new String[] {
							ppius.b()[0].getText(),
							ppius.b()[1].getText(),
							ppius.b()[2].getText(),
							ppius.b()[3].getText()
					};
					Bukkit.getServer().getPluginManager().callEvent(new SignFinishedEvent(player.getName(), text));
				}
				super.channelRead(context, packet);
			}
			
			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) throws Exception {
				super.write(context, packet, channelPromise);
			}
		};
		
		ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.k.pipeline();
		pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
	}

	private void sendPacket(Packet<?> packet, Player p) {
		/*
		 * Getting the player connection and sending the packet
		 */
		PlayerConnection playerConnection = ((CraftPlayer)p).getHandle().playerConnection;
		playerConnection.sendPacket(packet);
	}

	public void unInject(Player player) {
		ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.k.pipeline();
		pipeline.remove(player.getName());
	}

}
