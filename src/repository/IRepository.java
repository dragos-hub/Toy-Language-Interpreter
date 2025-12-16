package repository;

import model.prgState.PrgState;
public interface IRepository {
    PrgState getCrtPrg();
    void logPrgStateExec() throws RepoException;
}
