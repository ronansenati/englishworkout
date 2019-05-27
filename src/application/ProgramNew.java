package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import db.DB;
import db.DbException;
import entities.MigraEntities;

public class ProgramNew {

	public static String path = null;

	public static void main(String[] args) {

		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

		Connection conn = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		PreparedStatement st3 = null;
		PreparedStatement st4 = null;
		PreparedStatement st5 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;

		try {
			System.out.println("Enter file full path migration files and script to generation your base:");
			path = "C:\\Users\\ronan\\eclipse-workspace\\MigraçãoEnglishWorkOut\\Carga de dados";
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
			BufferedWriter bw = new BufferedWriter(new FileWriter(path + "\\" + "log.txt"));

			bw.write(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			bw.flush();
			bw.newLine();

			for (String list : listOrigem) {

				File[] arrayFile = null;
				File file = new File(list);
				arrayFile = file.listFiles();

				for (File arquivo : arrayFile) {
					if (arquivo.getName().endsWith("ScriptEnglishWorkOut.sql")) {
						mg.executeSqlScript(conn, arquivo);
					}
				}
				for (File arquivo : arrayFile) {
					if (arquivo.getName().endsWith("MigraSentenceOrigin.csv")) {
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

							st5 = conn.prepareStatement(
									"SELECT * FROM englishworkout.sentence where sentence=\"" + sentence1eng + "\"");

							rs1 = st5.executeQuery();
							// idcategorysentence
							if (rs1 != null && rs1.next())
								resultString = rs1.getString("sentence").toString();
							rs1.close();
							if (!sentence1eng.toLowerCase().equals(resultString.replaceAll(" ", "").toLowerCase())) {

								if (sentence2port.replaceAll(" ", "").toLowerCase().equals("")) {
									st = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st.setString(1, sentence1eng);
									st.setString(2, sentence3tips);
									st.setInt(3, mg.sentenceTenseTypeAnalizer(sentence1eng, "tense.txt"));
									st.setInt(4, 2);
									st.setInt(5, mg.sentenceTenseTypeAnalizer(sentence1eng, "ModalTypeEngl.txt"));
									st.executeUpdate();
									line = br.readLine();

									rs1 = st5.executeQuery();

									if (rs1 != null && rs1.next()) {

										long id = rs1.getLong(1);
										System.out.println("Inserted 1 Engl ID - " + id + " - " + sentence1eng + "F12");
										bw.write("Inserted 1 Engl ID - " + id + " - " + sentence1eng + "F12");
										bw.flush();
										bw.newLine();
									}
								} else {
									st = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st.setString(1, sentence1eng);
									st.setString(2, sentence3tips);
									st.setInt(3, mg.sentenceTenseTypeAnalizer(sentence1eng, "tense.txt"));
									st.setInt(4, 2);
									st.setInt(5, mg.sentenceTenseTypeAnalizer(sentence1eng, "ModalTypeEngl.txt"));
									st.executeUpdate();

									st2 = conn.prepareStatement("INSERT INTO sentence "
											+ "(sentence, tipssentence, idcategorysentence,idlanguage, idtypesentence) "
											+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

									st2.setString(1, sentence2port);
									st2.setString(2, sentence3tips);

									st4 = conn.prepareStatement(
											"SELECT * FROM englishworkout.sentence  order by idsentence desc LIMIT 1");

									rs3 = st4.executeQuery();

									// idcategorysentence

									while (rs3.next()) {
										st2.setInt(3, (rs3.getInt("idcategorysentence")));
									}

									st2.setInt(4, 1);
									st2.setInt(5, mg.sentenceTenseTypeAnalizer(sentence2port, "ModalTypePort.txt"));

									st2.executeUpdate();

									rs = st.getGeneratedKeys();
									rs2 = st2.getGeneratedKeys();

									st3 = conn.prepareStatement(
											"INSERT INTO relsourcetranslate "
													+ "(idsentencetranslate,idsentencesource) " + "VALUES " + "(?,?)",
											Statement.RETURN_GENERATED_KEYS);

									if (rs != null && rs.next()) {
										long id = rs.getLong(1);
										System.out.println("Inserted 1 Engl ID - " + id + " - " + sentence1eng);
										st3.setLong(1, id);
										bw.write("Inserted 1 Engl ID - " + id + " - " + sentence1eng);
										bw.flush();
										bw.newLine();
									}

									if (rs2 != null && rs2.next()) {
										long id2 = rs2.getLong(1);
										st3.setLong(2, id2);
										System.out.println("Inserted 2 Port ID - " + id2 + " - " + sentence2port);
										bw.write("Inserted 2 Port ID - " + id2 + " - " + sentence2port);
										bw.flush();
										bw.newLine();
									}
									st3.executeUpdate();
								}
							}
							line = br.readLine();
						}
						br.close();
					}
				}
			}
			conn.commit();
			System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			bw.write(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			bw.flush();
			bw.newLine();
			bw.close();
			sc.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		} catch (Exception e2) {
			throw new DbException("System Error:" + e2.getMessage() + "\n\n" + e2.getStackTrace());
		} finally {
			DB.closeStatement(st);
			DB.closeStatement(st2);
			DB.closeStatement(st3);
			DB.closeStatement(st4);
			DB.closeStatement(st5);
			DB.closeResultSet(rs);
			DB.closeResultSet(rs1);
			DB.closeResultSet(rs2);
			DB.closeConnection();
		}
	}

}