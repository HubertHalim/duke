package duke.command;

import duke.main.UiHandler;
import duke.utils.*;

import java.io.IOException;

/**
 * Done command implementing command interface.
 */
public class DoneCommand implements Command {

    /**
     * Attempts to mark specified tasks as done and set ui response to
     * the appropriate one.
     * @param task task for this execution
     * @param uiHandler ui handler to capture response
     * @param storage storage to be updated
     * @param taskList list of tasks
     */
    @Override
    public void execute(String task, UiHandler uiHandler, Storage storage, TaskList taskList) {
        String[] token = task.split(" ", 2);
        if (token.length < 2) {
            uiHandler.setResponse("Please specify which task to mark as done");
            return;
        }
        String[] indices = token[1].split(" ");
        if (indices.length < 1 || Utils.isNumeric(indices[0]) != true) {
            uiHandler.setResponse("Please specify which task to mark as done");
            return;
        }
        int taskId = Integer.parseInt(indices[0]) - 1;
        if (taskId >= taskList.size() || taskId < 0) {
            uiHandler.setResponse("No such task number");
            return;
        }
        taskList.markTaskAsDone(taskId);
        String response = "Nice! Task(s) marked as done(unknown task number ignored):\n " + taskList.getList().get(taskId);

        for (int i = 1; i < indices.length; i++) {
            if (Utils.isNumeric(indices[i]) != true) {
                continue;
            } else {
                taskId = Integer.parseInt(indices[i]) - 1;
                if (taskId >= taskList.size() || taskId < 0) {
                    continue;
                }
                taskList.markTaskAsDone(taskId);
                response = response + "\n " + taskList.getList().get(taskId);
            }
        }
        try {
            storage.storeData(Parser.tasksToStorage(taskList.getList()));
        } catch (IOException e) {
            System.out.println("Error in storing data");
        }
        uiHandler.setResponse(response);
        return;
    }

}
