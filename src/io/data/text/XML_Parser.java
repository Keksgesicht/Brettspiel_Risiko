package io.data.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Braun
 */
class XML_Parser {

	private File file;
	private String lines;

	XML_Parser(File file, boolean read) throws IOException {
		this.file = file;
		if (read)
			lines = read();

	}

	void write() throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(file)) {
			out.println(lines);
		}
	}

	void setAttributes(Map<String, String> attributes) {
		// TODO
	}

	void setChild(String child) {
		// TODO
	}

	void setNextEntry(String entry) {
		// TODO
	}

	String read() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	Map<String, String> getAttributes() {
		return null;
		// TODO
	}

	String getChild() {
		return null;
		// TODO
	}

	String getNextEntry() {
		return null;
		// TODO
	}


}
