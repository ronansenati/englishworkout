package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import db.DbException;
import entities.MigraEntities;

public class MigraFilesSentenceOrigin {

	public static String path = null;

	public static void main(String[] args) {

		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

		try {
			path = "C:\\Users\\ronan\\eclipse-workspace\\MigraçãoEnglishWorkOut\\Carga de dados";
			System.out.println(path);
			// String path = sc.nextLine();		
		
			MigraEntities mg = new MigraEntities();
			List<String> listOrigem = new ArrayList<>();
			listOrigem = mg.listFoldersBetter(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(path + "\\" + "MigraSentenceOrigin.csv"));

			for (String list : listOrigem) {

				File[] arrayFile = null;
				File file = new File(list);
				arrayFile = file.listFiles();

				for (File arquivo : arrayFile) {

					if (arquivo.getName().endsWith(".csv")) {
						BufferedReader br = new BufferedReader(new FileReader(arquivo));
						String line = br.readLine();						

						while (line != null) {

							String[] fields = line.split(";");

							String sentence1eng = fields[0];
							bw.write(sentence1eng);
							String sentence2port = "";
							String sentence3tips = null;

							if (fields.length >= 2) {
								sentence2port = fields[1];
								bw.write(";" + sentence2port);
							}
							if (fields.length >= 3) {
								sentence3tips = fields[2];
								bw.write(";" + sentence3tips);
							}
							bw.newLine();
							line = br.readLine();
						}
						br.close();
						bw.flush();
						
					}

				}

			}bw.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException(e.getMessage());
		}finally {
			System.out.println("THE END!");
		}
	}

}
