package vista;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ContMainView extends Main implements Initializable {
	@FXML
	TextArea txtTexto;
	
	private Alert alert;
	@FXML
	Label guard;
	@FXML
	TextField txtArchivo;
	@FXML
	ComboBox<String> combTxts = new ComboBox<String>();

	@FXML
	private void grabar(ActionEvent event) throws IOException, InterruptedException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		BufferedWriter writer = new BufferedWriter(new FileWriter(txtArchivo.getText() + ".txt", true));
		if (!txtTexto.getText().equals("")) {
			writer.write((dtf.format(now)));
			writer.newLine();
			writer.write(txtTexto.getText());
			writer.newLine();
			writer.close();
			guard.setVisible(true);
		} else {
			txtTexto.setPromptText("Tiene que haber algo escrito");
		}

	}

	@FXML
	private void borarArchivo(ActionEvent event) {
		try {

			File file = new File(txtArchivo.getText() + ".txt");

			if (file.delete()) {
				txtTexto.setPromptText("Arcivo " + file.getName() + " eliminado");
			} else {
				txtTexto.setPromptText("Algo fallo");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@FXML
	private void ocultarLabel() {
		guard.setVisible(false);
	}

	@FXML
	private void limpiar(ActionEvent event) {
		ocultarLabel();
		txtTexto.clear();
	}

	@FXML
	private void ver(ActionEvent event) {
		ocultarLabel();
		File file = new File(txtArchivo.getText() + ".txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			txtTexto.clear();
			while (sc.hasNextLine()) {
			txtTexto.appendText(sc.nextLine() + "\n");
		//	sc.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error primoh");
			alert.setHeaderText("Algo ha ido mal");
			alert.setContentText("Ese fichero no existe, bro");

			alert.showAndWait();
			
		}

	}

	@FXML
	private void eleccion(ActionEvent event) {
		String archivo = combTxts.getValue();

		// System.out.println( archivo.substring(0, combTxts.getValue().length()-4));
		txtArchivo.setText(archivo.substring(0, combTxts.getValue().length() - 4));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String filePath = new File("").getAbsolutePath();
		File f = new File(filePath);
		File[] ficheros = f.listFiles();
		File filedel = new File("txts.txt");
		if (filedel.delete()) {
			combTxts.setPromptText("Elije .txt");
		}

		for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].getName().length() > 3) {
				String extension = ficheros[x].getName().substring(ficheros[x].getName().length() - 4,
						ficheros[x].getName().length());
				// System.out.println(extension);
				try {
					if (extension.equals(".txt")) {
						BufferedWriter writer = new BufferedWriter(new FileWriter("txts.txt", true));
						writer.write(ficheros[x].getName());
						writer.newLine();
						writer.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				File file = new File("txts.txt");
				try {
					Scanner sc = new Scanner(file);
					List<String> list = new ArrayList<String>();
					ObservableList<String> lista = FXCollections.observableList(list);

					while (sc.hasNext()) {
						lista.add(sc.nextLine());

					}
					combTxts.setItems(lista);
					sc.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
