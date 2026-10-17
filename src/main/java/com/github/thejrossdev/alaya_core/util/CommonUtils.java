package com.github.thejrossdev.alaya_core.util;

import com.github.thejrossdev.alaya_core.Alaya_core;

public class CommonUtils {

	public class Utils {
		public static long getServerTick() {
			return Alaya_core.OVERWORLD.getGameTime();
		}

		public static String getTimeFromTicks(float ticks, int decimalPlaces) {
			float ticks_to_seconds = 20;
			float seconds_to_minutes = 60;
			String affix = "s";
			float time = ticks / ticks_to_seconds;
			if (time > seconds_to_minutes) {
				time /= seconds_to_minutes;
				affix = "m";
			}
			return stringTruncation(time, decimalPlaces) + affix;
		}

		public static String stringTruncation(double f, int decimalPlaces) {
			if (f == Math.floor(f)) {
				return Integer.toString((int) f);
			}

			double multiplier = Math.pow(10, decimalPlaces);
			double truncatedValue = Math.floor(f * multiplier) / multiplier;

			// Convert the truncated value to a string
			String result = Double.toString(truncatedValue);

			// Remove trailing zeros
			result = result.replaceAll("0*$", "");

			// Remove the decimal point if there are no decimal places
			result = result.endsWith(".") ? result.substring(0, result.length() - 1) : result;

			return result;
		}

		public static String translateModKey(String key) {
			return Alaya_core.MODID + "." + key;
		}
		public static String translateModKey(String pre, String key) {
			return pre + "." + Alaya_core.MODID + "." + key;
		}
	}

}
