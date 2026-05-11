package fr.univ_amu.iut.exercice3;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;

/**
 * Bouton qui stocke son propre état via une {@link IntegerProperty}.
 *
 * <p>Chaque clic incrémente le compteur interne. Le composant expose le triplet JavaBeans ({@code
 * getNbClics()}, {@code nbClicsProperty()}) pour que d'autres composants puissent s'y lier via des
 * bindings.
 *
 * <p>Ce pattern (composant qui possède son propre état observable) est fondamental en JavaFX : il
 * permet la <b>séparation des préoccupations</b> entre le composant (qui gère son état) et le reste
 * de l'application (qui observe les changements).
 */
public class BoutonCouleur extends Button {

  // TODO exercice 3 : ajouter une IntegerProperty nbClics et une String couleur.
  //
  // 1. Déclarer un champ IntegerProperty nbClics (SimpleIntegerProperty, valeur 0).
  //
  // 2. Déclarer un champ String couleur (la couleur CSS du bouton, ex: "red").
  //
  // 3. Implémenter le constructeur BoutonCouleur(String texte, String couleur) :
  //    - appeler super(texte)
  //    - stocker la couleur
  //    - ajouter un handler setOnAction qui incrémente nbClics de 1
  //
  // 4. Implémenter les accesseurs JavaBeans :
  //    - int getNbClics()
  //    - IntegerProperty nbClicsProperty()
  //    - String getCouleur()

  private final IntegerProperty nbClics = new SimpleIntegerProperty(0);
  private String couleur = "";

  /** Constructeur par défaut (nécessaire pour la version étudiante). */
  public BoutonCouleur() {
    super();
  }

  /** Crée un bouton de couleur avec le texte et la couleur CSS donnés. */
  public BoutonCouleur(String texte, String couleur) {
    super(texte);
    this.couleur = couleur;
    addEventHandler(javafx.event.ActionEvent.ACTION, e -> nbClics.set(getNbClics() + 1));
  }

  public int getNbClics() {
    return nbClics.get();
  }

  public IntegerProperty nbClicsProperty() {
    return nbClics;
  }

  public String getCouleur() {
    return couleur;
  }
}
