/**
 * Populate the class instance (that is presumably empty) using the values in the DTO.
 *
 * @param classInstance required
 * @param dto required
 */
export function populateClassInstanceFromDto(classInstance: any, dto: any) {
  if (!classInstance) {
    throw new Error('The class instance is null.');
  }
  if (!dto) {
    throw new Error('The dto is null.');
  }

  const classInstanceKeys = Object.keys(classInstance);
  classInstanceKeys.forEach(key => {
    // A leading lodash indicates that the class uses setters
    const cleanKey = key.startsWith('_') ? key.substring(1) : key;
    if (!dto.hasOwnProperty(cleanKey)) {
      return;
    }

    classInstance[cleanKey] = dto[cleanKey];
  });

  // If the class supports immutablility, seal it
  if (classInstance.seal) {
    classInstance.seal();
  }
}
