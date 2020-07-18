package maze;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final int WIDTH = 2;
    private static final int PATH_REPRESENTATION = '/';
    private final Scanner in = new Scanner(System.in);
    private Maze lastMaze;

    private static void print(Maze maze) {
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Cell cell = maze.get(new Position(i, j));
                for (int k = 0; k < WIDTH; k++) {
                    System.out.print(cell.getRepresentation());
                }
            }
            System.out.println();
        }
    }

    private static void printWithPath(Maze maze, Collection<Position> path) {
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Position p = new Position(i, j);
                char c = path.contains(p)
                        ? PATH_REPRESENTATION
                        : maze.get(p).getRepresentation();
                for (int k = 0; k < WIDTH; k++) {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    public void run() throws Exception {
        Map<Integer, Command> allCommands = List.of(
                new GenerateMazeCommand(),
                new LoadMazeCommand(),
                new SaveMazeCommand(),
                new DisplayMazeCommand(),
                new FindEscapeCommand(),
                new ExitCommand()
        ).stream().collect(Collectors.toMap(Command::getIndex, x -> x, (x, y) -> x, LinkedHashMap::new));
        while (true) {
            System.out.println("=== Menu ===");
            for (Command c : allCommands.values()) if (c.isVisible()) System.out.println(c);
            int index = in.nextInt();
            Command c = allCommands.get(index);
            if (c == null || !c.isVisible()) {
                System.out.println("Incorrect option. Please try again");
            } else {
                c.handle();
                if (c.getIndex() == 0) break;
            }
        }
    }

    private abstract static class Command {
        public abstract int getIndex();

        public abstract String getDescription();

        public abstract void handle() throws Exception;

        public boolean isVisible() {
            return true;
        }

        @Override
        public String toString() {
            return getIndex() + ". " + getDescription();
        }
    }

    private static class ExitCommand extends Command {
        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public String getDescription() {
            return "Exit";
        }

        @Override
        public void handle() {
            System.out.println("Bye!");
        }
    }

    private class GenerateMazeCommand extends Command {
        @Override
        public int getIndex() {
            return 1;
        }

        @Override
        public String getDescription() {
            return "Generate a new maze";
        }

        @Override
        public void handle() {
            System.out.println("Enter the size of a new maze");
            int size = in.nextInt();
            lastMaze = new MazeGenerator().generate(size, size);
            print(lastMaze);
        }
    }

    private class LoadMazeCommand extends Command {
        @Override
        public int getIndex() {
            return 2;
        }

        @Override
        public String getDescription() {
            return "Load a maze";
        }

        @Override
        public void handle() throws IOException {
            String fileName = in.next();
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                System.out.printf("The file %s does not exist%n", fileName);
                return;
            }
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName))) {
                lastMaze = (Maze) is.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Cannot load the maze. It has an invalid format");
            }
        }
    }

    private class SaveMazeCommand extends Command {
        @Override
        public int getIndex() {
            return 3;
        }

        @Override
        public String getDescription() {
            return "Save the maze";
        }

        @Override
        public boolean isVisible() {
            return lastMaze != null;
        }

        @Override
        public void handle() throws IOException {
            String fileName = in.next();
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName))) {
                os.writeObject(lastMaze);
            }
        }
    }

    private class DisplayMazeCommand extends Command {
        @Override
        public int getIndex() {
            return 4;
        }

        @Override
        public String getDescription() {
            return "Display the maze";
        }

        @Override
        public boolean isVisible() {
            return lastMaze != null;
        }

        @Override
        public void handle() {
            print(lastMaze);
        }
    }

    private class FindEscapeCommand extends Command {

        @Override
        public int getIndex() {
            return 5;
        }

        @Override
        public String getDescription() {
            return "Find the escape";
        }

        @Override
        public boolean isVisible() {
            return lastMaze != null;
        }

        @Override
        public void handle() {
            printWithPath(lastMaze, new PathFinder().find(lastMaze));
        }
    }
}
