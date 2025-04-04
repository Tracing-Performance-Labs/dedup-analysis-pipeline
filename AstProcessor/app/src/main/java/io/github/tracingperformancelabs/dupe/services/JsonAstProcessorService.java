package io.github.tracingperformancelabs.dupe.services;

import com.google.gson.*;

import java.util.function.Consumer;

public class JsonAstProcessorService implements AstProcessorService<String, String> {
    private final Gson gson;

    public JsonAstProcessorService() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void visit(String root, Consumer<String> consumer) {
        try {
            JsonElement rootElement = JsonParser.parseString(root).getAsJsonObject();
            visit(rootElement, consumer);
        } catch (JsonSyntaxException ignored) {
        }
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
