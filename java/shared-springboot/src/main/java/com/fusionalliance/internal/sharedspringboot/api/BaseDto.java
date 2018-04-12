/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 22, 2017
 */
package com.fusionalliance.internal.sharedspringboot.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fusionalliance.internal.sharedutility.core.GenericCastUtility;
import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.core.TreatAsProtected;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;

/**
 * This abstract class defines base logic for DTO objects.
 * <p>
 * Instances can be instantiated directly, or they can be created from JSON using GSON or a similar framework using reflection. Therefore, do not
 * count on setters to filter values. Note that GSON respects the <code>transient</code> modifier; other frameworks may use another scheme.
 * <p>
 * By convention, all implementations should
 * <ul>
 * <li>be marked as final (all implementations are closed for further extension)
 * <li>use fluent setters and build()</li>
 * <li>override the validate() method, calling super.validate() at the beginning of the method</li>
 * <li>use the validate method to ensure that required fields are populated, supply any default values to optional fields that were not supplied, and
 * verify that all fields contain reasonable values</li>
 * </ul>
 */
public abstract class BaseDto<T extends BaseDto<?>> {
	/** List of error messages generated during validation; no messages means validation was successful */
	@JsonIgnore
	private transient final List<String> validationErrors = new ArrayList<String>();
	/** Flag indicating that the build() method has been called */
	@JsonIgnore
	private transient boolean built;

	/**
	 * Build the instance and validate; this prevents further setting of fields.
	 */
	public final T build() {
		built = true;

		validate();

		return GenericCastUtility.cast(this);
	}

	public final boolean isValidationErrors() {
		return !validationErrors.isEmpty();
	}

	public final List<String> getValidationErrors() {
		return new ArrayList<String>(validationErrors);
	}

	/**
	 * Check that the instance is not built.
	 * <p>
	 * All fluent setters should call this method first.
	 * 
	 * @throws IllegalUpdateException
	 */
	protected final void checkNotBuilt() {
		if (built) {
			throw new IllegalUpdateException();
		}
	}

	/**
	 * Validate the instance.
	 * <p>
	 * Override and implement this method in all subclasses.
	 * <p>
	 * Do NOT invoke this method, except,
	 * <ul>
	 * <li>Call super.validate() first thing in overriding methods</li>
	 * <li>Call from a containing DTO's validateChildDto() method</li>
	 * </ul>
	 */
	@TreatAsProtected("public to allow embedded validation across packages")
	public void validate() {
		// Do nothing
	}

	/**
	 * Validate child DTO, i.e., those defined as fields of the main class, and add their validation errors to this class' validation errors.
	 * <p>
	 * This ensures that the main class' error list contains all errors.
	 * 
	 * @param childInboundDtoParm
	 */
	protected final <C extends BaseDto<?>> void validateChildDto(final C childInboundDtoParm) {
		childInboundDtoParm.validate();

		if (!childInboundDtoParm.isValidationErrors()) {
			return;
		}

		for (String childValidationError : childInboundDtoParm.getValidationErrors()) {
			addValidationError(childValidationError);
		}
	}

	/**
	 * Add a validation error.
	 * 
	 * @param validationErrorParm
	 *            required
	 */
	protected final void addValidationError(final String validationErrorParm) {
		ValidationUtility.checkStringNotBlank("The validation error message is blank.", validationErrorParm);

		validationErrors.add(validationErrorParm);
	}

	public String toJson() {
		final String json = GsonHelper.GSON.toJson(this);

		return json;
	}
}
