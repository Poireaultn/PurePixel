# Débruitage d’image par ACP et seuillage 📷

Ce projet Java est une application de **traitement d’image** permettant le **débruitage d’images** via :
- une **extraction de patchs** (locale ou globale),
- une **analyse en composantes principales (ACP)**,
- un **seuillage** (doux ou dur) dans le domaine projeté.

Les résultats sont enregistrés après traitement, et le projet s’appuie sur la librairie `commons-math3-3.6.1` pour les calculs mathématiques avancés.

---

## 🔧 Installation et Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/Poireaultn/PurePixel.git 
```

### 2. Importer le projet dans Eclipse

```menu
Fichier > Importer > General > Existing Projects into Workspace
```

- Sélectionner le dossier contenant le projet.
- Cocher **"Copy projects into workspace"** si vous souhaitez faire une copie locale.

---

## 📚 Ajouter la librairie `commons-math3-3.6.1.jar`

1. Clic droit sur le projet > **Properties**  
2. Java Build Path > **Libraries**  
3. Cliquer sur **"Add External JARs..."**  
4. Sélectionner :  
   ```
   lib/commons-math3-3.6.1.jar
   ```
5. Valider avec **Apply and Close**

---

## 🖼️ Structure des fichiers image

```
src/
├── Image/
│   ├── Images/           # Dossier contenant les images à traiter (.png, .jpg, ...)
│   └── Resultats/        # Dossier de sortie pour les images débruitées
```

---

## 🚀 Exécution du projet

### ▶️ Depuis Eclipse

1. Aller dans le fichier :
   ```
   src/PurePixel.java
   ```
2. Clic droit > **Run As > Java Application**

---

### 🧾 En ligne de commande

#### 1. Compiler

```bash
javac -cp "lib/commons-math3-3.6.1.jar" -d bin src/PurePixel/Main.java
```

#### 2. Exécuter

```bash
java -cp "bin:lib/commons-math3-3.6.1.jar" PurePixel.Main
```

## 🧠 Fonctionnalités

- 📦 Extraction de patchs **locale** ou **globale**
- 📉 Réduction dimensionnelle via **ACP**
- 🧽 **Débruitage** via **seuillage doux** ou **dur**
- 🖼️ Chargement des images depuis `src/Image/Images`
- 💾 Sauvegarde des résultats dans `src/Image/Resultats`

---

## 📁 Arborescence du projet

```
PurePixel/
├── src/
│   ├── Image/
│   │   ├── Images/            # Images d’entrée
│   │   └── Resultats/         # Images traitées
│   └── PurePixel.java              # Fichier principal
├── lib/
│   └── commons-math3-3.6.1.jar  # Librairie mathématique
└── README.md
```

---

## 👥 Auteurs

- **ALMIRANTEARENA Alaïa** — _almirante10_
- **BOUHAFID Ilyas** — _ilyas157_
- **CHERGUI Hichem** — _Hichem-17_
- **MENDIBURU Charles** — _Charles40130_
- **POIREAULT Nathan** — _Poireaultn_

Projet académique — **ING 1 GM — CY Tech 2025**
