# 3 - Document technique Projet

Pelle Servan - Bochu Robin

# Subject

Le but du projet est de développer un programme batch java qui devra implémenter les fonctionnalités suivantes:

- scruter un dossier particulier, à la recherche des fichiers users_<YYYYMMDDHHmmSS>.csv
- parser ces fichiers qui sont au format :
<Numero_Securite_Sociale>, <Nom>, <Prenom>, <Date_Naissance>, <Numero_Telephone>, <E_Mail>, <ID_Remboursement>, <Code_Soin>, <Montant_Remboursement>
- peuple une base de données relationnelle avec ces entrées :
    - l'ID remboursement est un identifiant qui permet de déterminer s'il s'agit d'un insert ou d'un update
    - une colonne <Timestamp_fichier> est extraite du nom du fichier
- une fois traités les fichiers sont déplacés dans un autre dossier
- les lignes en erreur (format incorrect) sont déplacés dans un dossier de rejet, dans un fichier avec le timestamp du fichier d'origine
- optionnel : les fichiers users_<YYYYMMDDHHmmSS>.json peuvent également être supportés.

# Modalités

Le projet doit être fait par groupe de 2 ou de manière individuelle, selon votre préférence.

## Stack technique

Le projet doit être fait en Java, avec Maven pour le build.
Des tests unitaires peuvent (doivent) être implémentés avec des Junit.
Des dépendances peuvent (doivent) être inclues, ne serait-ce que le driver JDBC pour la base (PostgreSQL recommandée).

## Livrables

Les livrables attendus via Mootse ou WeTransfer sont :

- un zip avec le code source du programme : note sur 14
- un document technique rédigé expliquant les choix d'implémentation (un readme sera OK) : note sur 6

Date attendue : 05/06/2023 00:00

## Les Choix D'implémentation

Pour la création de ce projet assez volumineux, nous avons décider Maven qui permet de construire des projets de manière très simpliste. Maven permet d'intégrer de nombreuses dépendances très facilement en les ajoutants directement dans le fichier pom.xml. Celles-ci nous on permis de pouvoir utiliser directement des fonctions/classes pré-faites et donc de gagner pas mal de temps.

## Les différentes dépendances utilisés :

### Listing :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd%22%3E
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>tse_java_project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>19</maven.compiler.source>
        <maven.compiler.target>19</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- SLF4J -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.32</version>
        </dependency>
        <!-- Logback -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.6</version>
        </dependency>
        <!-- OpenCSV -->
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>5.5.2</version>
        </dependency>
        <!-- XML parsing -->
        <dependency>
            <groupId>org.w3c</groupId>
            <artifactId>dom</artifactId>
            <version>2.3.0-jaxb-1.0.6</version>
        </dependency>
        <!-- JUnit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

- SLF4J (Simple Logging Facade for Java) :
    - Une API de journalisation qui fournit une interface générique pour différentes bibliothèques de journalisation. Il permet de configurer facilement les journaux dans une application Java.
- Logback :
    - Une bibliothèque de journalisation qui implémente l'API SLF4J. Elle offre une configuration flexible et des fonctionnalités avancées de journalisation pour les applications Java.
- OpenCSV :
    - Une bibliothèque Java pour la manipulation de fichiers CSV. Elle permet de lire, écrire et manipuler des données dans des fichiers CSV.
- XML parsing :
    - Une bibliothèque pour l'analyse et la manipulation de documents XML.
- JUnit :
    - Un framework de test unitaire pour les applications Java. Il fournit des fonctionnalités pour écrire et exécuter des tests automatisés afin de vérifier le comportement attendu du code.

# Structure du projet :

- MAIN :
    - Code principal qui va appeler les fonctions des autres packages pour gérer le flux de la données des CSVs jusqu'à la base de données
- CSV :
    - Ensemble de fonctions qui vont permettre de lire tous les fichiers .csv dans le répertoire d'input.
    - Elles permettent de checker si le nom des fichiers correspondent au format attendu alors il est redirigé dans un autre répertoire
    - Il y a une vérification de la donnée de chaque ligne pour vérifier que chaque ligne respecte le format indiqué dans le fichier XML.
- DB
    - Après lecture des lignes, si une données est correcte alors elle est envoyé sur la db (Check id remboursement pour une personne -> Update ou Insert)
- FILE
    - Permet de déplacer un file
- GENERATEDATA
    - Script permettant de générer de la donnée aléatoirement

# Génération de données

Pour pouvoir utiliser ce script pour générer ce file, il faut auparavant télécharger le fichier requirements.txt et taper dans votre prompt

```bash
pip install -r requirements.txt
```

 (Installer les différentes libraires utilisées).

Pour avoir une fiabilité accrue, nous avons décidé de générer des caractères aléatoire lorsque cela possible en respectant les patterns de notre fichier .xml

Dans le cas de la date de naissance par exemple, nous sommes partie sur du faker car les expressions régulières qui valident un format correctes sont excessivement longues et illisibles.

