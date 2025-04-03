package io.github.tracingperformancelabs.dupe.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.function.Consumer;

public class JsonAstProcessorService implements AstProcessorService<Object, String> {
    private final Gson gson;

    public JsonAstProcessorService() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void visit(Object root, Consumer<String> consumer) {
        JsonElement rootElement = gson.toJsonTree(root);
        visit(rootElement, consumer);
    }

    private void visit(JsonElement element, Consumer<String> consumer) {
        if (element.isJsonArray()) {
            final var ary = element.getAsJsonArray();
            ary.asList().forEach(e -> visit(e, consumer));
        } else if (element.isJsonObject()) {
            final var obj = element.getAsJsonObject();
            obj.entrySet().forEach(entry -> {
                consumer.accept(entry.getKey());
                visit(entry.getValue(), consumer);
            });
        } else if (element.isJsonPrimitive()) {
            final var prim = element.getAsJsonPrimitive();
            if (prim.isString()) {
                final var str = prim.getAsString();
                consumer.accept(str);
            }
        }
    }
}
