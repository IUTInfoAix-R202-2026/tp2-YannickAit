package fr.univ_amu.iut.exercice1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProprieteSimpleTest {

  @Mock private PrintStream out;
  private PrintStream originalOut;
  private ProprieteSimple propriete;

  @BeforeEach
  void setUp() {
    openMocks(this);
    originalOut = System.out;
    System.setOut(out);
    propriete = new ProprieteSimple();
    propriete.setAnInt(1024);
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  // --- creerPropriete() ---

  @Test
  @Order(1)
  void testLaProprieteExisteApresSetAnInt() {
    assertThat(propriete.anIntProperty())
        .as("anIntProperty() doit retourner une propriété non null après setAnInt()")
        .isNotNull();
  }

  @Test
  @Order(2)
  void testLaValeurInitialeEst1024() {
    assertThat(propriete.getAnInt())
        .as("getAnInt() doit retourner la valeur définie par setAnInt(1024)")
        .isEqualTo(1024);
  }

  @Test
  @Order(3)
  void testCreerProprieteAfficheLeToString() {
    propriete.creerPropriete();
    verify(out).println("anIntProperty = IntegerProperty [value: 1024]");
  }

  @Test
  @Order(4)
  void testCreerProprieteAfficheGet() {
    propriete.creerPropriete();
    verify(out).println("anIntProperty.get() = 1024");
  }

  @Disabled("Retire cette annotation pour activer le test")
  @Test
  @Order(5)
  void testCreerProprieteAfficheGetValue() {
    propriete.creerPropriete();
    verify(out).println("anIntProperty.getValue() = 1024");
  }

  // --- ajouterEtRetirerInvalidationListener() ---

  @Test
  @Order(6)
  void testInvalidationListenerEstDeclenche() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();
    verify(out).println("The observable has been invalidated.");
  }

  @Test
  @Order(7)
  void testInvalidationListenerPasDeclencheSiMemeValeur() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();

    // "Add invalidation listener." est affiché
    verify(out).println("Add invalidation listener.");
    // "setValue() with 1024." est affiché (mais le listener ne se déclenche pas)
    verify(out).println("setValue() with 1024.");
    // "set() with 2105." est affiché, PUIS le listener se déclenche
    verify(out).println("set() with 2105.");
    verify(out).println("The observable has been invalidated.");
  }

  @Test
  @Order(8)
  void testInvalidationListenerEstParesseux() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();

    // Après set(2105) le listener se déclenche, mais après setValue(5012)
    // il ne se redéclenche PAS car get() n'a pas été appelé entre-temps
    verify(out).println("setValue() with 5012.");
    // Le message "The observable has been invalidated." n'apparait qu'une seule fois
  }

  @Test
  @Order(9)
  void testInvalidationListenerRetireFonctionne() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();

    verify(out).println("Remove invalidation listener.");
    verify(out).println("set() with 1024.");
    // Après le retrait, set(1024) ne déclenche plus rien
  }

  // --- ajouterEtRetirerChangeListener() ---

  @Test
  @Order(10)
  void testChangeListenerEstDeclencheAvecAncienneEtNouvelleValeur() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();
    propriete.ajouterEtRetirerChangeListener();

    verify(out).println("The observableValue has changed: oldValue = 1024, newValue = 2105");
  }

  @Test
  @Order(11)
  void testChangeListenerDeclencheAChaqueMiseAJour() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();
    propriete.ajouterEtRetirerChangeListener();

    // Contrairement à l'InvalidationListener, le ChangeListener se déclenche
    // à CHAQUE changement de valeur, pas seulement au premier
    verify(out).println("The observableValue has changed: oldValue = 1024, newValue = 2105");
    verify(out).println("The observableValue has changed: oldValue = 2105, newValue = 5012");
  }

  @Test
  @Order(12)
  void testChangeListenerRetireFonctionne() {
    propriete.creerPropriete();
    propriete.ajouterEtRetirerInvalidationListener();
    propriete.ajouterEtRetirerChangeListener();

    verify(out).println("Remove change listener.");
    // "set() with 1024." apparaît dans les deux méthodes (invalidation + change),
    // on vérifie qu'il a été appelé au moins 2 fois
    verify(out, atLeast(2)).println("set() with 1024.");
  }
}
