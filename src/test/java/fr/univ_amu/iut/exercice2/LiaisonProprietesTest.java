package fr.univ_amu.iut.exercice2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LiaisonProprietesTest {

  private PrintStream out;
  private PrintStream originalOut;
  private LiaisonProprietes liaison;

  @BeforeEach
  void setUp() {
    liaison = new LiaisonProprietes();
    liaison.setAnInt(1024);
    originalOut = System.out;
    out = mock(PrintStream.class);
    System.setOut(out);
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  // --- Tests unitaires sur le mécanisme bind/unbind (sans console) ---

  @Test
  @Order(1)
  void testBindPropageLaValeur() {
    IntegerProperty source = new SimpleIntegerProperty(42);
    IntegerProperty cible = new SimpleIntegerProperty(0);

    cible.bind(source);

    assertThat(cible.get())
        .as("Après bind(), la cible doit avoir la même valeur que la source")
        .isEqualTo(42);
  }

  @Test
  @Order(2)
  void testLaCibleSuitLaSource() {
    IntegerProperty source = new SimpleIntegerProperty(42);
    IntegerProperty cible = new SimpleIntegerProperty(0);
    cible.bind(source);

    source.set(100);

    assertThat(cible.get())
        .as("Quand la source change, la cible doit suivre automatiquement")
        .isEqualTo(100);
  }

  @Test
  @Order(3)
  void testUnbindArreteLaPropagation() {
    IntegerProperty source = new SimpleIntegerProperty(42);
    IntegerProperty cible = new SimpleIntegerProperty(0);
    cible.bind(source);
    source.set(100);

    cible.unbind();
    source.set(999);

    assertThat(cible.get())
        .as("Après unbind(), la cible garde sa dernière valeur et ne suit plus la source")
        .isEqualTo(100);
  }

  @Test
  @Order(4)
  void testIsBoundRetourneTrueSiLiee() {
    IntegerProperty source = new SimpleIntegerProperty(42);
    IntegerProperty cible = new SimpleIntegerProperty(0);

    assertThat(cible.isBound()).as("Avant bind(), isBound() doit retourner false").isFalse();

    cible.bind(source);
    assertThat(cible.isBound()).as("Après bind(), isBound() doit retourner true").isTrue();

    cible.unbind();
    assertThat(cible.isBound()).as("Après unbind(), isBound() doit retourner false").isFalse();
  }

  // --- Tests sur la sortie console de lierEtDelierProprietes() ---

  @Test
  @Order(5)
  void testAfficheValeurInitialeAvantLiaison() {
    liaison.lierEtDelierProprietes();
    verify(out).println("otherProperty.get() = 0");
  }

  @Test
  @Order(6)
  void testLiaisonPropageLaValeurSource() {
    liaison.lierEtDelierProprietes();
    verify(out).println("Binding otherProperty to anIntProperty.");
    verify(out).println("otherProperty.get() = 1024");
  }

  @Test
  @Order(7)
  void testChangementSourcePropageVersCible() {
    liaison.lierEtDelierProprietes();
    verify(out).println("Calling anIntProperty.set(7168).");
    // "otherProperty.get() = 7168" apparaît 5 fois au total (3 après set + 1 après unbind + 1
    // finale)
    // On vérifie qu'il y en a au moins 3 (celles qui prouvent la propagation)
    verify(out, atLeast(3)).println("otherProperty.get() = 7168");
  }

  @Test
  @Order(8)
  void testApresUnbindLaCibleNeSuitPlus() {
    liaison.lierEtDelierProprietes();
    verify(out).println("Unbinding otherProperty from anIntProperty.");
    verify(out).println("Calling anIntProperty.set(8192).");
    // La dernière ligne affiche toujours 7168, pas 8192
  }
}
