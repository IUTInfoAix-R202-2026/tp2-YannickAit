#!/usr/bin/env bash
#
# Regenere les captures "Resultat attendu" des apps GUI du TP2 (branche
# solution) dans .github/assets/apercu-*.png.
#
# Outil de MAINTENANCE (enseignant) : a relancer apres modification des
# exercices. Les exercices 1, 2 (console) et 4 (modele pur) n'ont pas de
# fenetre : pas de capture.
#
# Prerequis : xvfb (xvfb-run) + ImageMagick (import, convert).
# Usage (depuis la racine du TP) :
#   ./.github/assets/capture-screenshots.sh            # toutes les captures
#   ./.github/assets/capture-screenshots.sh ex3        # une seule
#
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT"
ASSETS=.github/assets
mkdir -p "$ASSETS"
SCREEN="-screen 0 1366x900x24"
WAIT=24

declare -A APP=(
  [lanceur]="tp2.javafx/fr.univ_amu.iut.App|apercu-lanceur"
  [ex3]="tp2.javafx/fr.univ_amu.iut.exercice3.PaletteReactive|apercu-ex3-palette-reactive"
  [ex5]="tp2.javafx/fr.univ_amu.iut.exercice5.CalculatriceTriangle|apercu-ex5-calculatrice-triangle"
  [ex6]="tp2.javafx/fr.univ_amu.iut.exercice6.FormulaireConnexion|apercu-ex6-formulaire-connexion"
  [ex6valide]="tp2.javafx/fr.univ_amu.iut.exercice6.FormulaireConnexion|apercu-ex6-formulaire-connexion-valide"
  [ex7]="tp2.javafx/fr.univ_amu.iut.exercice7.CercleInteractif|apercu-ex7-cercle-interactif"
  [ex8]="tp2.javafx/fr.univ_amu.iut.exercice8.ConvertisseurTemperatures|apercu-ex8-convertisseur"
  [bonus9]="tp2.javafx/fr.univ_amu.iut.bonus9.BalleRebondissante|apercu-bonus9-balle"
  [bonus10]="tp2.javafx/fr.univ_amu.iut.bonus10.SlowPong|apercu-bonus10-pong"
)
# ex6 = etat initial (avant), ex6valide = formulaire rempli (apres) : la maquette
# montre les deux. ex6valide pilote la saisie via xdotool.
ORDER=(lanceur ex3 ex5 ex6 ex6valide ex7 ex8 bonus9 bonus10)

# $1 = mainClass, $2 = nom de sortie, $3 = cle.
# Certaines apps sont pilotees via xdotool avant la capture (ex6valide : on
# remplit le formulaire pour montrer l'etat valide). Variables passees par
# l'environnement -> bash -c en quotes simples (pas d'echappement).
capture() {
  local main="$1" out="$2" key="${3:-}"
  MAIN="$main" OUT="$out" KEY="$key" WAITS="$WAIT" \
    xvfb-run -a --server-args="$SCREEN" bash -c '
      ./mvnw -q -Djavafx.mainClass="$MAIN" javafx:run >"/tmp/cap-$OUT.log" 2>&1 &
      M=$!
      sleep "$WAITS"
      if [ "$KEY" = ex6valide ] && command -v xdotool >/dev/null 2>&1; then
        # Remplit le formulaire (identifiant + mot de passe valides) puis clique
        # OK : OK passe au vert, Annuler au rouge, le message s affiche.
        eval "$(xdotool search --class iut.exercice6.FormulaireConnexion getwindowgeometry --shell 2>/dev/null | grep -E "^(X|Y)=")"
        if [ -n "${X:-}" ]; then
          xdotool mousemove $((X + 210)) $((Y + 33)) click 1; sleep 0.3; xdotool type "utilisateur"; sleep 0.3
          xdotool mousemove $((X + 210)) $((Y + 70)) click 1; sleep 0.3; xdotool type "Motdepasse1"; sleep 0.3
          xdotool mousemove $((X + 65)) $((Y + 109)) click 1; sleep 0.5
        fi
      fi
      if [ "$KEY" = bonus10 ] && command -v xdotool >/dev/null 2>&1; then
        # Clique "Start" (centre) : le bouton se cache et la balle apparait,
        # pour montrer la partie en cours plutot que l ecran de depart.
        eval "$(xdotool search --class iut.bonus10.SlowPong getwindowgeometry --shell 2>/dev/null | grep -E "^(X|Y)=")"
        if [ -n "${X:-}" ]; then
          xdotool mousemove $((X + 250)) $((Y + 253)) click 1; sleep 0.6
        fi
      fi
      import -window root "/tmp/$OUT-raw.png" 2>/dev/null
      ap=$(pgrep -f "enable-native-access=javafx.graphics --module-path" | head -1)
      [ -n "$ap" ] && kill -9 "$ap" 2>/dev/null
      kill -9 "$M" 2>/dev/null
      exit 0
    '
  convert "/tmp/$out-raw.png" -trim +repage "$ASSETS/$out.png"
  echo "  $out.png  ($(identify -format '%wx%h' "$ASSETS/$out.png" 2>/dev/null))"
}

echo "Pre-compilation..."
./mvnw -q compile

if [ "$#" -ge 1 ]; then
  echo "Captures demandees : $* -> $ASSETS/"
  for k in "$@"; do
    entry="${APP[$k]:-}"
    if [ -z "$entry" ]; then echo "Cle inconnue : $k (attendu : ${ORDER[*]})" >&2; continue; fi
    capture "${entry%%|*}" "${entry##*|}" "$k"
  done
else
  echo "Toutes les captures -> $ASSETS/"
  for k in "${ORDER[@]}"; do
    entry="${APP[$k]}"
    capture "${entry%%|*}" "${entry##*|}" "$k"
  done
fi

pgrep -f "enable-native-access=javafx.graphics --module-path" | xargs -r kill -9 2>/dev/null
echo "Termine."
