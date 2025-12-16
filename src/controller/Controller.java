package controller;

import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.statement.IStmt;
import model.statement.StmtException;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repository;
    private boolean displayFlag;

    private List<Integer> getReachableAddresses(Collection<Value> symTableValues,
                                                Map<Integer, Value> heap) {
        List<Integer> workList = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        // 1. Start with addresses from the symTable
        for (Value v : symTableValues) {
            if (v instanceof RefValue ref) {
                workList.add(ref.getAddr());
            }
        }

        // 2. BFS/DFS through the heap references
        while (!workList.isEmpty()) {
            int addr = workList.remove(0);

            if (addr != 0 && !visited.contains(addr) && heap.containsKey(addr)) {
                visited.add(addr);

                Value heapVal = heap.get(addr);

                if (heapVal instanceof RefValue refVal) {
                    workList.add(refVal.getAddr());
                }
            }
        }

        return new ArrayList<>(visited);
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> reachableAddrs,
                                                     Map<Integer, Value> heap) {
        return heap.entrySet()
                .stream()
                .filter(e -> reachableAddrs.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Controller(IRepository repository, boolean displayFlag) {
        this.repository = repository;
        this.displayFlag = displayFlag;
    }

    PrgState oneStep(PrgState state) throws CtrlException {
        IMyStack<IStmt> stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new CtrlException("Stack is empty");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws CtrlException {
        PrgState prg = repository.getCrtPrg();

        if (displayFlag) {
            System.out.println("Initial program state:\n" + prg.toString());
        }

        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            repository.logPrgStateExec();
            if (displayFlag) {
                System.out.println("Current program state:\n" + prg.toString());
            }
            List<Integer> reachable = getReachableAddresses(
                    prg.getSymTable().getContent().values(),
                    prg.getHeap().getContent()
            );

            prg.getHeap().setContent(
                    safeGarbageCollector(
                            reachable,
                            prg.getHeap().getContent()
                    )
            );
            repository.logPrgStateExec();
            if (displayFlag) {
                System.out.println("Current program state after garbage collector:\n" + prg.toString());
            }
        }
        for (int i = 0; i < prg.getOut().getList().size(); i++) {
            System.out.println(prg.getOut().getList().get(i).toString());
        }

        if (displayFlag) {
            System.out.println("Execution finished!\n");
        }
    }
}
