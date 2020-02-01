import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
//        String logo = " ____        _        \n"
//                + "|  _ \\ _   _| | _____ \n"
//                + "| | | | | | | |/ / _ \\\n"
//                + "| |_| | |_| |   <  __/\n"
//                + "|____/ \\__,_|_|\\_\\___|\n";
//        System.out.println("Hello from\n" + logo);
        Scanner sc = new Scanner(System.in);
        DukeWorker worker = new DukeWorker();
        worker.initializeWorker();
        System.out.println(Constants.ANSI_RED + "Hello! I'm Duke\n" + "What's up?" + Constants.ANSI_RESET);
        String request = "";
        while (!request.split(" ")[0].toLowerCase().equals("bye")) {
            request = sc.nextLine();
            request = request.trim().toLowerCase();
            System.out.println(Constants.ANSI_RED + worker.handleRequest(request) + Constants.ANSI_RESET);
        }
    }
}
