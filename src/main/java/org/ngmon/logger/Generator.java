package org.ngmon.logger;

import org.ngmon.logger.util.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Generator {

	static InputStream LOGIN_EVENT = Generator.class.getResourceAsStream("event1.json");
	private static String loginLine;

	static InputStream DUMMY_EVENT = Generator.class.getResourceAsStream("dummyevent.json");
	private static String dummyLine;

	private static Random randomINT = new Random(System.currentTimeMillis());

	static Logger LOGBACK_LOGGER =  LoggerFactory.getLogger("org.ngmon.JEL");

	private static String[] ips = new String[]
		  { "147.251.43.10", "147.251.43.47", "147.251.43.87", "147.251.43.86",
		    "147.251.43.99", "147.251.43.127", "147.251.43.210", "147.251.43.205",
		    "147.251.43.199", "147.251.43.117", "147.251.43.110", "147.251.43.215",
		    "147.251.43.207", "147.251.43.211", "147.251.43.213", "147.251.43.124"};

	private static String[] users = new String[]{"xtovarn", "tomp", "xnovak36", "xvaseko", "xlogin1", "xlogin2", "xlogin16", "xlogin5"};

	static long lasttime = System.currentTimeMillis();

	static {
		try {
			loginLine = TestUtil.readInputStreamToString(LOGIN_EVENT);
			dummyLine = TestUtil.readInputStreamToString(DUMMY_EVENT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String getNextString(int cap) {
		return UUID.randomUUID().toString().substring(0,cap);
	}

	static long getNextTime(int maxsleep) {

		if (getNextBool(2)) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		lasttime = System.currentTimeMillis();

		return lasttime;
	}

	static boolean intToBool(int integer, int modulo) {
		return (integer % modulo) == 0;
	}

	private static String getNextIp() {
		return ips[randomINT.nextInt(16)];
	}

	private static String getNextUser() {
		return users[randomINT.nextInt(8)];
	}

	private static boolean getNextBool(int modulo) {
		return intToBool(randomINT.nextInt(), modulo);
	}

	private static String loginLine(long timestamp, String host, boolean success, String user) {
		return String.format(loginLine, timestamp, host, success, (randomINT.nextInt(99) + 100) + ".60.43." + (randomINT.nextInt(253) + 1), randomINT.nextInt(1000)+10000, user);
	}

	private static String dummyLine(long timestamp) {
		return String.format(dummyLine, timestamp, "event_" + getNextString(2), getNextIp(), getNextString(16), randomINT.nextInt(1000)+1);
	}

	static void output(String string) {
		LOGBACK_LOGGER.debug(string);
	}

	public static void main(String[] args) {

		System.out.println(new Date(lasttime));

		for (int i = 0; i < 100000; i++) {
			long nextTime = getNextTime(10);

			output(dummyLine(nextTime));

			if (intToBool(randomINT.nextInt(), 7)) {
				output(loginLine(nextTime, getNextIp(), !getNextBool(7), getNextUser()));

			}
		}

		System.out.println(new Date(lasttime));

		for (int i = 0; i < 200000; i++) {
			long nextTime = getNextTime(10);

			output(loginLine(nextTime, ips[randomINT.nextInt(8)], false, users[randomINT.nextInt(6)]));

			if (getNextBool(2)) {
				output(dummyLine(nextTime));
			}
		}

		System.out.println(new Date(lasttime));

		for (int i = 0; i < 50000; i++) {
			long nextTime = getNextTime(10);

			output(dummyLine(nextTime));

			if (intToBool(randomINT.nextInt(), 11)) {
				output(loginLine(nextTime, getNextIp(), getNextBool(3), getNextUser()));

			}
		}

		System.out.println(new Date(lasttime));
	}

}