```python
# Lecture propriétés DU XML
import xml.etree.ElementTree as ET

# Génération caractères aléatoires Faker permet de générer de la données plus réaliste 
# mais ici bridé par les différentes expressions régulières
import random
from faker import Faker

# Utilisation expression régulière
import re
#Génération de charactère en respectant une expression régulière en input
import rstr

# Gestion de CSV (Lecture/Ecriture)
import csv

# Param dans le prompt (Nbr lignes ici)
import sys
import os

# Get current date
from datetime import datetime

print(os.getcwd())

# Fonction pour générer la données, Paramètres : Path du XML pour le format, Nombre de lignes souhaitées
def generate_csv_data(xml_file, num_records=30):
    tree = ET.parse(xml_file)

    # On se call à la root du XML
    root = tree.getroot()
    fields = root.find('fields')

    # Les 2 structures de données qu'on va retourner
    # contient les données ainsi que les header correspondants
    csv_data = []
    csv_header = []

    # Lecture header
    for field in fields:
        csv_header.append(field.get('name'))

    # Itération à travers les différents field pour lire les propriétés
    for i in range(num_records):
        record = {}
        for field in fields.findall('field'):
            field_name = field.get('name')
            field_type = field.get('type')
            field_required = field.get('required')
            field_size = field.get('size')
            field_pattern = field.get('pattern')

            if field_type == 'long':
                field_value = random.randint(1000000000000, 9999999999999)
            elif field_type == 'string':
                field_size = int(field_size) if field_size is not None else 10
                field_value = Faker().text(max_nb_chars=field_size+1)[0:-1]
            elif field_type == 'int':
                field_size = int(field_size) if field_size is not None else 10
                field_value = random.randint(10**(field_size-1), (10**field_size)-1)
            elif field_type == 'float':
                field_value = round(random.uniform(0, 1000), 2)
            
            # Modif manuelle -> Si présence d'un pattern et que ce n'est pas une date de naissance alors on génère par rapport au regex seulement
            if field_pattern is not None and field_name != 'Date_Naissance':
                field_value = rstr.xeger(field_pattern)

            # Si Date de naissance je fait en sorte de ne pas avoir de date iréelle
            elif field_name == 'Date_Naissance':
                year = random.randint(1950, 2030)
                month = random.randint(1, 12)
                day = random.randint(1, 31)
                field_value = f"{year:04d}-{month:02d}-{day:02d}"

            ### IMPORTANT - Si on enleve des required alors une chance sur 2 de générer ou non une valeur 
            if field_required != 'true':
               cond_not_required = random.randint(0, 1)      
               if cond_not_required == 1:
                   field_value = None

            record[field_name] = field_value

        csv_data.append(record)
    return csv_data,csv_header

args = sys.argv[1:]
num_rows = 30  # Default number of rows

if len(args) >= 1:
    filename = args[0]
if len(args) >= 2:
    num_rows = int(args[1])   

csv_data,csv_header = generate_csv_data('./define_header.xml', num_rows)

filename = os.getcwd() + filename + str(datetime.now().strftime("%Y%m%d%H%M%S")) + '.csv'
print(filename)

# Écriture dans le csv
with open(filename, "w", newline="") as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=csv_header)
    writer.writeheader()
    writer.writerows(csv_data)
```

# Flux de nos données

### 1 - Check des fichiers dans le répertoire d'input :

- On va itérer dans le dossier input_paticulier qui va contenir tous fichiers CSV
- Ensuite nous allons regarder le nom de chaque fichier csv pour trouver ceux qui ne respecte pas le bon format users_<YYYYMMDDHHmmSS>.csv grâce à notre fichier define_header.xml
- Si le format n'est pas bon alors, alors le fichier est directement envoyé dans le répertoire output_paticulier

### 2 - Check des lignes par fichiers csv :

- Pour chaque ligne dans notre CSV, nous regardons si chaque valeur par ligne/colonne correspond bien au format indiqué dans le file define_header.xml
- Si une valeur dans la ligne est incorrecte, alors la ligne est envoyé dans un fichier le fichier : "dirty_filenameorigin.csv"
- Si ne valeur est correcte alors elle est envoyé dans un fichier généré : "clean_filenameorigin.csv"
    - Cela permet que si un problème avec la connexion à la base de données survient, alors les données ne seront pas perdu.

### 3 - Interactions avec la base de données :

- On itère dans chaque ligne du fichier clean
- On regarde si pour le même numéro de sécurité social et le même id on a déjà une ligne dans notre base de données correspondante.
    - Si c'est le cas, alors on va faire une requête qui va update la ligne
    - Sinon, on va ajouter cette nouvelle ligne directement dans la base de données

# Logs

Nous n’avons pas utilisé la dépendance lgo4j car, nous n’avons pas réussi à trouver comment stocker tous les logs d’un type avec le fichier correspondant et les autres logs dans un autre fichier distinct.

Les 2 types sont Info et debug, comme dit au dessus pour un soucis de compréhension, les logs sont stocker par type. 

# Tests Unitaires

Pour les tests, nous avons juger que seul la vérification de format étaient importante
