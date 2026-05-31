# PGRF2 Java3D Project

Tento projekt je 3D grafická aplikace v jazyce Java, vyvinutá v rámci předmětu PGRF2 na FIM UHK. Zaměřuje se na implementaci pokročilejších algoritmů pro renderování (rendering pipeline), stínování a texturování trojrozměrných objektů bez použití externích knihoven jako OpenGL.

Aplikace slouží jako interaktivní nástroj pro demonstraci principů zobrazování těles v plném (Solid) i drátovém (Wireframe) režimu, řešení viditelnosti pomocí Z-bufferu, mapování textur a implementace Phongova osvětlovacího modelu s pohybujícím se zdrojem světla.

## Klíčové funkce
* **3D Rendering Pipeline**: Kompletní matematický řetězec transformací vrcholů (Modelovací -> Pohledová -> Projekční -> Dehomogenizace a transformace na obrazovku).
* **Vykreslování (Solid & Wireframe)**: Podpora pro rendering drátových modelů hran i plnohodnotné vyplňování polygonů (trojúhelníků).
* **Viditelnost (Z-buffer)**: Softwarová implementace hloubkového bufferu (Z-Buffer / DepthBuffer) pro korektní řešení překrývání vykreslovaných ploch v prostoru.
* **Materiály a Shadery (Shading)**:
  * `ShaderTexture`: Mapování 2D textur (cihly, pískovec, voda) na povrchy těles.
  * `ShaderPhong`: Výpočet osvětlení v reálném čase (ambientní a difuzní složka) reagující na pozici zdroje světla.
  * `ShaderInterpolated` & `ShaderConstant`: Interpolace barev vrcholů a konstantní barva pro specifické prvky.
* **Geometrická tělesa**: Podpora pro základní procedurální 3D objekty – Kostka (Cube), Koule (Sphere), Kužel (Cone), souřadné osy (X, Y, Z) a bodový světelný zdroj (reprezentovaný menší koulí).
* **Interaktivní transformace**: Možnost posunu (translace), otáčení (rotace) a změny měřítka (scaling) aktivního tělesa či světla v reálném čase.
* **Virtuální kamera**: Prvofosobní kamera (First Person Mode) s plynulým pohybem v prostoru a rozhlížením pomocí myši.
* **Projekce**: Možnost dynamického přepínání mezi perspektivním a ortogonálním zobrazením scény.

## Použité technologie
* **Jazyk**: Java (vyžaduje JDK 25)
* **Grafické rozhraní**: Java Swing / vlastní rastrový buffer (`RasterBufferedImage`)
* **Matematická knihovna**: Knihovna `transforms` pro práci s lineární algebrou (vektory, matice, kvaterniony) – **poskytnuta v rámci předmětu, vytvořena na FIM UHK**.

## Ovládání
### Manipulace s objekty
* `MEZERNÍK`: Přepínání aktivního tělesa (Kostka -> Koule -> Kužel -> Světelný zdroj).
* `Šipky` (Vlevo/Vpravo/Nahoru/Dolů): Posun tělesa podél os X a Y.
* `Page Up` / `Page Down`: Posun tělesa podél osy Z (dopředu/dozadu).
* `X`, `Y`, `Z`: Rotace aktivního tělesa kolem příslušných lokálních os.
* `[` , `]`: Změna měřítka tělesa (zmenšení / zvětšení).

### Kamera a Projekce
* `W`, `A`, `S`, `D`: Pohyb kamery v prostoru (dopředu, doleva, dozadu, doprava).
* `Tažení myší`: Rozhlížení se do stran (změna azimutu a zenitu virtuální kamery).
* `P`: Přepínání mezi perspektivní a ortogonální projekcí.

### Režimy zobrazení a Shadery
* `M`: Přepínání drátového režimu (Wireframe) pro celou scénu.
* `T`: Polyfunkční klávesa pro změnu vzhledu:
  * Pokud je aktivní **běžné těleso**, přepíná (cykluje) jeho shader: `Textura` -> `Phongovo stínování` -> `Interpolované barvy`.
  * Pokud je aktivní **světelný zdroj**, mění barvu emitovaného světla: `Červená` -> `Zelená` -> `Modrá`.
