package dataaccess;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private Object gameDB;


    public MemoryGameDAO(){
        this.gameDB = new Object();
        //throw new RuntimeException("Not implemented!");
    }


    public void clearGameTable(){
        this.gameDB = new Object();
        //throw new RuntimeException("Not implemented");
    }
}
