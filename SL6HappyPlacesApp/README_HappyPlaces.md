
# 🌍 Happy Places App

Die **Happy Places App** ist eine Android-Anwendung zur Verwaltung persönlicher Lieblingsorte. Nutzer:innen können Orte mit Name, Beschreibung, Standort, Foto und optionalen Notizen speichern, anzeigen, bearbeiten und löschen.

## 📱 Features

- ✅ Orte mit Name, Beschreibung, Kategorie und Notiz erfassen
- 🗺 Standortwahl über OpenStreetMap (OSMDroid)
- 📷 Foto aus Galerie hinzufügen
- 🔍 Suchfunktion (Name oder Kategorie)
- 🧭 Alle Orte auf interaktiver Karte anzeigen
- ✏️ Orte später bearbeiten oder löschen
- 💾 Lokale Speicherung mit Room-Datenbank (MVVM)

## 🛠 Technischer Aufbau

| Technologie       | Zweck                                         |
|------------------|----------------------------------------------|
| Jetpack Compose  | UI mit moderner Declarative UI                |
| Room             | Lokale Datenbank für gespeicherte Orte        |
| OSMDroid         | OpenStreetMap-Integration                     |
| Kotlin           | Programmiersprache                            |
| MVVM             | Struktur für UI-Logik-Trennung                |
| Coil             | Bilder aus URIs anzeigen                      |
| Navigation       | Bildschirmwechsel mit Navigation-Component    |

## 🧪 Getestet auf

- Android Studio **Meerkat | 2024.3.1 Patch 1**
- Android 13 (One UI 5.1)
- Min SDK 24, Target SDK 34

## 🚀 Beispiel-Workflow

1. Klick auf „+“ → neuen Ort hinzufügen
2. Name & Beschreibung eingeben
3. Standort auf Karte setzen
4. Optional: Foto aus Galerie auswählen
5. Ort speichern
6. Später bearbeiten oder löschen (auch über die Startseite möglich)
