package com.yuhtin.quotes.waitlistbot.repository.mongo;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@Data
public final class RepositoryCollectionField {

    private final Field holder;
    private final RepositoryCollection collectionAnnotation;

    public RepositoryCollectionField(Field holder) {
        this.holder = holder;
        this.collectionAnnotation = holder.getAnnotation(RepositoryCollection.class);
    }

    public void accessible() {
        holder.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);

            modifiersField.setInt(holder, holder.getModifiers() & ~Modifier.PRIVATE);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
    }

}
