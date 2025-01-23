// The "Caretaker" class that stores the mementos
import java.util.List;
import java.util.ArrayList;
public class MementoHistory {
    private List<Memento> mementos;
    MementoHistory(){
        mementos = new ArrayList<Memento>();
    }
    public void addMemento(Memento memento){
        mementos.add(memento);
    }
    // Since this is only for undoing moves. it will always retrive the most recent memento and remove it from the list.
    public Memento getMemento(){
        Memento savedMemento = mementos.get(mementos.size() - 1);
        mementos.remove(mementos.size() - 1);
        return savedMemento;
    }
}
