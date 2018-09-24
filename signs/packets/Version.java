package signs.packets;

import signs.packets.v1_8_R2.SignOpener_v1_8_R2;
import signs.packets.v1_8_R3.SignOpener_v1_8_R3;

public enum Version {
	
	v1_8_R3,v1_8_R2;
	
	private Version() {
		
	}
	
	public static SignOpener getVersion(Version version, double x, double y, double z, String line1, String line2, String line3, String line4) {
		SignOpener returner = version == Version.v1_8_R2 ? new SignOpener_v1_8_R2(x, y, z, line1, line2, line3, line4) : new SignOpener_v1_8_R3(version, x, y, z, line1, line2, line3, line4);
		return returner;
	}

}
