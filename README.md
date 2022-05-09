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

 
