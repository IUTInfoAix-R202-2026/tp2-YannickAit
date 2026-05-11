package fr.univ_amu.iut.exercice3;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests de l'exercice 3 - Palette réactive (pont avec TP1). */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaletteReactiveTest {

  private Stage stage;

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null); // évite la fuite de Scene entre tests (TestFX réutilise le Stage)
    this.stage = stage;
    new PaletteReactive().start(stage);
  }

  // --- Étape 1 : la fenêtre ---

  @Test
  @Order(1)
  void laFenetreEstVisible(FxRobot robot) {
    assertThat(stage.isShowing())
        .as("le Stage doit être affiché - appeler show() à la fin de start()")
        .isTrue();
  }

  @Test
  @Order(2)
  void laSceneExiste(FxRobot robot) {
    assertThat(stage.getScene())
        .as("le Stage doit avoir une Scene attachée via setScene()")
        .isNotNull();
  }

  @Test
  @Order(3)
  void laRacineEstUnBorderPane(FxRobot robot) {
    assertThat(stage.getScene().getRoot())
        .as("la racine de la Scene doit être un BorderPane")
        .isInstanceOf(BorderPane.class);
  }

  // --- Étape 2 : les trois BoutonCouleur ---

  @Test
  @Order(4)
  void lesTroisBoutonsExistent(FxRobot robot) {
    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    BoutonCouleur btnVert = robot.lookup("#btn-vert").queryAs(BoutonCouleur.class);
    BoutonCouleur btnBleu = robot.lookup("#btn-bleu").queryAs(BoutonCouleur.class);
    assertThat(btnRouge).as("un BoutonCouleur avec l'id 'btn-rouge' doit être présent").isNotNull();
    assertThat(btnRouge.getText()).as("le bouton rouge doit afficher 'Rouge'").isEqualTo("Rouge");
    assertThat(btnVert).as("un BoutonCouleur avec l'id 'btn-vert' doit être présent").isNotNull();
    assertThat(btnVert.getText()).as("le bouton vert doit afficher 'Vert'").isEqualTo("Vert");
    assertThat(btnBleu).as("un BoutonCouleur avec l'id 'btn-bleu' doit être présent").isNotNull();
    assertThat(btnBleu.getText()).as("le bouton bleu doit afficher 'Bleu'").isEqualTo("Bleu");
  }

  @Test
  @Order(5)
  void lesBoutonsOntUneCouleur(FxRobot robot) {
    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    BoutonCouleur btnVert = robot.lookup("#btn-vert").queryAs(BoutonCouleur.class);
    BoutonCouleur btnBleu = robot.lookup("#btn-bleu").queryAs(BoutonCouleur.class);
    assertThat(btnRouge.getCouleur())
        .as("le bouton rouge doit avoir la couleur 'red'")
        .isEqualTo("red");
    assertThat(btnVert.getCouleur())
        .as("le bouton vert doit avoir la couleur 'green'")
        .isEqualTo("green");
    assertThat(btnBleu.getCouleur())
        .as("le bouton bleu doit avoir la couleur 'blue'")
        .isEqualTo("blue");
  }

  @Test
  @Order(6)
  void leHBoxDesBoutonsEstEnHaut(FxRobot robot) {
    BorderPane root = (BorderPane) stage.getScene().getRoot();
    assertThat(root.getTop())
        .as("la zone top du BorderPane doit contenir un HBox")
        .isInstanceOf(HBox.class);
  }

  // --- Étape 3 : la zone de couleur ---

  @Test
  @Order(7)
  void laZoneDeCouleurExiste(FxRobot robot) {
    Pane zone = robot.lookup("#zone").queryAs(Pane.class);
    assertThat(zone).as("un Pane avec l'id 'zone' doit être présent au centre").isNotNull();
  }

  // --- Étape 4 : le label compteurs ---

  @Test
  @Order(8)
  void leLabelCompteursExiste(FxRobot robot) {
    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs).as("un Label avec l'id 'compteurs' doit être présent en bas").isNotNull();
  }

  // --- Étape 5 : texte initial via Bindings.when() ---

  @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(9)
  void leTexteInitialAvantClic(FxRobot robot) {
    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs.getText())
        .as("avant tout clic, le label doit afficher 'Bienvenue !' via Bindings.when()")
        .isEqualTo("Bienvenue !");
  }

  // --- Étape 6 : cliquer change la couleur ---

  @Test
  @Order(10)
  void cliquerRougeMetLaZoneEnRouge(FxRobot robot) {
    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    robot.interact(btnRouge::fire);
    Pane zone = robot.lookup("#zone").queryAs(Pane.class);
    assertThat(zone.getStyle())
        .as("la zone doit avoir un fond rouge après un clic sur Rouge")
        .contains("red");
  }

  @Test
  @Order(11)
  void cliquerVertMetLaZoneEnVert(FxRobot robot) {
    BoutonCouleur btnVert = robot.lookup("#btn-vert").queryAs(BoutonCouleur.class);
    robot.interact(btnVert::fire);
    Pane zone = robot.lookup("#zone").queryAs(Pane.class);
    assertThat(zone.getStyle())
        .as("la zone doit avoir un fond vert après un clic sur Vert")
        .contains("green");
  }

  // --- Étape 7 : le label se met à jour via binding ---

  @Test
  @Order(12)
  void cliquerIncrementeLeCompteur(FxRobot robot) {
    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    robot.interact(btnRouge::fire);
    robot.interact(btnRouge::fire);
    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs.getText())
        .as("après 2 clics sur Rouge, le label doit contenir 'Rouge: 2'")
        .contains("Rouge: 2");
  }

  @Test
  @Order(13)
  void leTexteBasculeDeBienvenueAuxCompteurs(FxRobot robot) {
    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs.getText()).as("avant clic : 'Bienvenue !'").isEqualTo("Bienvenue !");

    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    robot.interact(btnRouge::fire);
    assertThat(compteurs.getText())
        .as("après un clic, le texte doit basculer vers les compteurs")
        .contains("Rouge: 1");
  }

  // --- Étape 8 : compteurs indépendants ---

  @Test
  @Order(14)
  void lesCompteursSontIndependants(FxRobot robot) {
    BoutonCouleur btnRouge = robot.lookup("#btn-rouge").queryAs(BoutonCouleur.class);
    BoutonCouleur btnBleu = robot.lookup("#btn-bleu").queryAs(BoutonCouleur.class);
    robot.interact(btnRouge::fire);
    robot.interact(btnRouge::fire);
    robot.interact(btnBleu::fire);

    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs.getText()).as("Rouge doit valoir 2").contains("Rouge: 2");
    assertThat(compteurs.getText()).as("Vert doit valoir 0 (aucun clic)").contains("Vert: 0");
    assertThat(compteurs.getText()).as("Bleu doit valoir 1").contains("Bleu: 1");
  }

  // --- Étape 9 : vérifier que le binding fonctionne (pas de setText()) ---

  @Test
  @Order(15)
  void leLabelEstLieParBinding(FxRobot robot) {
    Label compteurs = robot.lookup("#compteurs").queryAs(Label.class);
    assertThat(compteurs.textProperty().isBound())
        .as("textProperty() du label compteurs doit être liée par bind() (pas par setText())")
        .isTrue();
  }
}
