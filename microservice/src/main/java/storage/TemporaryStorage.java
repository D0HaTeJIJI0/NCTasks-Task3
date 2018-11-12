package storage;

import entity.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TemporaryStorage {
    private Map<String, Object> storage = new HashMap<>(256);

    private TemporaryStorage() {
    }

    public Object getEntity(String id) {
        return storage.get(id);
    }

    public List<Object> getAllEntities() {
        return new ArrayList<>(storage.values());
    }

    public void insertEntity(String id, Object entity) {
        storage.put(id, entity);
    }

    public static TemporaryStorage getInstance() {
        return Holder.instance;
    }

    public void deleteEntity(String id) {
        storage.remove(id);
    }

    public void deleteEntity(Person person) {
        List<String> listEntitiesToRemove = new ArrayList<>();
        for (String key: storage.keySet()) {
            if (person.equals(storage.get(key))) {
                listEntitiesToRemove.add(key);
            }
        }
        for (String key: listEntitiesToRemove) {
            storage.remove(key);
        }
    }

    private static class Holder {
        private static TemporaryStorage instance = new TemporaryStorage();
    }
}