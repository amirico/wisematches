package wisematches.server.web.controllers;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class UnknownEntityException extends Exception {
	private final Object entityId;
	private final String entityType;

	public UnknownEntityException(Object entityId, String entityType) {
		super("Entity " + entityId + " of " + entityType + " not found");
		this.entityId = entityId;
		this.entityType = entityType;
	}

	public Object getEntityId() {
		return entityId;
	}

	public String getEntityType() {
		return entityType;
	}
}
