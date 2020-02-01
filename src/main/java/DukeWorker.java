import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DukeWorker {

    private List<Task> tasks = new ArrayList<>();

    public void initializeWorker() {
        try {
            boolean fileExist = Storage.checkFileExistence(Constants.DUKE_FILE_PATH);
            if (!fileExist) {
                Storage.createStorage();
            } else {
                String data = Storage.loadStorage(Constants.DUKE_FILE_PATH);
                tasks = Parser.storageToTask(data);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String handleRequest(String request) {
        String[] token = request.split(" ", 2);
        if (token[0].equals("bye")) {
            return "Bye ciao adios";
        } else if (token[0].equals("list")) {
            if (tasks.size() == 0) {
                return "No scheduled task yet";
            }
            String list = "1." + tasks.get(0);
            for (int i = 2; i < tasks.size() + 1; i++) {
                list = list + "\n" + i + "." + tasks.get(i - 1);
            }
            return list;
        } else if (token[0].equals("done")) {
            if (token.length < 2) {
                return "Please specify which task to mark as done";
            }
            String[] indices = token[1].split(" ");
            if (indices.length < 1 || isNumeric(indices[0]) != true) {
                return "Please specify which task to mark as done";
            }
            int taskId = Integer.parseInt(indices[0]) - 1;
            if (taskId >= tasks.size() || taskId < 0) {
                return "No such task number";
            }
            tasks.get(taskId).markAsDone();
            try {
                Storage.storeData(Parser.tasksToStorage(tasks), Constants.DUKE_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Error in storing data");
            }
            String response = "Nice! Task(s) marked as done(unknown task number ignored):\n " + tasks.get(taskId);

            for (int i = 1; i < indices.length; i++) {
                if (isNumeric(indices[i]) != true) {
                    continue;
                } else {
                    taskId = Integer.parseInt(indices[i]) - 1;
                    if (taskId >= tasks.size() || taskId < 0) {
                        continue;
                    }
                    tasks.get(taskId).markAsDone();
                    response = response + "\n " + tasks.get(taskId);
                }
            }
            return response;
        } else if (token[0].equals("delete")) {
            if (token.length < 2) {
                return "Please specify which task to delete";
            }
            String[] indices = token[1].split(" ");
            if (!isNumeric(indices[0])) {
                return "Please specify which task to delete";
            }
            List<Integer> toBeDeleted = new ArrayList<>();
            for (int i = 0; i < indices.length; i++) {
                if (isNumeric(indices[i]) != true) {
                    continue;
                } else {
                    toBeDeleted.add(Integer.parseInt(indices[i]) - 1);
                }
            }
            Collections.sort(toBeDeleted);
            if (toBeDeleted.size() < 1) {
                return "Please specify which task to delete";
            }
            if (toBeDeleted.get(0) >= tasks.size() || toBeDeleted.get(toBeDeleted.size() - 1) < 0) {
                return "No such task number";
            }
            String response = "";
            for (int i = toBeDeleted.size() - 1; toBeDeleted.size() > 0 && i >= 0; i--) {
                int taskId = toBeDeleted.get(i);
                if (taskId >= tasks.size() || taskId < 0) {
                    continue;
                }
                if (response.equals("")) {
                    response = tasks.get(taskId) + response;
                } else {
                    response = tasks.get(taskId) + "\n " + response;
                }
                tasks.remove(taskId);
            }
            try {
                Storage.storeData(Parser.tasksToStorage(tasks), Constants.DUKE_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Error in storing data");
            }
            response = "Nice! Deleted tasks(unknown task number ignored):\n " + response;
            return response;
        } else if (token[0].equals("todo") || token[0].equals("deadline") || token[0].equals("event")) {
           if (token.length < 2) {
               return "Adding task failed, task body cannot be empty";
           }
           if (addToList(token[1], token[0])) {
               try {
                   Storage.storeData(Parser.tasksToStorage(tasks), Constants.DUKE_FILE_PATH);
               } catch (IOException e) {
                   System.out.println("Error in storing data");
               }
               return "I've added this task to the list:\n " + tasks.get(tasks.size() - 1) + "\n" +
                       "Now you have " + tasks.size() + " task(s) in the list";
           } else {
               return "Adding task failed, either task body is empty or required time is not specified";
           }
        } else {
            return "I don't understand what you want";
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean addToList(String item, String type) {
        Task newTask;
        if (type.equals("todo")) {
            newTask = new Todos(item);
        } else if (type.equals("deadline")) {
            String[] tokens = item.split("/", 2);
            if (tokens.length < 2) {
                return false;
            }
            if (!tokens[1].substring(0, 2).equals("by")) {
                return false;
            }
            String time = tokens[1].substring(2).trim();
            if (!Parser.checkDateFormat(time)) {
                return false;
            }
            newTask = new Deadlines(tokens[0].trim(), Parser.stringToDate(time));
        } else {
            String[] tokens = item.split("/", 2);
            if (tokens.length < 2) {
                return false;
            }
            if (!tokens[1].substring(0, 2).equals("at")) {
                return false;
            }
            String time = tokens[1].substring(2).trim();
            if (!Parser.checkDateFormat(time)) {
                return false;
            }
            newTask = new Events(tokens[0].trim(), Parser.stringToDate(time));
        }
        tasks.add(newTask);
        return true;
    }

    private List<Task> getList() {
        return tasks;
    }
}
