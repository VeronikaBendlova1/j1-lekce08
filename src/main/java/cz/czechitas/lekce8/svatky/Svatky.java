package cz.czechitas.lekce8.svatky;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Třída s informacemi o tom,kdo má kdy svátek.
 */
public class Svatky {
  private static final DateTimeFormatter MONTH_PARSER = DateTimeFormatter.ofPattern("d.M.");

  public Stream<Svatek> seznamSvatku() {
    try {
      Path path = Paths.get(Svatky.class.getResource("svatky.txt").toURI());
      return Files.lines(path).map(Svatky::parseLine);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Vrátí seznam všech svátků v daném měsíci.
   *
   * @param mesic Měsíc, pro který se mají svátky vypsat.
   * @return Stream svátků.
   */
  public Stream<Svatek> svatkyVMesici(Month mesic) {
    return seznamSvatku()
            .filter(svatek -> svatek.getDen().getMonth().equals( mesic));
  }

  /**
   * Vrátí den, kdy má dotyčné jméno svátek.
   *
   * @param jmeno
   * @return
   */
  public Stream<MonthDay> datumSvatku(String jmeno) {
    return seznamSvatku()
            .filter(svatek -> svatek.getJmeno().equals(jmeno))
            .map(Svatek::getDen);
  }

  /**
   * Vrátí všechna jména mužů.
   *
   * @return Stream jmen.
   */
  public Stream<String> muzi() {
    //TODO implementovat pomosí lambda výrazu
    return seznamSvatku()
            .filter(svatecek -> svatecek.getGender().equals(Gender.MUZ))
            .map(Svatek::getJmeno);


  }

  /**
   * Vrátí všechna jména žen.
   *
   * @return Stream jmen.
   */
  public Stream<String> zeny() {
    //TODO implementovat pomocí method reference
    return seznamSvatku()
            .filter(svatecek -> svatecek.getGender().equals(Gender.ZENA))
            .map(dortik -> dortik.getJmeno());
    // Musí být map - metoda má vracet stream stringů, takže to musíme proměnit na stringy
  }

  /**
   * Vrátí jména, která mají v daný den svátek.
   *
   * @return Stream jmen.
   */
  public Stream<String> den(MonthDay den) {
    //TODO

    return seznamSvatku()
            .filter(xy -> xy.getDen().equals( den))
            .map(dortik -> dortik.getJmeno());
    //.collect(Collectors.toList()); vrátí to do listu
  }

  /**
   * Vrátí ženská jména, která maj ísvátek v daném měsíci.
   *
   * @param mesic Vybraný měsíc.
   * @return Stream jmen.
   */
  public Stream<String> zenskaJmenaVMesici(Month mesic) {
    //TODO
    return seznamSvatku()
            .filter(svatek -> svatek.getDen().getMonth().equals(mesic) && svatek.getGender().equals(Gender.ZENA))
            .map(svatek1 -> svatek1.getJmeno());
  }


  /**
   * Vrátí počet mužů, kteří mají svátek 1. den v měsíci.
   *
   * @return Počet mužských jmen.
   */
  public int pocetMuzuSvatekPrvniho() {
    //TODO
    return (int) seznamSvatku()
            .filter(svatek -> svatek.getDen().getDayOfMonth() == 1 && svatek.getGender().equals(Gender.MUZ))
            .map(Svatek::getJmeno)
            .count();
  }

  /**
   * Vypíše do konzole seznam jmen, která mají svátek v listopadu.
   *
   * @return
   */
  public void vypsatJmenaListopad() {
    //TODO
   seznamSvatku()
            .filter(x -> x.getDen().getMonth().equals(Month.NOVEMBER))
            .forEach(jmeno -> System.out.println(jmeno));
  }


    /**
     * Vypíše počet unikátních jmen v kalendáři.
     *
     */
  public int pocetUnikatnichJmen(){
    //TODO
    return (int)
            seznamSvatku()
            .map(svatek-> svatek.getJmeno())
            .distinct()
            .count();
  }



  /**
   * Vrátí seznam jmen, která mají svátek v červnu – přeskočí prvních 10 jmen.
   *
   * @see Stream#skip(long)
   */
  public Stream<String> cervenJmenaOdDesatehoJmena() {
    //TODO
    return seznamSvatku()
            .filter(svatek -> svatek.getDen().getMonth().equals(Month.JUNE))
            .skip(10)
            .map(svatek -> svatek.getJmeno());
  }

  /**
   * Vrátí seznam jmen, která mají svátek od 24. 12. včetně do konce roku.
   *
   * @see Stream#dropWhile(java.util.function.Predicate)
   */
  public Stream<String> jmenaOdVanoc() {
    //TODO
    return seznamSvatku()
            .filter(svatek -> svatek.getDen().getMonth().equals(Month.DECEMBER))
            .dropWhile(svatek -> svatek.getDen().getDayOfMonth() < 24)
            .map(Svatek::getJmeno);
  }

  private static Svatek parseLine(String line)

  {
    String[] parts = line.split("\\s");
    assert parts.length == 3;
    return new Svatek(
            MonthDay.parse(parts[0], MONTH_PARSER),
            parts[1],
            Gender.valueOf(parts[2].toUpperCase(Locale.ROOT))
    );
  }


}