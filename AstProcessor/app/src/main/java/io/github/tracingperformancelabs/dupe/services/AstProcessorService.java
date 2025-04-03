package io.github.tracingperformancelabs.dupe.services;

import java.util.function.Consumer;

public interface AstProcessorService<TNode, TContent> {
    /**
     * Visit the ast at root and call consumer for each node.
     *
     * @param root     The ast root.
     * @param consumer A callback to be executed on each leaf node.
     */
    void visit(TNode root, Consumer<TContent> consumer);
}
