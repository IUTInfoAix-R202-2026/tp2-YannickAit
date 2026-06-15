package fr.univ_amu.iut.exercice5;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests de l'exercice 5 - Calculatrice de triangle avec dessin. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalculatriceTriangleTest {

  private Stage stage;

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null); // évite la fuite de Scene entre tests (TestFX réutilise le Stage)
    this.stage = stage;
    new CalculatriceTriangle().start(stage);
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(1)
  void laFenetreEstVisible(FxRobot robot) {
    assertThat(stage.isShowing()).as("le Stage doit être affiché").isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(2)
  void laSceneExiste(FxRobot robot) {
    assertThat(stage.getScene()).as("le Stage doit avoir une Scene").isNotNull();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(3)
  void laRacineEstUnGridPane(FxRobot robot) {
    assertThat(stage.getScene().getRoot())
        .as("la racine doit être un GridPane")
        .isInstanceOf(GridPane.class);
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(4)
  void lesSixSlidersExistent(FxRobot robot) {
    assertThat(robot.lookup("#slider-x1").queryAs(Slider.class)).as("slider-x1").isNotNull();
    assertThat(robot.lookup("#slider-y1").queryAs(Slider.class)).as("slider-y1").isNotNull();
    assertThat(robot.lookup("#slider-x2").queryAs(Slider.class)).as("slider-x2").isNotNull();
    assertThat(robot.lookup("#slider-y2").queryAs(Slider.class)).as("slider-y2").isNotNull();
    assertThat(robot.lookup("#slider-x3").queryAs(Slider.class)).as("slider-x3").isNotNull();
    assertThat(robot.lookup("#slider-y3").queryAs(Slider.class)).as("slider-y3").isNotNull();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(5)
  void lesSlidersOntDesTickMarks(FxRobot robot) {
    Slider s = robot.lookup("#slider-x1").queryAs(Slider.class);
    assertThat(s.isShowTickLabels()).as("slider doit afficher les labels").isTrue();
    assertThat(s.isShowTickMarks()).as("slider doit afficher les marks").isTrue();
    assertThat(s.isSnapToTicks()).as("slider doit snapper aux ticks").isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(6)
  void leTextFieldAireExiste(FxRobot robot) {
    TextField tf = robot.lookup("#aire").queryAs(TextField.class);
    assertThat(tf).as("un TextField avec id 'aire' doit exister").isNotNull();
    assertThat(tf.isEditable()).as("le TextField aire ne doit pas être éditable").isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(7)
  void leTextFieldAireEstLieParBinding(FxRobot robot) {
    TextField tf = robot.lookup("#aire").queryAs(TextField.class);
    assertThat(tf.textProperty().isBound())
        .as("textProperty() du TextField aire doit être liée par bind()")
        .isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(8)
  void deplacerSliderModifieAire(FxRobot robot) {
    Slider sx2 = robot.lookup("#slider-x2").queryAs(Slider.class);
    Slider sy3 = robot.lookup("#slider-y3").queryAs(Slider.class);
    // Triangle P1(0,0) P2(6,0) P3(0,3) -> aire = 9
    robot.interact(() -> sx2.setValue(6));
    robot.interact(() -> sy3.setValue(3));

    TextField tf = robot.lookup("#aire").queryAs(TextField.class);
    assertThat(tf.getText())
        .as("l'aire doit se mettre à jour quand les sliders changent")
        .contains("9");
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(9)
  void lePanneauDessinExiste(FxRobot robot) {
    Pane dessin = robot.lookup("#dessin").queryAs(Pane.class);
    assertThat(dessin).as("un Pane avec id 'dessin' doit exister").isNotNull();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(10)
  void lesTroisLignesExistent(FxRobot robot) {
    Pane dessin = robot.lookup("#dessin").queryAs(Pane.class);
    long nbLignes = dessin.getChildren().stream().filter(n -> n instanceof Line).count();
    assertThat(nbLignes).as("le panneau de dessin doit contenir 3 lignes (Line)").isEqualTo(3);
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(11)
  void deplacerSliderModifieLeDessin(FxRobot robot) {
    Slider sx1 = robot.lookup("#slider-x1").queryAs(Slider.class);
    Slider sy1 = robot.lookup("#slider-y1").queryAs(Slider.class);
    robot.interact(() -> sx1.setValue(5));
    robot.interact(() -> sy1.setValue(3));

    Pane dessin = robot.lookup("#dessin").queryAs(Pane.class);
    Line premiereLigne =
        (Line)
            dessin.getChildren().stream().filter(n -> n instanceof Line).findFirst().orElse(null);
    assertThat(premiereLigne).as("au moins une ligne doit exister").isNotNull();
    // Avec facteur 50, x=5 -> startX=250, y=3 -> startY=150
    assertThat(premiereLigne.getStartX())
        .as("startX de la première ligne doit être 5*50=250")
        .isEqualTo(250.0);
    assertThat(premiereLigne.getStartY())
        .as("startY de la première ligne doit être 3*50=150")
        .isEqualTo(150.0);
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(12)
  void aireCorrectePourTriangleConnu(FxRobot robot) {
    // Triangle P1(0,0) P2(1,0) P3(0,1) -> aire = 0.5
    Slider sx2 = robot.lookup("#slider-x2").queryAs(Slider.class);
    Slider sy3 = robot.lookup("#slider-y3").queryAs(Slider.class);
    robot.interact(() -> sx2.setValue(1));
    robot.interact(() -> sy3.setValue(1));

    TextField tf = robot.lookup("#aire").queryAs(TextField.class);
    assertThat(tf.getText()).as("P1(0,0) P2(1,0) P3(0,1) -> aire = 0.5").contains("0.5");
  }
}
