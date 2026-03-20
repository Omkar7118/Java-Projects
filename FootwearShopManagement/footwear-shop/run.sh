#!/usr/bin/env bash
# ───────────────────────────────────────────────────────────
#  Footwear Shop Management System – Build & Run
#  Requires: JDK 21+  (javac + java)
#  Internet: needed only on first run (downloads sqlite-jdbc)
# ───────────────────────────────────────────────────────────
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# ── Colours ──
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; NC='\033[0m'

echo -e "${GREEN}╔══════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║   👟  Footwear Shop – Build & Launch     ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════╝${NC}"

# ── 1. Check Java ──
if ! command -v javac &> /dev/null; then
    echo -e "${RED}[ERROR] javac not found. Install JDK 21+ and add it to PATH.${NC}"
    exit 1
fi
JAVA_VER=$(javac -version 2>&1 | grep -oP '\d+' | head -1)
if [ "$JAVA_VER" -lt 21 ]; then
    echo -e "${RED}[ERROR] JDK 21+ required (found $JAVA_VER).${NC}"
    exit 1
fi
echo -e "${GREEN}[✓] Java $JAVA_VER detected${NC}"

# ── 2. Download SQLite JDBC (if missing) ──
LIB="sqlite-jdbc-3.45.1.0.jar"
if [ ! -f "$LIB" ]; then
    echo -e "${YELLOW}[↓] Downloading SQLite JDBC driver…${NC}"
    curl -sL -o "$LIB" \
        "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar"
    if [ ! -s "$LIB" ]; then
        echo -e "${RED}[ERROR] Download failed. Place sqlite-jdbc-3.45.1.0.jar in this folder manually.${NC}"
        exit 1
    fi
    echo -e "${GREEN}[✓] SQLite JDBC downloaded${NC}"
else
    echo -e "${GREEN}[✓] SQLite JDBC already present${NC}"
fi

# ── 3. Compile ──
mkdir -p out
echo -e "${YELLOW}[⟳] Compiling…${NC}"

find src/main/java -name "*.java" > /tmp/sources.txt

javac \
    -cp "$LIB" \
    -d out \
    @/tmp/sources.txt

if [ $? -ne 0 ]; then
    echo -e "${RED}[ERROR] Compilation failed. See errors above.${NC}"
    exit 1
fi
echo -e "${GREEN}[✓] Compilation successful${NC}"

# ── 4. Run ──
echo -e "${GREEN}[▶] Launching Footwear Shop…${NC}"
java -cp "out:$LIB" shop.App

echo -e "${GREEN}[✓] Application closed.${NC}"
