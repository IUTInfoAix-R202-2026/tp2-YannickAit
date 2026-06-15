package fr.univ_amu.iut.exercice6;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests de l'exercice 6 - Formulaire de connexion avec validation par bindings. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FormulaireConnexionTest {

  private Stage stage;

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null); // évite la fuite de Scene entre tests (TestFX réutilise le Stage)
    this.stage = stage;
    new FormulaireConnexion().start(stage);
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
  void leChampIdentifiantExiste(FxRobot robot) {
    TextField tf = robot.lookup("#user-id").queryAs(TextField.class);
    assertThat(tf).as("un TextField avec id 'user-id' doit exister").isNotNull();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(3)
  void leChampMotDePasseExiste(FxRobot robot) {
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    assertThat(pf).as("un PasswordField avec id 'pwd' doit exister").isNotNull();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(4)
  void leChampMotDePasseEstNonEditableAuDebut(FxRobot robot) {
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    assertThat(pf.isEditable())
        .as("le champ mot de passe doit être non éditable au démarrage (userId vide)")
        .isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(5)
  void saisirSixCaracteresRendMotDePasseEditable(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);

    robot.interact(() -> userId.setText("abcdef"));

    assertThat(pf.isEditable())
        .as("après 6 caractères dans userId, le champ mot de passe doit devenir éditable")
        .isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(6)
  void cinqCaracteresNeSuffitPas(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);

    robot.interact(() -> userId.setText("abcde"));

    assertThat(pf.isEditable())
        .as("5 caractères ne suffisent pas - le champ mot de passe reste non éditable")
        .isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(7)
  void boutonOkDesactiveInitialement(FxRobot robot) {
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);
    assertThat(ok.isDisabled())
        .as("le bouton OK doit être désactivé au démarrage (mot de passe vide)")
        .isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(8)
  void boutonOkDesactiveSiMotDePasseTropCourt(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("Ab1"));

    assertThat(ok.isDisabled())
        .as("mot de passe trop court (< 8 chars) - OK reste désactivé")
        .isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(9)
  void motDePasseSansChiffreGardeOkDesactive(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("Abcdefgh"));

    assertThat(ok.isDisabled()).as("mot de passe sans chiffre - OK reste désactivé").isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(10)
  void motDePasseSansMajusculeGardeOkDesactive(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("abcdefg1"));

    assertThat(ok.isDisabled()).as("mot de passe sans majuscule - OK reste désactivé").isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(11)
  void motDePasseValideActiveOk(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("Abcdefg1"));

    assertThat(ok.isDisabled())
        .as("mot de passe valide (>= 8 chars + majuscule + chiffre) - OK doit être actif")
        .isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(12)
  void boutonAnnulerDesactiveSiChampsVides(FxRobot robot) {
    Button cancel = robot.lookup("#btn-cancel").queryAs(Button.class);
    assertThat(cancel.isDisabled())
        .as("bouton Annuler désactivé si les deux champs sont vides")
        .isTrue();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(13)
  void boutonAnnulerActifSiUnChampRempli(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    Button cancel = robot.lookup("#btn-cancel").queryAs(Button.class);

    robot.interact(() -> userId.setText("a"));

    assertThat(cancel.isDisabled())
        .as("bouton Annuler actif dès qu'un champ contient du texte")
        .isFalse();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(14)
  void boutonAnnulerVideLesChamps(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button cancel = robot.lookup("#btn-cancel").queryAs(Button.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("motdepasse"));
    robot.interact(cancel::fire);

    assertThat(userId.getText()).as("userId doit être vide après Annuler").isEmpty();
    assertThat(pf.getText()).as("pwd doit être vide après Annuler").isEmpty();
  }

  // @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(15)
  void boutonOkAfficheLMessage(FxRobot robot) {
    TextField userId = robot.lookup("#user-id").queryAs(TextField.class);
    PasswordField pf = robot.lookup("#pwd").queryAs(PasswordField.class);
    Button ok = robot.lookup("#btn-ok").queryAs(Button.class);
    Label msg = robot.lookup("#message").queryAs(Label.class);

    robot.interact(() -> userId.setText("utilisateur"));
    robot.interact(() -> pf.setText("Abcdefg1"));
    robot.interact(ok::fire);

    assertThat(msg.getText()).as("le message doit contenir l'identifiant").contains("utilisateur");
    assertThat(msg.getText())
        .as("le message doit masquer le mot de passe avec des étoiles")
        .contains("********");
  }
}
