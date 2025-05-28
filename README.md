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

## 🎨 Installation et configuration de JavaFX

### 1. Télécharger JavaFX

Téléchargez le SDK JavaFX correspondant à votre système d'exploitation depuis le site officiel :  
🔗 [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

Décompressez l'archive dans un dossier de votre choix.

### 2. Configurer JavaFX dans Eclipse

1. Clic droit sur le projet > Properties
2. Java Build Path > Libraries > Modulepath
3. Cliquer sur "Add External JARs..."
4. Ajouter les fichiers .jar du dossier lib/ de JavaFX
5. Appliquer les changements

### 3. Ajouter les VM arguments
Toujours dans Eclipse :

1. Aller dans Run > Run Configurations...
2. Sélectionner votre configuration d'exécution (ou en créer une nouvelle)
3. Onglet Arguments, partie VM arguments, ajouter :
   ```
   --module-path "C:/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml
   ```
⚠️ Remplacez C:/javafx-sdk-21/ par le chemin vers votre installation JavaFX.

---

## 🖼️ Structure des fichiers image

```
src/
│   └── Ressource/
│       └── Image/
│           ├── Images/          # Dossier contenant les images à traiter (.png, .jpg, ...)
│           └── Résultats/       # Dossier de sortie pour les images débruitées
```

---

## 🚀 Exécution du projet en mode console

### ▶️ Depuis Eclipse

1. Ouvrir le fichier src/Java/PurePixel.java
2. Clic droit > **Run As > Java Application**

### 🧾 En ligne de commande

#### 1. Compiler

```bash
javac -cp "lib/commons-math3-3.6.1.jar" -d bin src/PurePixel/Main.java
```

#### 2. Exécuter

```bash
java -cp "bin:lib/commons-math3-3.6.1.jar" PurePixel.Main
```

---

## 🚀 Lancement de l'IHM

### ▶️ Depuis Eclipse

1. Ouvrir le fichier src/Java/App.java
2. Clic droit > **Run As > Java Application**

Assurez-vous que les VM arguments sont bien configurés comme indiqué ci-dessus.

### 🧾 En ligne de commande

#### 1. Compiler

```bash
javac --module-path /chemin/vers/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp "lib/commons-math3-3.6.1.jar" -d bin src/Java/*.java
```

#### 2. Exécuter

```bash
java --module-path /chemin/vers/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp "bin:lib/commons-math3-3.6.1.jar" Java.PurePixel
```

---

## 🧠 Fonctionnalités

- 📦 Extraction de patchs **locale** ou **globale**
- 📉 Réduction dimensionnelle via **ACP**
- 🧽 **Débruitage** via **seuillage doux** ou **dur**
- 🖼️ Chargement des images depuis `src/Image/Images`
- 📂 Sauvegarde des résultats dans `src/Image/Resultats`

---

## 📁 Arborescence du projet

```
PurePixel/
├── src/
│   ├── Java/
│   │   ├── App.java             # Fichier mode IHM
│   │   └── PurePixel.java       # Fichier mode Console
│   └── Ressource/
│       └── Image/
│           ├── Images/          # Images d’entrée
│           └── Résultats/       # Images traitées
├── lib/
│   └── commons-math3-3.6.1.jar  # Librairie mathématique
└── README.md
```

---

## 📄 Génération de la Javadoc

Pour générer automatiquement la documentation Java (Javadoc) pour l'ensemble du projet, y compris tous les packages, suivez ces étapes :

### ▶️ Depuis la ligne de commande

1. Ouvrir un terminal à la racine du projet (là où se trouve le dossier `src/`).
2. Exécuter la commande suivante :

```bash
javadoc -d doc -sourcepath src -subpackages ACP Image Seuillage Vecteur
```

3. Pour consulter la documentation, ouvrez le fichier suivant dans votre navigateur :

```
doc/index.html
```

---

## 👥 Auteurs

- **ALMIRANTEARENA Alaïa** — _almirante10_
- **BOUHAFID Ilyas** — _ilyas157_
- **CHERGUI Hichem** — _Hichem-17_
- **MENDIBURU Charles** — _Charles40130_
- **POIREAULT Nathan** — _Poireaultn_

Projet académique — **ING 1 GM/GI — CY Tech 2025**
