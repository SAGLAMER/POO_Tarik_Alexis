package atelier3.gui;

import atelier3.controller.InputViewData;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author francoise.perrin
 * 
 * Cette classe est la fen�tre du jeu de dames
 * Elle d�l�gue a un objet Board la gestion de l'affichage du damier
 * et affiche les axes le long du damier
 * 
 */

public class View extends BorderPane {

	// le damier compos� de carr�s noirs et blancs
	// sur lesquels sont positionn�s des pi�ces noires ou blanches
	Pane board ;

	public View (EventHandler<MouseEvent> clicListener) {
		super();

		// les cases et le pi�ces sur le damier seront �cout�es par l'objet
		// pass� en param�tre au constructeur
		board = new Board(clicListener);

		//Affichage du score
		String sResult;
		sResult = "Blanc 0 - 0 Noir";
		labelScore = new Label(sResult);
		labelScore.setPrefHeight(30);
		this.setTop(labelScore);
		labelScore.setMaxWidth(Double.MAX_VALUE);
		labelScore.setAlignment(Pos.CENTER);
		labelScore.setFont(new Font("Arial", 22));

		
		// cr�ation d'un fond d'�cran qui contient le damier + les axes 
		BorderPane checkersBoard = new BorderPane();	

		// la taille du damier est fonction de taille de la Scene
		board.prefWidthProperty().bind(this.widthProperty());
		board.prefHeightProperty().bind(this.heightProperty());

		// ajout du damier au centre du fond d'�cran
		checkersBoard.setCenter(board);

		// ajout des axes sur les cot�s du damier
		checkersBoard.setTop(createHorizontalAxis());
		checkersBoard.setBottom(createHorizontalAxis());
		checkersBoard.setLeft(createVerticalAxis());
		checkersBoard.setRight(createVerticalAxis());

		// ajout du fond d'�cran �la vue
		this.setCenter(checkersBoard);


		
	}

	///////////////////////////////////////////////////////////////////////////////////// 
	// M�thode invoqu�e depuis le Controller pour propager les d�placements
	// effectu�s sur le model sur la vue
	/////////////////////////////////////////////////////////////////////////////////////
	
	public void actionOnGui(InputViewData<Integer> dataToRefreshView) {
		((Board)this.board).actionOnGui(dataToRefreshView);
		setScore(dataToRefreshView);
		winner(dataToRefreshView);
	}
	
	//////////////////////////////////////////////////////////////////////////
	

	private GridPane createHorizontalAxis() {
		GridPane pane = new GridPane();
		pane.prefWidthProperty().bind(this.widthProperty());
		for (char c = 'a'; c<='j'; c++){
			Label label1 = new Label(String.valueOf(c));
			label1.setAlignment(Pos.CENTER);
			label1.prefWidthProperty().bind(pane.prefWidthProperty().divide(GuiConfig.SIZE));
			pane.add(label1, c-'a', 0);
		}
		return pane;
	}

	private GridPane createVerticalAxis() {
		GridPane pane = new GridPane();
		pane.prefHeightProperty().bind(this.heightProperty());
		for (int c = 10; c>=1; c--){
			Label label1 = new Label(String.valueOf(c));
			label1.prefHeightProperty().bind(pane.prefHeightProperty().divide(GuiConfig.SIZE));
			pane.add(label1, 0, 10-c+1);
		}
		return pane;
	}

	private void setScore(InputViewData<Integer> dataToRefreshView)
	{
		String sResult = "Blanc " + dataToRefreshView.score[0] + " - " + dataToRefreshView.score[1] + " Noir";
		labelScore.setText(sResult);
	}

	private void winner(InputViewData<Integer> dataToRefreshView)
	{
		Image image = null;
		String trophyFile = null, nomImageFile = null;
		File g = new File("");
		trophyFile = g.getAbsolutePath()+"/images/" + "trophy.png";	// attention au chemin
		try {
			image = new Image(new FileInputStream(trophyFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Alert alertWinner=new Alert(Alert.AlertType.INFORMATION);
		alertWinner.setTitle("Congratulations !");
		ImageView icon = new ImageView(image);
		icon.setFitHeight(48);
		icon.setFitWidth(48);
		alertWinner.getDialogPane().setGraphic(icon);


		if(dataToRefreshView.score[2] == 1)
		{
			alertWinner.setHeaderText("Winner : Blanc ");
			alertWinner.show();
		}
		else if(dataToRefreshView.score[2] == 2)
		{
			alertWinner.setHeaderText("Winner : Noir ");
			alertWinner.show();
		}
	}
}


