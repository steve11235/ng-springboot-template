export interface Entity {
    /**
     * Validate the entity state, return true if valid, false otherwise.
     * Typically, error messages should be added to the MessageManager.
     */
    validate: () => boolean;
}
