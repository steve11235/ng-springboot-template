package com.fusionalliance.internal.interview.sharedutility.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public final class ReaderToStringLoader {

	/**
	 * Return a String containing the contents provided by the Reader. An attempt will be made to close the Reader, regardless if an IOException is
	 * thrown.
	 * 
	 * @param readerParm
	 *            required
	 * @return
	 * @throws IOException
	 */
	public static String readToString(final Reader readerParm) throws IOException {
		ValidationUtility.checkObjectNotNull("Reader null.", readerParm);

		final BufferedReader reader = new BufferedReader(readerParm);

		final char[] charBuffer = new char[8000];
		int charsRead = 0;
		final StringBuilder stringBuilder = new StringBuilder(8000);

		try {
			while (true) {
				charsRead = reader.read(charBuffer, 0, charBuffer.length);

				if (charsRead == -1) {
					break;
				}

				stringBuilder.append(charBuffer, 0, charsRead);
			}

			return stringBuilder.toString();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException ieo) {
				// Do nothing
			}
		}
	}

	private ReaderToStringLoader() {
		// Do nothing
	}
}
