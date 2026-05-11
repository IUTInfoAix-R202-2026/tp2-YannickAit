package fr.univ_amu.iut.exercice3;

import static org.assertj.core.api.Assertions.assertThat;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

/** Tests unitaires de BoutonCouleur - convention JavaBeans et compteur. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoutonCouleurTest {

  @Test
  @Order(1)
  void testLeTexteEstCorrect() {
    BoutonCouleur btn = new BoutonCouleur("Rouge", "red");
    assertThat(btn.getText())
        .as("Le texte du bouton doit être celui passé au constructeur")
        .isEqualTo("Rouge");
  }

  @Test
  @Order(2)
  void testLaCouleurEstStockee() {
    BoutonCouleur btn = new BoutonCouleur("Vert", "green");
    assertThat(btn.getCouleur())
        .as("getCouleur() doit retourner la couleur passée au constructeur")
        .isEqualTo("green");
  }

  @Test
  @Order(3)
  void testNbClicsInitialEstZero() {
    BoutonCouleur btn = new BoutonCouleur("Bleu", "blue");
    assertThat(btn.getNbClics()).as("getNbClics() doit retourner 0 avant tout clic").isEqualTo(0);
  }

  @Test
  @Order(4)
  void testNbClicsPropertyRetourneUnePropriete() {
    BoutonCouleur btn = new BoutonCouleur("Rouge", "red");
    IntegerProperty prop = btn.nbClicsProperty();
    assertThat(prop)
        .as("nbClicsProperty() doit retourner une IntegerProperty non null")
        .isNotNull();
    assertThat(prop.get())
        .as("La propriété doit avoir la même valeur que getNbClics()")
        .isEqualTo(btn.getNbClics());
  }

  @Test
  @Order(5)
  void testClicIncrementeNbClics() {
    BoutonCouleur btn = new BoutonCouleur("Rouge", "red");
    btn.fire();
    assertThat(btn.getNbClics()).as("Après un clic, getNbClics() doit valoir 1").isEqualTo(1);
  }

  @Test
  @Order(6)
  void testDeuxClicsIncrementent() {
    BoutonCouleur btn = new BoutonCouleur("Rouge", "red");
    btn.fire();
    btn.fire();
    assertThat(btn.getNbClics()).as("Après deux clics, getNbClics() doit valoir 2").isEqualTo(2);
  }

  @Test
  @Order(7)
  void testLaPropertyEstLieeAuCompteur() {
    BoutonCouleur btn = new BoutonCouleur("Rouge", "red");
    btn.fire();
    assertThat(btn.nbClicsProperty().get())
        .as("nbClicsProperty().get() doit refléter le compteur après un clic")
        .isEqualTo(1);
  }
}
