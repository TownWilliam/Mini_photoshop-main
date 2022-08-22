package src.component.util;


public class ImageUtil {
	
	/** Embed color into the rgb array **/
	public static int[] decodeColor(int color, int rgb[]) {
		if(rgb == null) rgb = new int[3];
		rgb[0] = (color & 0x00ff0000) >> 16;
		rgb[1] = (color & 0x0000ff00) >> 8;
		rgb[2] = (color & 0x000000ff);	
		return rgb;
	}
	
	/** Decode a rgb array into a 32-bit Integer num **/
	public static int encodeColor(int rgb[]) {
		int color = (255 << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
		return color;
	}
	
}