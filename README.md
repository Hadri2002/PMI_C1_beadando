Klinikai nyilvántartó program
========

**A program célja**

A program segítségével a klinikán dolgozók felvehetnek időpontokat páciensek számára. Mindezt egy konzolos menü segítségével tehetik meg.


Osztályok
----------

**Main**

Ez tartalmazza a menü felépítését, futtatásával használható a program. Meghívja a különböző metódusokat, az alábbi módon:
* Új időpont felvétele
* Időpont módosítása
* Időpont törlése
* Időpont listázása
* Kilépés a programból

**Patient**

A páciens objektumok létrehozásához használható osztály, tartalmazza a páciens adatait, illetve getter, setter és constructor függvényeket.

Tagváltozói:
* String name - Páciens neve
* String taj - Páciens TAJ száma

**Appointment**

Az időpont osztály tartalmazza az időpontok tagváltozóit, constructor-ait, getter-setter függvényeit, illetve egy toString metódust, ami segítségével az objektumok kiiratásakor a név, taj szám, is az időintervallum jelenik meg

Tagváltozói:
* Patient patient - A páciens objektum
* LocalDate date - Az időpont dátuma
* LocalTime time - Az időpont kezdetének ideje
* Duration duration - Az időpont várható időtartama
* LocalTime endTime - Az időpont várható vége, amit a constructor automatikusan kiszámol a kezdeti idő és időtartam alapján

**Methods**

A metódusokat tartalmazó osztály, aminek függvényeit meghívja a main.


XML fájl
-------

Az XML fájl a következő felépítéssel, tag-ekkel rendelkezik:
* appointments - a fájl gyökere, tartalmazza a különböző időpontokat
* appointment - egy időpont adatait tartalmazza
* patient - az adott páciens adatait tartalmazza
  * name - a páciens nevét tartalmazó tag
  * taj - a páciens TAJ számát tartalmazó tag
* date - az időpont dátuma
* time - az időpont kezdetének ideje
* duration - a várható időtartam


Metódusok
--------

Ebben a szekcióban a Methods.java osztály metódusait fogom részletesen taglalni. 

**writeToXml**

Publikus, statikus, végrehajtó függvény

Feladatai:
* A lista tartalmát kiírja az xml fájlba az előbbiekben taglalt módon
* Felhasználja a createChildElement metódust ehhez

Bemeneti paraméterei:
* ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista
* String filepath - Az xml fájl elérési útja
  
**createChildElement**
  
Privát, statikus, végrehajtó függvény
  
Feladatai:
* Segédmetódus, a megfelelő adatokat megadva számára hozzáfűz a dokumentum-hoz egy új tag-et, illetve annak tartalmát

Bemeneti paraméterei:
* Document document - Az xml fájlt tartalmazó dokumentum objektum
* Element parent - A "felnőtt" elem, az az elem ami alá kell rangsorolni az épp létrehozni kívánt tag-et
* String tagName - A létrehozni kívánt tag neve
* String value - A létrehozni kívánt tag tartalma
  
**readFromXml**
 
Publikus, statikus, időpontokat tartalmazó ArrayList-tel visszatérő függvény
 
Feladatai:
* Az xml fájlból kiolvasva az adatokat felépít egy ArrayList-et, ami az időpontokat tartalmazza
 
Bemeneti paraméterei:
* String filepath - Az xml fájl elérési útja

