package main;

import static java.util.stream.Collectors.toMap;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

  private static final int TIMES_FOR_TIMING = 1000;
  private static final String[] RESOURCES = new String[] {
      "dependencies.txt",
      "dependencies-cyclical-complex.txt"
  };

  public static void main (String[] args) {
    System.out.println("With simple BFS:\n");
    for (String resource : RESOURCES) {
      System.out.println("For resource: " + resource);
      final Map<String, Set<String>> dependencies = new TreeMap<>();
      double time = MainUtils.timeIt( () -> {
        List<String> lines = Optional.ofNullable(MainUtils.readLinesFromResources(resource))
            .orElseThrow( () -> new RuntimeException("Could not read file"));
        dependencies.putAll(getDependenciesBFS(lines));
      }, TIMES_FOR_TIMING);

      prettyPrintMap(dependencies);
      System.out.println("Each time took an average of " + time + "ms.\n");
    }

    System.out.println("==============================\n");

    System.out.println("With efficient BFS:\n");
    for (String resource : RESOURCES) {
      System.out.println("For resource: " + resource);
      final Map<String, Set<String>> dependencies = new TreeMap<>();
      double time = MainUtils.timeIt( () -> {
        List<String> lines = Optional.ofNullable(MainUtils.readLinesFromResources(resource))
            .orElseThrow( () -> new RuntimeException("Could not read file"));
        dependencies.putAll(getDependenciesBFSEfficient(lines));
      }, TIMES_FOR_TIMING);

      prettyPrintMap(dependencies);
      System.out.println("Each time took an average of " + time + "ms.\n");
    }
  }

  private static Map<String, Set<String>> getDependenciesBFS (List<String> lines) {
    final Map<String, List<String>> loadedDependencies = loadDependencies(lines);

    return loadedDependencies.entrySet().stream().map(entry -> {
      final String element = entry.getKey();
      final Set<String> dependencies = new HashSet<>();

      // Depth First Search algorithm to collect all the dependencies.
      // This keeps track of all the subsequent dependencies to be processed.
      final Deque<String> frontier = new ArrayDeque<>(entry.getValue());
      // This keeps track of all the dependencies that have been processed and closed.
      final List<String> closedSet = new ArrayList<>();
      // While there are more unprocessed dependencies...
      while (!frontier.isEmpty()) {
        // Get the first one
        final String currentDependency = frontier.pop();
        // If it is included in the closed set then we don't do anything
        if (closedSet.contains(currentDependency)) {
          continue;
        }
        // Otherwise, add it to the dependencies set
        dependencies.add(currentDependency);
        // Get all the chaining dependencies and add them to the frontier
        final List<String> chainingDependencies = loadedDependencies.get(currentDependency);
        if (chainingDependencies != null) {
          frontier.addAll(chainingDependencies);
        }
        // Add it to the closed set to not process it again
        closedSet.add(currentDependency);
      }
      return new AbstractMap.SimpleEntry<>(element, dependencies);
    }).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, Set<String>> getDependenciesBFSEfficient (List<String> lines) {
    final Map<String, List<String>> loadedDependencies = loadDependencies(lines);
    final Map<String, Set<String>> finalDependencyMap = new TreeMap<>();

    loadedDependencies.forEach( (element, value) -> {
      // If an element has already been processed from earlier, there is no need to do it again.
      if (finalDependencyMap.get(element) != null) {
        return;
      }
      // Else instantiate its dependency set
      finalDependencyMap.put(element, new TreeSet<>());

      // Depth First Search algorithm to collect all the dependencies.
      // This keeps track of all the subsequent dependencies to be processed.
      final Deque<String> frontier = new ArrayDeque<>(value);
      // This keeps track of all the dependencies that have been processed and closed.
      final List<String> closedSet = new ArrayList<>();
      // While there are more unprocessed dependencies...
      while (!frontier.isEmpty()) {
        // Get the first one
        final String currentDependency = frontier.pop();
        // If it is included in the closed set then we don't do anything
        if (closedSet.contains(currentDependency)) {
          continue;
        }
        // Otherwise, add it to the final dependencies list for this element
        finalDependencyMap.get(element).add(currentDependency);
        // Get all the chaining dependencies, add the to them frontier and recalculate the dependencies of the current
        // one
        final List<String> chainingDepependencies = loadedDependencies.get(currentDependency);
        if (chainingDepependencies != null) {
          frontier.addAll(chainingDepependencies);
          finalDependencyMap.computeIfPresent(currentDependency, (elt, existingDependencies) -> {
            existingDependencies.addAll(chainingDepependencies);
            return existingDependencies;
          });
        }
        // Add it to the closed set to not process it again
        closedSet.add(currentDependency);
      }
    });

    return finalDependencyMap;
  }

  private static Map<String, List<String>> loadDependencies (List<String> lines) {
    return lines.stream()
        .map(line -> {
          String[] elements = line.split("\\s+");
          return new AbstractMap.SimpleEntry<>(
              elements[0],
              Arrays.asList(Arrays.copyOfRange(elements, 1, elements.length)));
        })
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static void prettyPrintMap (Map<String, Set<String>> map) {
    map.forEach( (key, value) -> System.out.println(key + " -> " + value));

  }
}
