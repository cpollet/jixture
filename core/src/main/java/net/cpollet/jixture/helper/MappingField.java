package net.cpollet.jixture.helper;

import java.lang.reflect.Field;

/**
 * @author Christophe Pollet
 */
public class MappingField {
	private Field field;
	private Field embeddableField;

	public MappingField(Field field) {
		this.field = field;
	}

	public MappingField(Field field, Field embeddableField) {
		this(field);
		this.embeddableField = embeddableField;
	}

	public boolean isEmbedded() {
		return embeddableField != null;
	}

	public Field getField() {
		return field;
	}

	public Field getEmbeddableField() {
		return embeddableField;
	}
}