**listAppointments**
 
 Publikus, statikus, végrehajtó függvény
 
 Feladatai:
 * Az Appointment.toString() metódusa alapján kilistázza az eddig felvett időpontokat
 
 Bemeneti paraméterei:
 * ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista

 **addNewAppointment**
 
 Publikus, statikus, végrehajtó függvény 
 
 Feladatai:
 * Létrehoz egy új időpontot annak minden adatával együtt
 * Ennek érdekében meghívja az egyes segédfüggvényeket (amiket külön részletez a leírás lentebb)
 * Leellenőrzi, hogy a létrehozott időpont ütközik-e másik időpontokkal, figyelmeztet erről
 * Leellenőrzi, hogy a létrehozott TAJ szám alatt van-e már beteg
 * Lekezeli a hibákat
 * Hozzáadja az új időpont objektumot a listához, majd elmenti az xml fájlba a writeToXml metódus segítségével
 
 Boolean-ok jelentései:
 * patientCheck - hamis, amíg létre nem hozza a felhasználó helyesen a páciens objektum adatait
 * nameCheck - igaz, ha már létezik az xml fájlban a beírt néven felhasználó és ez alapján ki is iratja őket (ezt szimplán figyelmeztetésként használja a program, mivel azonos néven létezhet több különböző TAJ-számú ember
 * dateCheck -  hamis, amíg létre nem hozza a felhasználó helyesen az időponttal kapcsolatos adatokat
 
 Bemenő paraméterei:
 * ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista
 * String filepath - Az xml fájl elérési útja

 **enterName**
 
 Privát, statikus, nevet tartalmazó Stringgel visszatérő függvény
 
 Feladati:
 * Segédmetódus, segítségével neveket lehet beolvasni 
 * Addig folytatja a beolvasást, amíg a felhasználó üres nevet ad meg
 
 **enterDate**
 
 Privát, statikus, dátumot tartalmazó LocalDate változóval visszatérő függvény
 
 Feladatai:
 * Egy elérhetetlen kezdeti értékre beállítva a dátumot, az időpont beolvasását addig folytatja, amíg a felhasználó valós értéket nem ad meg
 * Lekezeli, ha nem megfelelő formátumban történik a megadás
 * Csak a következő naptól kezdve írható be dátum
 * Leellenőrzi, hogy létezik-e már időpont az adott dátumon, erről tájékoztatja a felhasználót (dateTaken boolean igaz, ha van azon a dátumon időpont már az xml fájlban)
 
 Bemenő paraméterei:
  * ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista

 **enterTime**
 
 Privát, statikus, időpontot tartalmazó LocalTime változóval visszatérő függvény
 
 Feladatai:
 * Egy elérhetetlen kezdeti értékre beállítva az időt, az időpont beolvasását addig folytatja, amíg a felhasználó valós értéket nem ad meg
 * Lekezeli, ha nem megfelelő formátumban történik a megadás
 * Figyeli, hogy csak 8-16-ig lehessen az időpont megadva
 
 **enterDuration**
 
Privát, statikus, percekben megadott időintervallumot tartalmazó Duration változóval visszatérő függvény
 
 Feladatai:
 * percekben megadott értéket alakít át Duration változóvá, lekezeli a hibákat

 **enterTaj**
 
 Privát, statikus, Stringben tárolt TAJ szám értékével tér vissza
 
 Feladatai: 
 * Ellenőrzi, hogy 9 hosszú-e és számokból áll csak-e a megadott TAJ szám
 
 **deleteAppointment**
 
 Publikus, statikus, végrehajtó függvény
 
 Feladatai:
 * TAJ szám alapján megkeresi az adott időpontot, törli azt, majd frissíti az XML fájlt
 * Ha nem létezik a TAJ szám, kilép
 
  Bemenő paraméterei:
 * ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista
 * String filepath - Az xml fájl elérési útja
 
 **modifyAppointment**
 
 Publikus, statikus, végrehajtó függvény
 
 Feladatai:
 * TAJ szám alapján megkeresi az adott időpontot, törli ideiglenesen azt, majd lementi az adatait egy új változóba (appointmentToModify, modifiedPatient)
 * menü segítségével kiválaszthatjuk melyik adatot változtatjuk meg, segédfüggvényekkel megváltoztatja azt
 * hozzáadja a listához a módosított időpontot, frissíti az XML fájlt
 
  Bemenő paraméterei:
 * ArrayList<Appointment> appointments - Az időpontokat tartalmazó dinamikus lista
 * String filepath - Az xml fájl elérési útja
 
 **modifyName*
 
 Privát, statikus, végrehajtó függvény
 
 Feladatai:
 *
