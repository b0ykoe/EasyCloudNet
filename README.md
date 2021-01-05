# EasyCloudNet

## Was ist EasyCloudNet?
EasyCloudNet ist ein Plugin welches die Entwicklung und Umgang mit CloudNet-Plugins (momentan **only** v3) erleichtern soll. Hierzu werden verschiedenste Komponenten angeboten.

## Requirements
- CloudNet v3
- Java 11 (oder höher)
- PaperSpigot 1.13.2 (oder höher)

## Info
EasyCloudNet kann unter umständen auch mit Java 8 und Spigot / Paperspigot 1.8 laufen. Jedoch
 sind die trims darauf ausgelegt mit der 1.13.2+ zu funktionieren. EasyCloudNet wird mit Java 11
  compiled, auch das kann sehr einfach geändert werden.

## Installation

- Das Modul in `modules/` kopieren.
- `rl confirm` in der Konsole eingeben, alternative ingame `/cloud rl confirm`
- Die Konfiguration, `modules/EasyCloudNet/config.json`, anpassen und anschließend mit `rl
 confirm` neu laden.
- **INFORMATION:** Soll ein Task das Plugin nicht erhalten so kann er in `blacklist` eingetragen
  werden.
  ```json
    "blacklist": ["bsp1", "bsp2"]
  ```
- **INFORMATION:** Soll ein Task eine besondere Konfiguration erhalten, so kann er bei `overrides
` eingetragen werden.
  ```json
    "overrides": [
      {
        "prefix": "ECN",
        "task": "Lobby",
        "format": "%display%%name% &8:&c %message%"
      },
      {
        "prefix": "ECN",
        "task": "Beispiel2",
        "format": "%name% &8:&f %message%"
      },
    ]
  ```
- Service mit `service <name> stop` neustarten, damit das Plugin beim Start des Services eingef
ügt wird.
- **INFORMATION:** Bei dem Befehl `tasks setup` gibt es zwei fragen.
    - `Do you want to add a new Override for the service?`
    - Wird dies bejaht (yes) so wird ein neuer Override Eintrag für diesen Service angelegt.
    - `Do you want to add a new blacklist this service? EasyCloudNet wont be installed on that
     service.`
    - Wird dies bejaht (yes) so wird das Plugin nicht auf den Service kopiert und wird auch nicht
     durch eine manuelle installation funktionieren.
- **INFORMATION:** ProtocolLib wird automatisch eingefügt.

## Maven
```
    <!-- Snapshots -->
	<repositories>
		<repository>
		    <id>Rapture-Snapshots</id>
            <url>https://repo.rapture.pw/repository/maven-snapshots/</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>pw.rapture</groupId>
	    <artifactId>EasyCloudNet</artifactId>
	    <version>1.0.0-SNAPSHOT</version>
	</dependency>

    <!-- Releases -->
	<repositories>
		<repository>
		    <id>Rapture-Releases</id>
            <url>https://repo.rapture.pw/repository/maven-releases/</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>pw.rapture</groupId>
	    <artifactId>EasyCloudNet</artifactId>
	    <version>1.0.0-RELEASE</version>
	</dependency>
```

## Beispiel
```java
EasyCloudNet easyCloudNet = EasyCloudNet.getInstance();

// Beim joinen und leaven wird AUTOMATISCH sein easyPlayer Objekt angelegt welches mit EasyCloudNet.getInstance().getEasyPlayer() aufgerufen werden kann

EasyPlayer easyPlayer = easyCloudNet.addEasyPlayer(UUID);
EasyPlayer easyPlayer = easyCloudNet.addEasyPlayer(Player);

easyCloudNet.removeEasyPlayer(UUID);
easyCloudNet.removeEasyPlayer(Player);

EasyPlayer easyPlayer = easyCloudNet.getEasyPlayer(UUID);
EasyPlayer easyPlayer = easyCloudNet.getEasyPlayer(Player);

EasyNameTag easyNameTag = easyPlayer.getEasyNameTag();
easyNameTag.setAutoUpdate(boolean); // default true - Standartwert wenn kein optionaler boolean gesetzt wird
// folgende optionale parameter geben an ob anschließend ein easyNameTag.updateNameTags() ausgeführt werden soll
easyNameTag.setColor(String, optional: boolean);
easyNameTag.setDisplay(String, optional: boolean);
easyNameTag.setGroupName(String, optional: boolean);
easyNameTag.setPrefix(String, optional: boolean);
easyNameTag.setShowDefaultGroup(boolean, optional: boolean)
easyNameTag.setSortId(int, optional: boolean);
easyNameTag.setSuffix(string, optional: boolean);
easyNameTag.updateNameTags();

EasyScoreboard easyScoreboard = easyPlayer.getEasyScoreboard();
easyScoreboard.init(String); // erstellt ein neues Scoreboard mit dem title
easyScoreboard.setLine(int, String); // setzt diesen String in das Scoreboard, Maximallänge von 32, alles drüber wird abgeschnitten
easyScoreboard.updateLine(int, String); // updatet den Wert im Scoreboard ohne zu flackern
easyScoreboard.setTitle(String); // updatet den Scoreboardtitle ohne zu flackern
```

## Special Thanks
- [CloudNet & Community](https://github.com/CloudNetService/CloudNet-v3)
- [dajooo](https://github.com/dajooo)