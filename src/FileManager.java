import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.collections.ObservableList;


public class FileManager {
    static String seperator = ",";
  
    public static void save(String fileName, ArrayList<Body> planets) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File(fileName));
        StringBuilder sb = new StringBuilder();
        
        for (Body b : planets) {
          if (b.parent != null) {
            sb.append(b.name + seperator);
            sb.append((b.a / Orbit.Au2m) + seperator);
            sb.append(b.e + seperator);
            sb.append((b.mass) + seperator);
            sb.append(Math.toDegrees(b.i) + seperator);
            sb.append(Math.toDegrees(b.phi) + seperator);
            sb.append(b.parent.name);
            sb.append("\n");
          } else {
            sb.append(b.name + seperator);
            sb.append((b.mass / Orbit.E2kg) + seperator);
            sb.append("\n");
          }
        }
        
        pw.write(sb.toString());
        pw.close();
    }
    
    public static void save(String fileName, ObservableList<Body> planets) throws FileNotFoundException{
      PrintWriter pw = new PrintWriter(new File(fileName));
      StringBuilder sb = new StringBuilder();
      
      for (Body b : planets) {
        if (b.parent != null) {
          sb.append(b.name + seperator);
          sb.append((b.a / Orbit.Au2m) + seperator);
          sb.append(b.e + seperator);
          sb.append((b.mass / Orbit.E2kg) + seperator);
          sb.append(Math.toDegrees(b.i) + seperator);
          sb.append(Math.toDegrees(b.phi) + seperator);
          sb.append(b.parent.name);
          sb.append("\n");
        } else {
          sb.append(b.name + seperator);
          sb.append((b.mass / Orbit.E2kg) + seperator);
          sb.append("\n");
        }
      }
      
      pw.write(sb.toString());
      pw.close();
  }
    
    public static ObservableList<Body> readObs(String csvFile, ObservableList<Body> planets) {
      planets.clear();
      BufferedReader br = null;
      String line = "";

      try {

          br = new BufferedReader(new FileReader(csvFile));
          while ((line = br.readLine()) != null) {

              // use comma as separator
              String[] planet = line.split(seperator);
              String name = planet[0];
              try {
                double a = Double.parseDouble(planet[1]);
                double e = Double.parseDouble(planet[2]);
                double mass = Double.parseDouble(planet[3]);
                double i = Math.toRadians(Double.parseDouble(planet[4]));
                double phi = Math.toRadians(Double.parseDouble(planet[5]));
                String parent = planet[6];
                
                planets.add(new Orbit(name, a, e, mass, i, phi, parent, planets));
              } catch(ArrayIndexOutOfBoundsException e) {
                double mass = Double.parseDouble(planet[1]);
                
                planets.add(new Body(name, mass));
              }
          }
          
          return planets;

      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          if (br != null) {
              try {
                  br.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return null;
    }
}