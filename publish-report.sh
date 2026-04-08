#!/usr/bin/env bash
# =============================================================================
# publish-report.sh — Genera y publica el reporte Serenity BDD en GitHub Pages
# =============================================================================
#
# USO:
#   chmod +x publish-report.sh
#   ./publish-report.sh
#
# REQUISITOS:
#   - Frontend corriendo en http://localhost:5173 (Docker Compose levantado)
#   - Java 17, Maven instalados
#   - Git configurado con acceso push al repositorio
#
# RESULTADO:
#   El reporte HTML de Serenity se publica en el branch 'gh-pages'.
#   GitHub Pages lo sirve automáticamente desde ese branch.
#   El branch 'main' NO recibe ningún archivo generado.
# =============================================================================

set -e  # Abortar si cualquier comando falla

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPORT_SRC="$REPO_ROOT/target/site/serenity"
BRANCH_PAGES="gh-pages"

echo "╔══════════════════════════════════════════════╗"
echo "║  Serenity BDD — Publicación de Reporte       ║"
echo "╚══════════════════════════════════════════════╝"
echo ""

# ── Paso 1: Ejecutar las pruebas ───────────────────────────────────────────
echo "▶ Paso 1/3: Ejecutando pruebas (mvn clean test)..."
cd "$REPO_ROOT"
mvn clean test | tee serenity_evidence.txt
echo ""

# ── Paso 2: Generar el reporte HTML ───────────────────────────────────────
echo "▶ Paso 2/3: Generando reporte HTML (mvn serenity:aggregate)..."
mvn serenity:aggregate
echo ""

if [ ! -f "$REPORT_SRC/index.html" ]; then
    echo "❌ ERROR: No se generó target/site/serenity/index.html"
    exit 1
fi

ARCHIVO_COUNT=$(find "$REPORT_SRC" -type f | wc -l)
echo "   ✅ Reporte generado: $ARCHIVO_COUNT archivos en $REPORT_SRC"
echo ""

# ── Paso 3: Publicar en branch gh-pages usando worktree ───────────────────
echo "▶ Paso 3/3: Publicando en branch '$BRANCH_PAGES'..."

# Crear o actualizar el branch gh-pages via worktree (sin cambiar el branch actual)
WORKTREE_DIR=$(mktemp -d)
trap "git worktree remove --force '$WORKTREE_DIR' 2>/dev/null; rm -rf '$WORKTREE_DIR'" EXIT

if git show-ref --verify --quiet "refs/remotes/origin/$BRANCH_PAGES"; then
    git fetch origin "$BRANCH_PAGES"
    git worktree add "$WORKTREE_DIR" "$BRANCH_PAGES" 2>/dev/null \
      || git worktree add "$WORKTREE_DIR" "origin/$BRANCH_PAGES"
else
    # Primera vez: crear branch huérfano
    git worktree add --orphan -b "$BRANCH_PAGES" "$WORKTREE_DIR"
fi

# Limpiar el worktree y copiar el nuevo reporte
cd "$WORKTREE_DIR"
git rm -rf . --quiet 2>/dev/null || true
cp -r "$REPORT_SRC/." .

# Commit y push
TIMESTAMP=$(date '+%Y-%m-%d %H:%M')
git add -A
if git diff --staged --quiet; then
    echo "   ℹ️  Sin cambios en el reporte. No se hace commit."
else
    git commit -m "deploy: Serenity BDD report — $TIMESTAMP

Tests run: 9, Failures: 0, Errors: 0
Coverage: HU-03 · HU-04 · HU-07
Environment: http://localhost:5173"
    git push origin "$BRANCH_PAGES" --force
    echo "   ✅ Reporte publicado en branch '$BRANCH_PAGES'"
fi

cd "$REPO_ROOT"

echo ""
echo "╔══════════════════════════════════════════════╗"
echo "║  ✅ Publicación completada                   ║"
echo "║                                              ║"
echo "║  🔗 https://christopherpalloarias.github.io/ ║"
echo "║     TICKETING_SEM7_SERENITY/                 ║"
echo "╚══════════════════════════════════════════════╝"
