package controladores;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.swing.JOptionPane;

import util.UtilidadesXML;
import br.unesp.repositorio.base.xmlschema.metadatapatterner.Pattern;
import br.unesp.repositorio.base.xmlschema.metadatapatterner.Patterns;
import br.unesp.repositorio.base.xmlschema.metadatapatterner.Rules;
import br.unesp.repositorio.tools.metadatapatterner.tools.TextUtils;

public class Tela {

	private Patterns patterns;

	@FXML
	ListView<Pattern> listaPadroes;

	@FXML
	ListView<String> listaRegras;

	@FXML
	TextField txtRegra;

	@FXML
	TextField txtRotulo;

	@FXML
	TextField txtValor;

	@FXML
	TextArea visualizadorXML;

	private ObservableList<String> ovListaPadroes;

	public Tela() {
		novoXML();
		
	}

	public void novoXML() {
		patterns = new Patterns();
		atualizaJanela();
	}

	public void abrirXML(){
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Arquivos xml","*.xml"));
		File arquivoSelecionado = fc.showOpenDialog(null);
		if(arquivoSelecionado!=null){
			try {
				patterns = UtilidadesXML.carregaXML(arquivoSelecionado.getAbsolutePath());
				atualizaEditorTexto();
				atualizaEditorVisual();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void salvarXML(){
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Arquivos xml","*.xml"));
		File arquivoSelecionado = fc.showSaveDialog(null);
		if(arquivoSelecionado!=null){
			try {
				UtilidadesXML.toXML(patterns,arquivoSelecionado);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo","Erro",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void sair(){
		System.exit(0);
	}

	public void adicionarPadrao(){
		String rotulo = JOptionPane.showInputDialog("Rótulo da novo Padrão");
		if(rotulo!=null && !rotulo.isEmpty()){
			Pattern pattern = new Pattern();
			pattern.setLabel(rotulo);
			pattern.setValue(rotulo);
			pattern.setRules(new Rules());
			patterns.getPattern().add(pattern);
			atualizaJanela();
			listaPadroes.getFocusModel().focus(patterns.getPattern().indexOf(pattern));
		}
	}

	public void removerPadrao(){
		Pattern pattern = listaPadroes.getFocusModel().getFocusedItem();
		if(pattern!=null){

			ovListaPadroes.remove(pattern);
			patterns.getPattern().remove(pattern);
			atualizaJanela();
		}
	}

	private void atualizaJanela() {

		atualizaEditorVisual();
		atualizaEditorTexto();

	}

	public void adicionarRegra(){
		String regra = txtRegra.getText();
		if(regra!=null && !regra.isEmpty()){
			regra = TextUtils.removeExtraSpaces(TextUtils.removePuncts(TextUtils.removeAccents(regra.trim().toLowerCase())));
			Pattern pattern = listaPadroes.getFocusModel().getFocusedItem();
			if(pattern!=null){
				pattern.getRules().getMatch().add(regra);
				atualizaJanela();
				listaPadroes.getFocusModel().focus(patterns.getPattern().indexOf(pattern));
			}

		}
	}

	public void removerRegra(){
		String regra = listaRegras.getFocusModel().getFocusedItem();
		if(regra!=null && !regra.isEmpty()){
			Pattern pattern = listaPadroes.getFocusModel().getFocusedItem();
			if(pattern!=null){
				pattern.getRules().getMatch().remove(regra);
				atualizaJanela();
				listaPadroes.getFocusModel().focus(patterns.getPattern().indexOf(pattern));
			}

		}
	}

	public void aplicarAlteracoes(){
		Pattern pattern = listaPadroes.getFocusModel().getFocusedItem();
		if(pattern!= null){
			pattern.setLabel(txtRotulo.getText());
			pattern.setValue(txtValor.getText());
		}
		atualizaJanela();
	}

	private void atualizaEditorTexto(){
		try{
			visualizadorXML.setText(UtilidadesXML.toXML(patterns));
			
		}catch(Exception e){
			
		}
	}

	private void atualizaEditorVisual(){
		try{
			txtRegra.setText("");
			txtRotulo.setText("");
			txtValor.setText("");
			Pattern focusedItem = listaPadroes.getFocusModel().getFocusedItem();
			listaPadroes.getItems().clear();
			ObservableList<Pattern> lista = FXCollections.observableArrayList(patterns.getPattern());
			listaPadroes.setItems(lista);
			listaPadroes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pattern>() {



				public void changed(ObservableValue<? extends Pattern> ov,
						Pattern antigo, Pattern novo) {


					atualizaRegras(novo);
				}



			});

			atualizaRegras(focusedItem);
		}catch(Exception e){

		}
	}



	private void atualizaRegras(Pattern novo) {
		Pattern pattern = novo;
		if(pattern!=null){
			ovListaPadroes = FXCollections.observableArrayList(pattern.getRules().getMatch());
			listaRegras.setItems(ovListaPadroes);
			txtRotulo.setText(pattern.getLabel());
			txtValor.setText(pattern.getValue());
		}else{
			ovListaPadroes = FXCollections.observableArrayList();
			listaRegras.setItems(ovListaPadroes);
			txtRotulo.setText("");
			txtValor.setText("");
		}
	}
}
