package com.sljm.svnadmin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 执行linux命令
 * @author lisy
 *
 */
public class Shell {
	public static String doShell(String cmdStr) throws IOException {
		String[] cmd = new String[] { "/bin/sh", "-c", cmdStr };
		Process ps = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		String result = sb.toString();
		return result;
	}
}
