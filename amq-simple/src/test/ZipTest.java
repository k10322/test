package test;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

public class ZipTest {

	@Test
	public void zipTest() throws FileNotFoundException, IOException {

		System.out.println(" read file ");
		// System.out.println(" readFile(./src/test/input.txt) "+readFile("./src/test/input.txt"));

		System.out.println(" Zip Method  ");

		// System.out.println(zipMethod(readFile("./src/test/input.txt")));

		byte[] byteToSend = zipMethod(readFile("./src/test/input.txt"));

		writeintofile(byteToSend.toString(),"_zipped_file");
		
		writeintofile(BytesUnZipStringRead(byteToSend),"_unzipped_file");

	}

	void writeintofile(String content,String name){
		
		
		File file = new File("./src/test/filename"+name+".txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(content);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done");
		
	}
	private String BytesUnZipStringRead(byte[] bytes)
			throws FileNotFoundException, IOException {

		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(
				bytes));

		ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
		int bytesRead;
		byte[] tempBuffer = new byte[8192 * 2];
		ZipEntry entry = (ZipEntry) zipStream.getNextEntry();
		try {
			while ((bytesRead = zipStream.read(tempBuffer)) != -1) {
				streamBuilder.write(tempBuffer, 0, bytesRead);
			}
		} catch (IOException e) {

		}
		return streamBuilder.toString();
	}

	private byte[] zipMethod(String str) {
		System.out.println("  str   len  " + str.length());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// System.out.println("  baos  111    "+baos);
		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			ZipEntry entry = new ZipEntry("test_1234567888888888.txt");
			zos.putNextEntry(entry);
			System.out
					.println("  str.getBytes() len  " + str.getBytes().length);
			zos.write(str.getBytes());
			zos.closeEntry();
		} catch (IOException ioe) {
		}
		System.out.println("  baos   " + baos.size());
		return baos.toByteArray();
	}

	public String readFile(String filename) {
		String content = null;
		File file = new File(filename); // for ex foo.txt
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
