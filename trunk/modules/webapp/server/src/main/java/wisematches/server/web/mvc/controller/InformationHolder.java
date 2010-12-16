/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klimese
 */
public class InformationHolder {
	private String label = "";
	private String description = "";
	private String image = "";
	private List<InformationHolder> items;

	private final String propertyKey;

	public InformationHolder(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public List<InformationHolder> getItems() {
		return items;
	}

	void addItem(InformationHolder item) {
		if (items == null) {
			items = new ArrayList<InformationHolder>();
		}
		items.add(item);
	}

	void setLabel(String label) {
		this.label = label;
	}

	void setDescription(String description) {
		this.description = description;
	}

	void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("InformationHolder");
		sb.append("{propertyKey='").append(propertyKey).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
