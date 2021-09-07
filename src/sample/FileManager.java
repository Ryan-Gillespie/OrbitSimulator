package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class FileManager {
    /**
     * Loads a solar system from disk. The file should have the format of 
     * star [name] [mass]
     * [name] [a] [e] [mass] [parent name]
     * each line should be separated by ",\t"
     * 
     * @param fileName is the name of the planet file to open
     * @param solarSystem is the solarSystem array we're modifying as a return value
     */
    public static void LoadSystem(String fileName, List<OrbitingBody> solarSystem) {
        solarSystem.clear();
        HashMap<String, OrbitingBody> parentMap = new HashMap<>(100);
        List<String[]> contents = new ArrayList<>();
        
        // collect the file into a list of String arrays
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            contents = stream
                .filter(line -> !line.startsWith("//"))
                .map(line -> line.split(",\t"))
                .collect(Collectors.toList());
        } catch(IOException e) {
            System.out.println("Error Reading File");
        }

        // parse the collected lines
        contents.forEach(line -> {
            // Star case - only parse first two objects
            if (line[0].startsWith("star")) {
                OrbitingBody star = new OrbitingBody(line[1], Double.parseDouble(line[2]));
                solarSystem.add(star);
                parentMap.put(star.getName(), star);
            } 
            // Planet case - parse every variable
            else {
                try {
                    OrbitingBody planet = new OrbitingBody(line[0], Double.parseDouble(line[1]),
                        Double.parseDouble(line[2]), Double.parseDouble(line[3]), 
                        parentMap.get(line[4]));
                    solarSystem.add(planet);
                    parentMap.put(planet.getName(), planet);
                } catch(NullPointerException e) {
                    System.out.println("Error Parsing File, parent does not exist");
                } catch(NumberFormatException e) {
                    System.out.println("Error Parsing File, invalid number");
                }
            }
        });
    }
}
