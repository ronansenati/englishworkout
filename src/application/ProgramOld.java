package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import db.DB;
import db.DbException;
import entities.MigraEntities;

public class ProgramOld {

	public static void main(String[] args) {

		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		PreparedStatement st3 = null;

		try {
			System.out.println("Enter file full path migration files and script to generation your base:");
			String path = "C:\\Users\\ronan\\eclipse-workspace\\MigraçãoEnglishWorkOut\\Carga de dados";
			System.out.println(path);
			// String path = sc.nextLine();

			entities.ServiceWinStartStop.startService("MySql80");
			conn = DB.getConnection();
			conn.setAutoCommit(false);
			Locale.setDefault(Locale.US);
			Scanner sc = new Scanner(System.in);
			MigraEntities mg = new MigraEntities();
			List<String> listOrigem = new ArrayList<>();

			listOrigem = mg.listFoldersBetter(path);

			BufferedWriter bw = new BufferedWriter(new FileWriter(path + "\\" + "logOld.txt"));

			for (String list : listOrigem) {

				File[] arrayFile = null;
				File file = new File(list);

				arrayFile = file.listFiles();

				for (File arquivo : arrayFile) {
					if (arquivo.getName().endsWith("ScriptEnglishWorkOut _Old.sql")) {
						mg.executeSqlScript(conn, arquivo);
					}
				}
				for (File arquivo : arrayFile) {

					if (arquivo.getName().endsWith(".csv")) {

						BufferedReader br = new BufferedReader(new FileReader(arquivo));
						String line = br.readLine();

						String resultString = " ";

						while (line != null) {

							String[] fields = line.split(";");

							String sentence1eng = fields[0];
							String sentence2port = "";
							String sentence3tips = null;

							if (fields.length >= 2) {
								sentence2port = fields[1];
							}
							if (fields.length >= 3) {
								sentence3tips = fields[2];
							}
							// testes
							if (sentence1eng.contains("I’d bought a new car.")) {
								System.out.println(sentence1eng);

							}

							PreparedStatement st5 = conn.prepareStatement(
									"SELECT * FROM englishworkout.sentence where sentence=\"" + sentence1eng + "\"");

							ResultSet resultSet1 = st5.executeQuery(
									"SELECT * FROM englishworkout.sentence where sentence=\"" + sentence1eng + "\"");
							// idcategorysentence
							if (resultSet1 != null && resultSet1.next())
								resultString = resultSet1.getString("sentence").toString();

							if (!sentence1eng.toLowerCase().equals(resultString.replaceAll(" ", "").toLowerCase())) {

								if (sentence2port.replaceAll(" ", "").toLowerCase().equals("")) {
									st = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st.setString(1, sentence1eng);
									st.setString(2, sentence3tips);

									// idcategorysentence
									int control = 0;

									if (sentence1eng.contains("could") | sentence1eng.toLowerCase().contains("Could")
											| sentence1eng.contains("could not") | sentence1eng.contains("Could not")
											| sentence1eng.contains("couldn't") | sentence1eng.contains("Couldn't")) {
										st.setInt(3, 7);
										control = 7;
										System.out.println(sentence1eng + " - 7 Past of can - " + arquivo.getName());
										bw.write(sentence1eng + " - 7 Past of can - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									if (sentence1eng.contains("should") | sentence1eng.contains("Should")
											| sentence1eng.contains("should not") | sentence1eng.contains("Should not")
											| sentence1eng.contains("shouldn't")
											| sentence1eng.contains("Shouldn't") & control != 7)

									{
										st.setInt(3, 6);
										control = 6;
										System.out.println(sentence1eng + " - 6 Past of shall - " + arquivo.getName());
										bw.write(sentence1eng + " - 6 Past of shall - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.toLowerCase().contains("Would".toLowerCase())
											| sentence1eng.toLowerCase().contains("Would not".toLowerCase())
											| sentence1eng.toLowerCase().contains("Wouldn't".toLowerCase())
											| sentence1eng.toLowerCase().contains("I’d")
											| sentence1eng.toLowerCase().contains("They’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("You’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("We’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("He’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("She’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("It’d".toLowerCase()) & control != 7
													& control != 6 & control != 5)

									{
										st.setInt(3, 5);
										control = 5;
										System.out.println(sentence1eng + " - 5 Past of will - " + arquivo.getName());
										bw.write(sentence1eng + " - 5 Past of will - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.contains("Will") | sentence1eng.contains("Won't")
											| sentence1eng.contains("won't") | sentence1eng.contains("will")
											| sentence1eng.contains("will not") | sentence1eng.contains("Will not")
											| sentence1eng.contains("going to") | sentence1eng.contains("shall")
											| sentence1eng.contains("Shall") | sentence1eng.contains("shan't")
											| sentence1eng.contains("Shan't") | sentence1eng.contains("Shall not")
											| sentence1eng.contains("shall not") & control != 7 & control != 6
													& control != 5) {
										st.setInt(3, 4);
										control = 4;
										System.out.println(sentence1eng + " - 4 Future - " + arquivo.getName());
										bw.write(sentence1eng + " - 4 Future - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.contains("Have you") | sentence1eng.contains("Has he been")
											| sentence1eng.contains("I have been")
											| sentence1eng.contains("Have I been")
											| sentence1eng.contains("We have been")
											| sentence1eng.contains("He has been")
											| sentence1eng.contains("They have been")
											| sentence1eng.contains("She has been")
											| sentence1eng.contains("You have been")
											| sentence1eng.contains("It has been")
											| sentence1eng.contains("I have been")
											| sentence1eng.contains("have I been")
											| sentence1eng.contains("we have been")
											| sentence1eng.contains("he has been")
											| sentence1eng.contains("they have been")
											| sentence1eng.contains("she has been")
											| sentence1eng.contains("you have been")
											| sentence1eng.contains("it has been")
											| sentence1eng.contains("I haven't been")
											| sentence1eng.contains("I have not been")
											| sentence1eng.contains("we haven't been")
											| sentence1eng.contains("he hasn't been")
											| sentence1eng.contains("they haven't been")
											| sentence1eng.contains("she hasn't been")
											| sentence1eng.contains("you haven't been")
											| sentence1eng.contains("it hasn't been")
											| sentence1eng.contains("we have not been")
											| sentence1eng.contains("he has not been")
											| sentence1eng.contains("they have not been")
											| sentence1eng.contains("she has not been")
											| sentence1eng.contains("you have not been")
											| sentence1eng.contains("have been")
											| sentence1eng.contains("it has not been") & control != 7 & control != 6
													& control != 5 & control != 4) {
										st.setInt(3, 3);
										control = 3;
										System.out
												.println(sentence1eng + " - 3 Past participle - " + arquivo.getName());
										bw.write(sentence1eng + " - 3 Past participle - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									else if (!sentence1eng.contains("Do") & !sentence1eng.contains("Does")
											& !sentence1eng.contains("does") & !sentence1eng.contains("don't")
											& !sentence1eng.contains("doesn't") & !sentence1eng.contains("Don't")
											& !sentence1eng.contains("Doesn't") & !sentence1eng.contains("I am")
											& !sentence1eng.contains("What do") & !sentence1eng.contains("Do you")
											& !sentence1eng.contains("What are") & !sentence1eng.contains("Do I")
											& !sentence1eng.contains("What is") & !sentence1eng.contains("aren't")
											& !sentence1eng.contains("you are") & !sentence1eng.contains("You are")
											& !sentence1eng.contains("we are") & !sentence1eng.contains("We are")
											& !sentence1eng.contains("they are") & !sentence1eng.contains("They are")
											& !sentence1eng.contains("Are you") & !sentence1eng.contains("are you")
											& !sentence1eng.contains("Are we") & !sentence1eng.contains("are we")
											& !sentence1eng.contains("Are they") & !sentence1eng.contains("are they")
											& !sentence1eng.contains("I'm") & !sentence1eng.contains("I am")
											& !sentence1eng.contains("Am I") & !sentence1eng.contains("Is he")
											& !sentence1eng.contains("Is she") & !sentence1eng.contains("Is it")
											& !sentence1eng.contains("you're") & !sentence1eng.contains("You're")
											& !sentence1eng.contains("we're") & !sentence1eng.contains("We're")
											& !sentence1eng.contains("they're") & !sentence1eng.contains("They're")
											& !sentence1eng.contains("Where do") & !sentence1eng.contains("Where does")
											& !sentence1eng.contains("How do") & !sentence1eng.contains("I do")
											& !sentence1eng.contains("you do") & !sentence1eng.contains("You do")
											& !sentence1eng.contains("do you") & !sentence1eng.contains("They do")
											& !sentence1eng.contains("do they") & !sentence1eng.contains("we do")
											& !sentence1eng.contains("do we") & !sentence1eng.contains("You need")
											& !sentence1eng.contains("Edmund")) {

										if ((sentence1eng.contains("Did") | sentence1eng.contains("did")
												|| sentence1eng.contains("Didn't") || sentence1eng.contains("didn't")
												|| sentence1eng.contains("were") || sentence1eng.contains("Were")
												|| sentence1eng.contains("weren't") || sentence1eng.contains("Weren't")
												|| sentence1eng.contains("Was") || sentence1eng.contains("was")
												|| sentence1eng.contains("Wasn't")
												|| sentence1eng.contains("wasn't")
														| (sentence1eng.toLowerCase().contains("He ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("She ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("You ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("We ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("It ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("I ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("They ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed")))
												& control != 7 & control != 6 & control != 5 & control != 4
												& control != 3) {
											st.setInt(3, 2);
											control = 2;
											System.out
													.println(sentence1eng + " - 2 Simple past - " + arquivo.getName());
											bw.write(sentence1eng + " - 2 Simple past - " + arquivo.getName());
											bw.flush();
											bw.newLine();
										}
									}

									if (control != 7 & control != 6 & control != 5 & control != 4 & control != 3
											& control != 2) {
										st.setInt(3, 1);
										control = 1;
										System.out.println(sentence1eng + " - 1 Present - " + arquivo.getName());
										bw.write(sentence1eng + " - 1 Present - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									for (String verb : mg.returnListVerbs(path,
											"Verbs Irregular_Past_Participle.txt")) {
										if (((sentence1eng.toLowerCase().contains("Have you ".toLowerCase())
												&& sentence1eng.toLowerCase()
														.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("haven't you ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has he ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("haven't ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("hasn't ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have you ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has he ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have they ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has she ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have they ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has she ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have we ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has it".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have we ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has it ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have I ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have I ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("I have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("She has".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("He has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("It has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("We have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("They have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("You have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("you have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have you ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has he ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have you ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has he ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have they ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has she ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have they ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has she ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have we ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has it ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have we ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has it ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have I ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have I ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("I have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("She has ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("He has ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("It has ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("We have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("They have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("You have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("you have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("hasn't ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("haven't ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("ve ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| sentence1eng.toLowerCase()
														.contains("have not ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("has not ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| (sentence1eng.toLowerCase().contains("I’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("You’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("We’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("They’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("It’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("He’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("She’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| sentence1eng.toLowerCase().contains(
														"I’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"You’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"We’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("They’d ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"It’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"He’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("She’d ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase()))
												& control != 7 & control != 6 & control != 4) {

											System.out.println(verb + " - " + sentence1eng
													+ " - 3 Past participle FOR3- " + arquivo.getName());
											st.setInt(3, 3);
											control = 3;
											bw.write(verb + " - " + sentence1eng + " - 3 Past participle FOR3- "
													+ arquivo.getName());
											bw.flush();
											bw.newLine();
											break;
										}
									}

									for (String verb2 : mg.returnListVerbs(path, "Verbs Irregular_Simple_Past.txt")) {
										if (sentence1eng.toLowerCase()
												.contains(" " + verb2.replaceAll(" ", "").toLowerCase() + " ")) {
											if (control != 7 & control != 6 & control != 5 & control != 4 & control != 3
													& control != 2) {
												if (sentence1eng.toLowerCase()
														.contains("have".toLowerCase()) == false) {
													if (sentence1eng.toLowerCase()
															.contains("has".toLowerCase()) == false) {
														if (sentence1eng.toLowerCase()
																.contains(" read ".toLowerCase()) == false)
															if (sentence1eng.toLowerCase()
																	.contains("eed ".toLowerCase()) == false) {
																{
																	System.out.println(verb2 + " - " + sentence1eng
																			+ " - 2 Simple Past FOR2 - "
																			+ arquivo.getName());
																	st.setInt(3, 2);
																	control = 2;
																	bw.write(verb2 + " - " + sentence1eng
																			+ " - 2 Simple Past FOR2 - "
																			+ arquivo.getName());
																	bw.flush();
																	bw.newLine();
																	break;
																}
															}
													}
												}
											}
										}
									}

									if ((sentence1eng.contains("eed ")
											&& control != 7 & control != 6 & control != 5 & control != 4)) {
										System.out.println(sentence1eng + " - 1 Present FOR1 - " + arquivo.getName());
										st.setInt(3, 1);
										control = 1;
										bw.write(sentence1eng + " - 1 Present FOR1 - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									st.setInt(4, 2);

									if (sentence1eng.contains("?")) {
										st.setInt(5, 3);
									} else if (sentence1eng.contains("don't") | sentence1eng.contains("doesn't")
											| sentence1eng.contains("won't") | sentence1eng.contains("wouldn't")
											| sentence1eng.contains("haven't") | sentence1eng.contains("didn't")
											| sentence1eng.contains("not") | sentence1eng.contains("isn't")
											| sentence1eng.contains("hasn't") | sentence1eng.contains("weren't")
											| sentence1eng.contains("Don't") | sentence1eng.contains("Doesn't")
											| sentence1eng.contains("Won't") | sentence1eng.contains("Wouldn't")
											| sentence1eng.contains("Haven't") | sentence1eng.contains("Didn't")
											| sentence1eng.contains("Not") | sentence1eng.contains("Isn't")
											| sentence1eng.contains("Hasn't") | sentence1eng.contains("Weren't")
											| sentence1eng.contains("shouldn't")) {
										st.setInt(5, 2);
									} else {
										st.setInt(5, 1);
									}
									st.executeUpdate();
									line = br.readLine();

								} else {

									st = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st.setString(1, sentence1eng);
									st.setString(2, sentence3tips);

									int control = 0;
									// idcategorysentence
									if (sentence1eng.contains("could") | sentence1eng.contains("Could")
											| sentence1eng.contains("could not") | sentence1eng.contains("Could not")
											| sentence1eng.contains("couldn't") | sentence1eng.contains("Couldn't")) {
										st.setInt(3, 7);
										control = 7;
										System.out.println(sentence1eng + " - 7 Past of can - " + arquivo.getName());
										bw.write(sentence1eng + " - 7 Past of can - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									else if (sentence1eng.contains("should") | sentence1eng.contains("Should")
											| sentence1eng.contains("should not") | sentence1eng.contains("Should not")
											| sentence1eng.contains("shouldn't")
											| sentence1eng.contains("Shouldn't") & control != 7)

									{
										st.setInt(3, 6);
										control = 6;
										System.out.println(sentence1eng + " - 6 Past of shall - " + arquivo.getName());
										bw.write(sentence1eng + " - 6 Past of shall - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.toLowerCase().contains("Would".toLowerCase())
											| sentence1eng.toLowerCase().contains("Would not".toLowerCase())
											| sentence1eng.toLowerCase().contains("Wouldn't".toLowerCase())
											| sentence1eng.toLowerCase().contains("I’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("They’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("You’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("We’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("He’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("She’d".toLowerCase())
											| sentence1eng.toLowerCase().contains("It’d".toLowerCase()) & control != 7
													& control != 6 & control != 5) {
										st.setInt(3, 5);
										control = 5;
										System.out.println(sentence1eng + " - 5 Past of will - " + arquivo.getName());
										bw.write(sentence1eng + " - 5 Past of will - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.contains("Will") | sentence1eng.contains("Won't")
											| sentence1eng.contains("won't") | sentence1eng.contains("will")
											| sentence1eng.contains("will not") | sentence1eng.contains("Will not")
											| sentence1eng.contains("going to") | sentence1eng.contains("shall")
											| sentence1eng.contains("Shall") | sentence1eng.contains("shan't")
											| sentence1eng.contains("Shan't") | sentence1eng.contains("Shall not")
											| sentence1eng.contains("shall not") & control != 7 & control != 6
													& control != 5) {
										st.setInt(3, 4);
										control = 4;
										System.out.println(sentence1eng + " - 4 Future - " + arquivo.getName());
										bw.write(sentence1eng + " - 4 Future - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									} else if (sentence1eng.contains("Have you been")
											| sentence1eng.contains("Has he been")
											| sentence1eng.contains("I have been")
											| sentence1eng.contains("Have I been")
											| sentence1eng.contains("We have been")
											| sentence1eng.contains("He has been")
											| sentence1eng.contains("They have been")
											| sentence1eng.contains("She has been")
											| sentence1eng.contains("You have been")
											| sentence1eng.contains("It has been")
											| sentence1eng.contains("I have been")
											| sentence1eng.contains("have I been")
											| sentence1eng.contains("we have been")
											| sentence1eng.contains("he has been")
											| sentence1eng.contains("they have been")
											| sentence1eng.contains("she has been")
											| sentence1eng.contains("you have been")
											| sentence1eng.contains("it has been")
											| sentence1eng.contains("I haven't been")
											| sentence1eng.contains("I have not been")
											| sentence1eng.contains("we haven't been")
											| sentence1eng.contains("he hasn't been")
											| sentence1eng.contains("they haven't been")
											| sentence1eng.contains("she hasn't been")
											| sentence1eng.contains("you haven't been")
											| sentence1eng.contains("it hasn't been")
											| sentence1eng.contains("we have not been")
											| sentence1eng.contains("he has not been")
											| sentence1eng.contains("they have not been")
											| sentence1eng.contains("she has not been")
											| sentence1eng.contains("you have not been")
											| sentence1eng.contains("have been")
											| sentence1eng.contains("it has not been") & control != 7 & control != 6
													& control != 4 & control != 3) {
										st.setInt(3, 3);
										control = 3;
										System.out
												.println(sentence1eng + " - 3 Past participle - " + arquivo.getName());
										bw.write(sentence1eng + " - 3 Past participle - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									else if (!sentence1eng.contains("Do") & !sentence1eng.contains("Does")
											& !sentence1eng.contains("does") & !sentence1eng.contains("don't")
											& !sentence1eng.contains("doesn't") & !sentence1eng.contains("Don't")
											& !sentence1eng.contains("Doesn't") & !sentence1eng.contains("I am")
											& !sentence1eng.contains("What do") & !sentence1eng.contains("Do you")
											& !sentence1eng.contains("What are") & !sentence1eng.contains("Do I")
											& !sentence1eng.contains("What is") & !sentence1eng.contains("aren't")
											& !sentence1eng.contains("you are") & !sentence1eng.contains("You are")
											& !sentence1eng.contains("we are") & !sentence1eng.contains("We are")
											& !sentence1eng.contains("they are") & !sentence1eng.contains("They are")
											& !sentence1eng.contains("Are you") & !sentence1eng.contains("are you")
											& !sentence1eng.contains("Are we") & !sentence1eng.contains("are we")
											& !sentence1eng.contains("Are they") & !sentence1eng.contains("are they")
											& !sentence1eng.contains("I'm") & !sentence1eng.contains("I am")
											& !sentence1eng.contains("Am I") & !sentence1eng.contains("Is he")
											& !sentence1eng.contains("Is she") & !sentence1eng.contains("Is it")
											& !sentence1eng.contains("you're") & !sentence1eng.contains("You're")
											& !sentence1eng.contains("we're") & !sentence1eng.contains("We're")
											& !sentence1eng.contains("they're") & !sentence1eng.contains("They're")
											& !sentence1eng.contains("Where do") & !sentence1eng.contains("Where does")
											& !sentence1eng.contains("How do") & !sentence1eng.contains("I do")
											& !sentence1eng.contains("you do") & !sentence1eng.contains("You do")
											& !sentence1eng.contains("do you") & !sentence1eng.contains("They do")
											& !sentence1eng.contains("do they") & !sentence1eng.contains("we do")
											& !sentence1eng.contains("do we") & !sentence1eng.contains("You need")
											& !sentence1eng.contains("Edmund")) {

										if ((sentence1eng.contains("Did") | sentence1eng.contains("did")
												|| sentence1eng.contains("Didn't") || sentence1eng.contains("didn't")
												|| sentence1eng.contains("were") || sentence1eng.contains("Were")
												|| sentence1eng.contains("weren't") || sentence1eng.contains("Weren't")
												|| sentence1eng.contains("Was") || sentence1eng.contains("was")
												|| sentence1eng.contains("Wasn't")
												|| sentence1eng.contains("wasn't")
														| (sentence1eng.toLowerCase().contains("He ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("She ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("You ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("We ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("It ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("I ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed"))
														| (sentence1eng.toLowerCase().contains("They ".toLowerCase())
																&& sentence1eng.toLowerCase().contains("ed")))
												& control != 7 & control != 6 & control != 5 & control != 4
												& control != 3) {
											st.setInt(3, 2);
											control = 2;
											System.out
													.println(sentence1eng + " - 2 Simple past - " + arquivo.getName());
											bw.write(sentence1eng + " -  2 Simple past - " + arquivo.getName());
											bw.flush();
											bw.newLine();
										}
									}

									if (control != 7 & control != 6 & control != 5 & control != 4 & control != 3
											& control != 2) {
										st.setInt(3, 1);
										control = 1;
										System.out.println(sentence1eng + " - 1 Present - " + arquivo.getName());
										bw.write(sentence1eng + " -  1 Present  - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									for (String verb : mg.returnListVerbs(path,
											"Verbs Irregular_Past_Participle.txt")) {
										if (((sentence1eng.toLowerCase().contains("Have you ".toLowerCase())
												&& sentence1eng.toLowerCase()
														.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("haven't you ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has he ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("haven't ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("hasn't ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have you ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has he ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have they ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has she ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have they ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has she ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have we ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Has it ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have we".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has it ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have I ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have I ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("I have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("She has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("He has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("It has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("We have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("They have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("You have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("you have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("have ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("has ".toLowerCase())
														&& sentence1eng.toLowerCase()
																.contains(verb.replaceAll(" ", "").toLowerCase()))
												| (sentence1eng.toLowerCase().contains("Have you ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has he ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have you ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has he ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have they ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has she".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have they ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has she ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have we ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Has it ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have we ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has it ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have I ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("Have I".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("I have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("She has".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("He has ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("It has".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("We have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("They have".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("You have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("you have".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("has".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("have ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("hasn't ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("haven't ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("ve ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| sentence1eng.toLowerCase()
														.contains("have not ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("has not ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| (sentence1eng.toLowerCase().contains("I’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("You’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("We’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("They’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("It’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("He’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| (sentence1eng.toLowerCase().contains("She’d ".toLowerCase())
														&& sentence1eng.toLowerCase().contains("ed"))
												| sentence1eng.toLowerCase().contains(
														"I’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"You’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"We’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("They’d ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"It’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase().contains(
														"He’d ".toLowerCase() + verb.replaceAll(" ", "").toLowerCase())
												| sentence1eng.toLowerCase()
														.contains("She’d ".toLowerCase()
																+ verb.replaceAll(" ", "").toLowerCase()))
												& control != 7 & control != 6 & control != 4) {

											System.out.println(verb + " - " + sentence1eng
													+ " - 3 Past participle FOR3- " + arquivo.getName());
											st.setInt(3, 3);
											control = 3;
											bw.write(verb + " - " + sentence1eng + " - 3 Past participle FOR3- "
													+ arquivo.getName());
											bw.flush();
											bw.newLine();
											break;
										}
									}

									for (String verb2 : mg.returnListVerbs(path, "Verbs Irregular_Simple_Past.txt")) {
										if (sentence1eng.toLowerCase()
												.contains(" " + verb2.replaceAll(" ", "").toLowerCase() + " ")) {
											if (control != 7 & control != 6 & control != 5 & control != 4 & control != 3
													& control != 2) {
												if (sentence1eng.toLowerCase()
														.contains("have".replaceAll(" ", "").toLowerCase()) == false) {
													if (sentence1eng.toLowerCase().contains(
															"has".replaceAll(" ", "").toLowerCase()) == false) {
														if (sentence1eng.toLowerCase()
																.contains(" read ".toLowerCase()) == false)
															if (sentence1eng.toLowerCase()
																	.contains("eed ".toLowerCase()) == false) {
																{
																	System.out.println(verb2 + " - " + sentence1eng
																			+ " - 2 Simple Past FOR2 - "
																			+ arquivo.getName());
																	st.setInt(3, 2);
																	control = 2;
																	bw.write(verb2 + " - " + sentence1eng
																			+ " - 2 Simple Past FOR2 - "
																			+ arquivo.getName());
																	bw.flush();
																	bw.newLine();
																	break;
																}
															}
													}
												}
											}
										}
									}

									if ((sentence1eng.contains("eed ")
											&& control != 7 & control != 6 & control != 5 & control != 4)) {
										System.out.println(sentence1eng + " - 1 Present FOR1 - " + arquivo.getName());
										st.setInt(3, 1);
										control = 1;
										bw.write(sentence1eng + " - 1 Present FOR1 - " + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									st.setInt(4, 2);
									if (sentence1eng.contains("?")) {
										st.setInt(5, 3);
									} else if (sentence1eng.contains("don't") | sentence1eng.contains("doesn't")
											| sentence1eng.contains("won't") | sentence1eng.contains("wouldn't")
											| sentence1eng.contains("haven't") | sentence1eng.contains("didn't")
											| sentence1eng.contains("not") | sentence1eng.contains("isn't")
											| sentence1eng.contains("hasn't") | sentence1eng.contains("weren't")
											| sentence1eng.contains("Don't") | sentence1eng.contains("Doesn't")
											| sentence1eng.contains("Won't") | sentence1eng.contains("Wouldn't")
											| sentence1eng.contains("Haven't") | sentence1eng.contains("Didn't")
											| sentence1eng.contains("Not") | sentence1eng.contains("Isn't")
											| sentence1eng.contains("Hasn't") | sentence1eng.contains("Weren't")
											| sentence1eng.contains("shouldn't")) {
										st.setInt(5, 2);
									} else {
										st.setInt(5, 1);
									}

									st.executeUpdate();

									st2 = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st2.setString(1, sentence2port);
									st2.setString(2, sentence3tips);
									PreparedStatement st4 = conn.prepareStatement("select * from sentence");

									ResultSet resultSet = st4.executeQuery(
											"SELECT * FROM englishworkout.sentence  order by idsentence desc LIMIT 1");
									// idcategorysentence

									while (resultSet.next()) {
										System.out.println(resultSet.getInt("idcategorysentence"));
										st2.setInt(3, (resultSet.getInt("idcategorysentence")));
										bw.write(sentence2port + " - idcategorysentence: "
												+ resultSet.getInt("idcategorysentence") + "-" + arquivo.getName());
										bw.flush();
										bw.newLine();
									}

									st2.setInt(4, 1);
									if (sentence2port.contains("?")) {
										st2.setInt(5, 3);
									} else if (sentence2port.contains("não") | sentence2port.contains("Não")) {
										st2.setInt(5, 2);
									} else {
										st2.setInt(5, 1);
									}

									st2.executeUpdate();

									ResultSet rs2 = st2.getGeneratedKeys();
									ResultSet rs = st.getGeneratedKeys();

									st3 = conn.prepareStatement(
											"INSERT INTO relsourcetranslate "
													+ "(idsentencetranslate,idsentencesource) " + "VALUES " + "(?,?)",
											Statement.RETURN_GENERATED_KEYS);

									if (rs != null && rs.next()) {
										long id = rs.getLong(1);
										System.out.println("Inserted 1 ID -" + id); // display inserted record
										st3.setLong(1, id);
										bw.write("Inserted 1 ID -" + id);
										bw.flush();
										bw.newLine();
									}

									if (rs2 != null && rs2.next()) {
										long id2 = rs2.getLong(1);
										System.out.println("Inserted 2 ID -" + id2); // display inserted record
										st3.setLong(2, id2);
										bw.write("Inserted 2 ID -" + id2);
										bw.flush();
										bw.newLine();
									}

									st3.executeUpdate();
									DB.closeStatement(st);
									DB.closeStatement(st2);
									DB.closeStatement(st3);
								}
							}
							line = br.readLine();
						}
						br.close();					
					}
					conn.commit();
				}
			}
			sc.close();
			bw.close();

		} catch (SQLException |

				IOException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeStatement(st2);
			DB.closeConnection();

		}
	}

}