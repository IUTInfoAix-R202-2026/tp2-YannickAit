package fr.univ_amu.iut.exercice2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Exercice 2 - Liaison unidirectionnelle entre propriétés.
 *
 * <p>Une propriété peut être <b>liée</b> à une autre via {@code bind()} : elle suivra
 * automatiquement la valeur de la source. C'est le mécanisme fondamental de propagation automatique
 * des changements dans JavaFX.
 *
 * <p>Concepts découverts :
 *
 * <ul>
 *   <li>{@code bind()} : la cible suit automatiquement la source
 *   <li>{@code unbind()} : rompre la liaison
 *   <li>Une propriété liée ne peut pas être modifiée directement (RuntimeException)
 *   <li>{@code isBound()} : vérifier si une propriété est liée
 * </ul>
 *
 * @see <a
 *     href="https://openjfx.io/javadoc/25/javafx.base/javafx/beans/property/IntegerProperty.html#bind(javafx.beans.value.ObservableValue)">IntegerProperty.bind()</a>
 */
public class LiaisonProprietes {

  private IntegerProperty anIntProperty;

  /**
   * Crée une deuxième propriété, la lie à {@code anIntProperty}, modifie la source, puis rompt la
   * liaison.
   *
   * <p>Sortie attendue (si anIntProperty vaut 1024 au départ) :
   *
   * <pre>
   * otherProperty.get() = 0
   * Binding otherProperty to anIntProperty.
   * otherProperty.get() = 1024
   * Calling anIntProperty.set(7168).
   * otherProperty.get() = 7168
   * otherProperty.get() = 7168
   * otherProperty.get() = 7168
   * Unbinding otherProperty from anIntProperty.
   * otherProperty.get() = 7168
   * Calling anIntProperty.set(8192).
   * otherProperty.get() = 7168
   * </pre>
   */
  void lierEtDelierProprietes() {
    // TODO exercice 2 : lier une propriété à une autre et observer la propagation.
    //
    // 1. Créer une nouvelle IntegerProperty appelée otherProperty (valeur par défaut 0).
    //
    // 2. Afficher une ligne vide, puis "otherProperty.get() = " + otherProperty.get()
    //    -> Affiche 0 (valeur par défaut, pas encore liée).
    //
    // 3. Afficher "Binding otherProperty to anIntProperty."
    //    Appeler otherProperty.bind(anIntProperty).
    //
    // 4. Afficher "otherProperty.get() = " + otherProperty.get()
    //    -> Affiche 1024 (la liaison a propagé la valeur de la source).
    //
    // 5. Afficher "Calling anIntProperty.set(7168)."
    //    Appeler anIntProperty.set(7168).
    //
    // 6. Afficher 3 fois "otherProperty.get() = " + otherProperty.get()
    //    -> Affiche 7168 à chaque fois (la liaison propage automatiquement).
    //
    // 7. Afficher "Unbinding otherProperty from anIntProperty."
    //    Appeler otherProperty.unbind().
    //
    // 8. Afficher "otherProperty.get() = " + otherProperty.get()
    //    -> Affiche toujours 7168 (la dernière valeur avant unbind).
    //
    // 9. Afficher "Calling anIntProperty.set(8192)."
    //    Appeler anIntProperty.set(8192).
    //
    // 10. Afficher "otherProperty.get() = " + otherProperty.get()
    //     -> Affiche 7168 (plus de liaison, la cible ne suit plus).

    IntegerProperty anIntProperty = new SimpleIntegerProperty(1024);
    IntegerProperty otherProperty = new SimpleIntegerProperty(0);
    System.out.println(" ");
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("Binding otherProperty to anIntProperty.");
    otherProperty.bind(anIntProperty);
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("Calling anIntProperty.set(7168).");
    anIntProperty.set(7168);
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("Unbinding otherProperty from anIntProperty.");
    otherProperty.unbind();
    System.out.println("otherProperty.get() = " + otherProperty.get());
    System.out.println("Calling anIntProperty.set(8192).");
    anIntProperty.set(8192);
    System.out.println("otherProperty.get() = " + otherProperty.get());
  }

  public int getAnInt() {
    return anIntProperty.get();
  }

  public void setAnInt(int value) {
    if (anIntProperty == null) {
      anIntProperty = new SimpleIntegerProperty();
    }
    anIntProperty.set(value);
  }

  public IntegerProperty anIntProperty() {
    return anIntProperty;
  }

  public static void main(String[] args) {
    LiaisonProprietes exemple = new LiaisonProprietes();
    exemple.setAnInt(1024);
    exemple.lierEtDelierProprietes();
  }
}
