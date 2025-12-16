package model.prgState;

import model.prgState.exeStack.IMyStack;
import model.prgState.exeStack.MyStack;
import model.prgState.fileTable.IMyFileTable;
import model.prgState.fileTable.MyFileTable;
import model.prgState.heap.IMyHeap;
import model.prgState.heap.MyHeap;
import model.prgState.out.IMyList;
import model.prgState.out.MyList;
import model.prgState.symTable.IMyDictionary;
import model.prgState.symTable.MyDictionary;
import model.statement.IStmt;
import model.value.Value;
import model.value.type.Type;

public class PrgState {
    private IMyStack<IStmt> exeStack;
    private IMyDictionary<String, Value> symTable;
    private IMyDictionary<String, Type> typeEnv;
    private IMyList<Value> out;
    private IMyFileTable fileTable;
    private IMyHeap heap;
    private IStmt originalProgram;

    public PrgState(IStmt prg) {
        this.exeStack = new MyStack<>();
        this.symTable = new MyDictionary<>();
        this.out = new MyList<>();
        this.fileTable = new MyFileTable();
        this.heap = new MyHeap();
        this.typeEnv = new MyDictionary<>();
        this.originalProgram = prg.deepCopy();
        exeStack.push(prg);
    }

    public IMyStack<IStmt> getExeStack() {
        return exeStack;
    }
    public IMyDictionary<String, Value> getSymTable() {
        return symTable;
    }
    public IMyList<Value> getOut() {
        return out;
    }
    public IMyFileTable getFileTable() {
        return fileTable;
    }
    public IMyHeap getHeap() {
        return heap;
    }
    public IMyDictionary<String, Type> getTypeEnv() {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Stack: " + exeStack.toString() + "\n" +
                "SymTable: " + symTable.toString() + "\n" +
                "Out: " + out.toString() + "\n" +
                "FileTable: " + fileTable.toString() + "\n" +
                "Heap: " + heap.toString() + "\n" +
                "TypeEnv: " + typeEnv.toString();
    }
}
