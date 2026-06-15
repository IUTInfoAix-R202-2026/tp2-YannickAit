package fr.univ_amu.iut.exercice4;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** Tests de l'exercice 4 - AireTriangle (bindings de calcul). */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AireTriangleTest {

  private AireTriangle triangle;

  @BeforeEach
  void setUp() {
    triangle = new AireTriangle();
  }

  // --- Étape 1 : les propriétés existent ---

  @Test
  @Order(1)
  void testLesProprietesSontInitialiseesAZero() {
    assertThat(triangle.getX1()).as("x1 initial").isEqualTo(0);
    assertThat(triangle.getY1()).as("y1 initial").isEqualTo(0);
    assertThat(triangle.getX2()).as("x2 initial").isEqualTo(0);
    assertThat(triangle.getY2()).as("y2 initial").isEqualTo(0);
    assertThat(triangle.getX3()).as("x3 initial").isEqualTo(0);
    assertThat(triangle.getY3()).as("y3 initial").isEqualTo(0);
  }

  @Test
  @Order(2)
  void testLesProprietesJavaBeansFonctionnent() {
    triangle.setX1(5);
    assertThat(triangle.getX1()).as("getX1() après setX1(5)").isEqualTo(5);
    assertThat(triangle.x1Property().get()).as("x1Property().get() après setX1(5)").isEqualTo(5);
  }

  // --- Étape 2 : le binding calcule l'aire ---

  @Test
  @Order(3)
  void testTriangleVideAireZero() {
    assertThat(triangle.getArea())
        .as("l'aire d'un triangle dont tous les points sont en (0,0) doit être 0")
        .isEqualTo(0.0);
  }

  @Test
  @Order(4)
  void testTriangleUniteAireZeroVirguleCinq() {
    triangle.setP1(0, 0);
    triangle.setP2(1, 0);
    triangle.setP3(0, 1);
    assertThat(triangle.getArea())
        .as("P1(0,0) P2(1,0) P3(0,1) : l'aire du triangle unité doit être 0.5")
        .isEqualTo(0.5);
  }

  @Test
  @Order(5)
  void testTriangleCorrectArea() {
    triangle.setP1(0, 0);
    triangle.setP2(6, 0);
    triangle.setP3(4, 3);
    assertThat(triangle.getArea())
        .as("P1(0,0) P2(6,0) P3(4,3) : l'aire doit être 9.0")
        .isEqualTo(9.0);
  }

  @Test
  @Order(6)
  void testTriangleDeuxiemeCorrectArea() {
    triangle.setP1(1, 0);
    triangle.setP2(2, 2);
    triangle.setP3(0, 1);
    assertThat(triangle.getArea())
        .as("P1(1,0) P2(2,2) P3(0,1) : l'aire doit être 1.5")
        .isEqualTo(1.5);
  }

  // --- Étape 3 : valeur absolue ---

  @Test
  @Order(7)
  void testAirePositiveMemeAvecDeterminantNegatif() {
    // Même triangle que ci-dessus mais avec les points dans l'autre sens
    triangle.setP1(0, 1);
    triangle.setP2(2, 2);
    triangle.setP3(1, 0);
    assertThat(triangle.getArea())
        .as("l'aire doit etre positive quel que soit l'ordre des points (valeur absolue)")
        .isGreaterThan(0);
  }

  // --- Étape 4 : recalcul automatique ---

  @Test
  @Order(8)
  void testModifierCoordonneeRecalculeAire() {
    triangle.setP1(0, 0);
    triangle.setP2(1, 0);
    triangle.setP3(0, 1);
    assertThat(triangle.getArea()).as("aire initiale du triangle unite").isEqualTo(0.5);

    // Modifier un seul point : l'aire doit se recalculer automatiquement
    triangle.setP2(6, 0);
    triangle.setP3(4, 3);
    assertThat(triangle.getArea())
        .as("l'aire doit se recalculer automatiquement quand une coordonnée change")
        .isEqualTo(9.0);
  }

  @Test
  @Order(9)
  void testAreaPropertyEstLiee() {
    assertThat(triangle.areaProperty().isBound())
        .as("areaProperty() doit être liée par bind() (recalcul automatique)")
        .isTrue();
  }

  // --- Étape 5 : affichage formaté ---

  @Test
  @Order(10)
  void testPrintResultAfficheLeTexteAttendu() {
    triangle.setP1(0, 1);
    triangle.setP2(6, 0);
    triangle.setP3(4, 3);

    // Capturer la sortie console
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.PrintStream original = System.out;
    System.setOut(new java.io.PrintStream(baos));

    triangle.printResult();

    System.setOut(original);
    String sortie = baos.toString().trim();

    assertThat(sortie)
        .as("printResult() doit afficher les coordonnées et l'aire formatée")
        .contains("P1(")
        .contains("P2(")
        .contains("P3(")
        .contains("aire");
  }
}
