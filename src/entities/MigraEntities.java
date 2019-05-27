package entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import application.ProgramNew;

public class MigraEntities {

	public MigraEntities() {
		super();
	}

	public List<String> returnListVerbs(String path, String tense) throws IOException {
		// TODO Auto-generated method stub

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		List<String> listOrigem = new ArrayList<>();

		listOrigem = listFoldersBetter(path);
		List<String> listVerbs = new ArrayList<String>();

		for (String list : listOrigem) {

			File[] arrayFile = null;
			File file = new File(list);

			arrayFile = file.listFiles();
			String[] fields = null;
			BufferedReader br = null;

			for (File arquivo : arrayFile) {
				if (arquivo.getName().equals(tense)) {

					br = new BufferedReader(new FileReader(arquivo));

					String line = br.readLine();

					while (line != null) {
						fields = line.split(" ");
						line = br.readLine();
						for (String list1 : fields) {
							listVerbs.add(list1);
						}
					}
				}
			}
			br.close();
		}
		sc.close();
		return listVerbs;
	}

	public int sentenceTenseAnalizerPastParticiple(String sentence, int tenseIdExt) throws IOException {

		int tenseId = tenseIdExt;

		List<String> listOrigem = new ArrayList<>();
		listOrigem = listFoldersBetter(ProgramNew.path);

		for (String list : listOrigem) {

			File[] arrayFile = null;
			File file = new File(list);
			arrayFile = file.listFiles();

			for (File arquivo : arrayFile) {
				if (arquivo.getName().endsWith("prePastParticiple.txt")) {
					BufferedReader br = new BufferedReader(new FileReader(arquivo));
					String line = br.readLine();

					while (line != null) {
						for (String verb : returnListVerbs(ProgramNew.path, "Verbs Irregular_Past_Participle.txt")) {
							if (((sentence.toLowerCase().contains(line.toLowerCase() + " ")
									&& sentence.toLowerCase().contains(verb.replaceAll(" ", "").toLowerCase()))
									| (sentence.toLowerCase().contains(line.trim().toLowerCase() + " ")
											&& sentence.toLowerCase().contains("ed"))
									| (sentence.toLowerCase().contains(
											line.trim().toLowerCase() + " " + verb.replaceAll(" ", "").toLowerCase())))
									&& tenseId != 7 & tenseId != 6 & tenseId != 4) {
								tenseId = 3;
								break;
							}
						}
						line = br.readLine();
					}
					br.close();
				}
			}
		}
		return tenseId;
	}

	public int sentenceTenseTypeAnalizer(String sentence, String fileTypeAnalizer) throws IOException {

		List<String> listOrigem = new ArrayList<>();
		listOrigem = listFoldersBetter(ProgramNew.path);
		int tenseId = 0;

		for (String list : listOrigem) {

			File[] arrayFile = null;
			File file = new File(list);
			arrayFile = file.listFiles();

			for (File arquivo : arrayFile) {
				if (arquivo.getName().endsWith(fileTypeAnalizer)) {

					BufferedReader br = new BufferedReader(new FileReader(arquivo));
					String line = br.readLine();

					while (line != null) {

						String[] fields = line.split(";");
						String auxTense1 = fields[0];
						String auxTense2 = fields[1];		

						if (sentence.toLowerCase().trim().contains(auxTense1.toLowerCase().trim()) == true) {
							if (tenseId == 0) {
								tenseId = Integer.parseInt(auxTense2.trim());
								break;
							}
						}
						line = br.readLine();
					}
					br.close();

					if (fileTypeAnalizer.equals("tense.txt")) {
						if (tenseId == 0 | tenseId == 1 | tenseId == 5) {
							tenseId = sentenceTenseAnalizerPastParticiple(sentence, tenseId);
						}

						for (String verb2 : returnListVerbs(ProgramNew.path, "Verbs Irregular_Simple_Past.txt")) {
							if (sentence.toLowerCase().contains(" " + verb2.replaceAll(" ", "").toLowerCase() + " ")) {
								if (tenseId == 0 | tenseId == 1) {
									if (sentence.toLowerCase()
											.contains("have".replaceAll(" ", "").toLowerCase()) == false) {
										if (sentence.toLowerCase()
												.contains("has".replaceAll(" ", "").toLowerCase()) == false) {
											if (sentence.toLowerCase().contains(" read ".toLowerCase()) == false)
												if (sentence.toLowerCase().contains("eed ".toLowerCase()) == false) {
													{
														tenseId = 2;
														break;
													}
												}
										}
									}
								}
							}
						}
						for (String prePronouns : returnListVerbs(ProgramNew.path, "Pronouns.txt")) {
							if ((sentence.toLowerCase().contains(prePronouns.toLowerCase() + " ")
									&& sentence.toLowerCase().contains("ed"))
									&& tenseId != 7 & tenseId != 6 & tenseId != 5 & tenseId != 4 & tenseId != 3) {
								tenseId = 2;
								break;
							}
						}

						if (tenseId == 0 | (sentence.contains("eed ") && tenseId == 0)) {
							tenseId = 1;
							break;
						}
					}
				}
			}
		}
		if (fileTypeAnalizer.contains("ModalType") & tenseId == 0) {
			tenseId = 1;
		}
		return tenseId;
	}

	public List<String> listFoldersBetter(String strPath) throws IOException {
		@SuppressWarnings("resource")
		Stream<Path> walk = Files.walk(Paths.get(strPath));
		List<String> result = walk.filter(Files::isDirectory).map(x -> x.toString()).collect(Collectors.toList());
		return result;
	}

	public String[] GetStringArray(List<String> arr) {

		String str[] = new String[arr.size()];
		for (int j = 0; j < arr.size(); j++) {
			str[j] = arr.get(j);
		}
		return str;
	}

	@SuppressWarnings("resource")
	public void executeSqlScript(Connection conn, File inputFile) {

		// Delimiter
		String delimiter = ";";

		// Create scanner
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile).useDelimiter(delimiter);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}

		// Loop through the SQL file statements
		Statement currentStatement = null;
		while (scanner.hasNext()) {

			// Get statement
			String rawStatement = scanner.next() + delimiter;

			try {
				// Execute statement
				if (!rawStatement.equals("\n\n;")) {
					currentStatement = conn.createStatement();
					currentStatement.execute(rawStatement);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// Release resources
				if (currentStatement != null) {
					try {
						currentStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				currentStatement = null;
			}
		}
	}
}
