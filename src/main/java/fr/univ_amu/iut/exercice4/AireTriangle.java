package fr.univ_amu.iut.exercice4;

import javafx.beans.binding.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Exercice 4 - Calcul de l'aire d'un triangle par bindings.
 *
 * <p>Cette classe modèle encapsule les coordonnées de trois points et calcule automatiquement
 * l'aire du triangle via la formule du déterminant :
 *
 * <pre>
 * aire = |x1(y2-y3) + x2(y3-y1) + x3(y1-y2)| / 2
 * </pre>
 *
 * <p>L'aire est recalculée <b>automatiquement</b> à chaque modification d'une coordonnée grâce aux
 * bindings JavaFX. Aucun appel manuel n'est nécessaire.
 *
 * <p>Cet exercice explore :
 *
 * <ul>
 *   <li>La convention JavaBeans complète : {@code getX1()}, {@code setX1()}, {@code x1Property()}
 *   <li>Les bindings de haut niveau : {@code Bindings.multiply()}, {@code subtract()}, etc.
 *   <li>L'API fluente : {@code x1.multiply(y2).subtract(...)}
 *   <li>{@code Bindings.format()} pour créer une {@link StringExpression}
 * </ul>
 *
 * @see <a href=
 *     "https://openjfx.io/javadoc/25/javafx.base/javafx/beans/binding/Bindings.html">Bindings</a>
 */
public class AireTriangle {

  private final IntegerProperty x1 = new SimpleIntegerProperty(0);
  private final IntegerProperty y1 = new SimpleIntegerProperty(0);
  private final IntegerProperty x2 = new SimpleIntegerProperty(0);
  private final IntegerProperty y2 = new SimpleIntegerProperty(0);
  private final IntegerProperty x3 = new SimpleIntegerProperty(0);
  private final IntegerProperty y3 = new SimpleIntegerProperty(0);

  private final DoubleProperty area = new SimpleDoubleProperty(0);

  private StringExpression output;

  public AireTriangle() {
    createBinding();
  }

  /**
   * Crée le binding qui calcule l'aire automatiquement.
   *
   * <p>Utiliser l'API fluente de JavaFX :
   *
   * <pre>
   * NumberBinding determinant = x1.multiply(y2).subtract(x1.multiply(y3))
   *     .add(x2.multiply(y3)).subtract(x2.multiply(y1))
   *     .add(x3.multiply(y1)).subtract(x3.multiply(y2));
   * </pre>
   *
   * <p>Puis lier {@code area} au résultat divisé par 2 (en valeur absolue). Créer aussi une {@link
   * StringExpression} via {@code Bindings.format()}.
   */
  private void createBinding() {
    // TODO exercice 4 : créer le binding pour calculer l'aire.
    //
    // Partie A - Formule du déterminant avec l'API fluente :
    //
    // 1. Calculer le déterminant comme NumberBinding :
    // determinant = x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)
    // En utilisant l'API fluente :
    NumberBinding determinant =
        x1.multiply(y2.subtract(y3))
            .add(x2.multiply(y3.subtract(y1)))
            .add(x3.multiply(y1.subtract(y2)));
    // 2. Lier area au déterminant divisé par 2 :
    area.bind(
        Bindings.when(determinant.greaterThanOrEqualTo(0))
            .then(determinant.divide(2.0))
            .otherwise(determinant.negate().divide(2.0)));
    //
    // Partie B - StringExpression pour l'affichage :
    //
    // 3. Créer output avec Bindings.format() :
    this.output =
        Bindings.format("P1(%s,%s) P2(%s,%s) P3(%s,%s) => aire = %s", x1, y1, x2, y2, x3, y3, area);
  }

  void printResult() {
    // TODO exercice 4 : afficher output.get() sur System.out.
    System.out.println(output.get());
  }

  // --- Setters de points ---

  public void setP1(int x, int y) {
    x1.set(x);
    y1.set(y);
  }

  public void setP2(int x, int y) {
    x2.set(x);
    y2.set(y);
  }

  public void setP3(int x, int y) {
    x3.set(x);
    y3.set(y);
  }

  // --- Convention JavaBeans pour x1 ---

  public int getX1() {
    return x1.get();
  }

  public void setX1(int value) {
    x1.set(value);
  }

  public IntegerProperty x1Property() {
    return x1;
  }

  // --- Convention JavaBeans pour y1 ---

  public int getY1() {
    return y1.get();
  }

  public void setY1(int value) {
    y1.set(value);
  }

  public IntegerProperty y1Property() {
    return y1;
  }

  // --- Convention JavaBeans pour x2 ---

  public int getX2() {
    return x2.get();
  }

  public void setX2(int value) {
    x2.set(value);
  }

  public IntegerProperty x2Property() {
    return x2;
  }

  // --- Convention JavaBeans pour y2 ---

  public int getY2() {
    return y2.get();
  }

  public void setY2(int value) {
    y2.set(value);
  }

  public IntegerProperty y2Property() {
    return y2;
  }

  // --- Convention JavaBeans pour x3 ---

  public int getX3() {
    return x3.get();
  }

  public void setX3(int value) {
    x3.set(value);
  }

  public IntegerProperty x3Property() {
    return x3;
  }

  // --- Convention JavaBeans pour y3 ---

  public int getY3() {
    return y3.get();
  }

  public void setY3(int value) {
    y3.set(value);
  }

  public IntegerProperty y3Property() {
    return y3;
  }

  // --- Accesseur aire ---

  public double getArea() {
    return area.get();
  }

  public DoubleProperty areaProperty() {
    return area;
  }

  public static void main(String[] args) {
    AireTriangle t = new AireTriangle();
    t.printResult();

    t.setP1(0, 1);
    t.setP2(6, 0);
    t.setP3(4, 3);
    t.printResult();

    t.setP1(1, 0);
    t.setP2(2, 2);
    t.setP3(0, 1);
    t.printResult();
  }
}
