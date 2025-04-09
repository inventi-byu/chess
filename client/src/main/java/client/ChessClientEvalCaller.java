package client;

public class ChessClientEvalCaller {

    private final ChessClient client;

    public ChessClientEvalCaller(ChessClient client){
        this.client = client;
    }
    public String evalLine(String line){
        String result = "";
        String[] command = line.split(" ");
        result = switch (command[0]) {
            case "quit" -> this.client.evalQuit();
            case "help" -> this.client.evalHelp();
            case "register" -> this.client.evalRegister(command);
            case "login" -> this.client.evalLogin(command);
            case "create" -> this.client.evalCreate(command);
            case "list" -> this.client.evalList();
            case "join" -> this.client.evalJoin(command);
            case "observe" -> this.client.evalObserve(command);
            case "logout" -> this.client.evalLogout();
            case "highlight" -> this.client.evalHighlight(command);
            case "redraw" -> this.client.evalRedraw();
            case "leave" -> this.client.evalLeave();
            case "move" -> this.client.evalMove(command);
            case "resign" -> this.client.evalResign();
            default -> "Unknown command.";
        };
        return result;
    }
}
