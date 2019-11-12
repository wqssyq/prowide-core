package com.prowidesoftware.swift.model.field;

import com.prowidesoftware.deprecation.ProwideDeprecated;
import com.prowidesoftware.deprecation.TargetYear;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Branch of Sender/Receiver
 *
 * <p>Subfields (components) Data types
 * <ol>
 * 		<li><code>String</code></li>
 * 		<li><code>String</code></li>
 * 		<li><code>String</code></li>
 * </ol>
 *
 * <p>Structure definition
 * <ul>
 * 		<li>validation pattern: <code>[[/&lt;DC&gt;][/34x]$][35x](****)</code></li>
 * 		<li>parser pattern: <code>[[/c][/S]$][S]</code></li>
 * 		<li>components pattern: <code>SSS</code></li>
 * </ul>
 *
 * @since 7.11.0
 */
public class OptionBPartyField extends Field {
    public static final String PARSER_PATTERN ="[[/c][/S]$][S]";
    public static final String COMPONENTS_PATTERN = "SSS";

    /**
     * Component number for the D/C Mark subfield
     */
    public static final Integer DC_MARK = 1;

    /**
     * Component number for the Account subfield
     */
    public static final Integer ACCOUNT = 2;

    /**
     * Component number for the Location subfield
     */
    public static final Integer LOCATION = 3;

    /**
     * Default constructor. Creates a new field setting all components to null.
     */
    public OptionBPartyField() {
        super(3);
    }

    /**
     * Creates a new field and initializes its components with content from the parameter value.
     * @param value complete field value including separators and CRLF
     */
    public OptionBPartyField(final String value) {
        super(value);
    }

    /**
     * Parses the parameter value into the internal components structure.
     *
     * <p>Used to update all components from a full new value, as an alternative
     * to setting individual components. Previous component values are overwritten.
     *
     * @param value complete field value including separators and CRLF
     * @since 7.8
     */
    @Override
    public void parse(final String value) {
        init(3);
        List<String> lines = SwiftParseUtils.getLines(value);
        if (lines.isEmpty()) {
            return;
        }
        if (lines.get(0).startsWith("/")) {
            String dcMark = SwiftParseUtils.getTokenFirst(lines.get(0), "/", "/");
            if (StringUtils.isNotEmpty(dcMark) && dcMark.length() == 1) {
                setComponent(1, dcMark);
                setComponent(2, SwiftParseUtils.getTokenSecondLast(StringUtils.substring(lines.get(0), 1), "/"));
            } else {
                setComponent(2, StringUtils.substring(lines.get(0), 1));
            }
            if (lines.size() > 1) {
                setComponent(3, lines.get(1));
            }
        } else {
            setComponent(3, lines.get(0));
        }
    }

    /**
     * Serializes the fields' components into the single string value (SWIFT format)
     */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        if (getComponent1() != null) {
            result.append("/").append(getComponent1());
        }
        if (getComponent2() != null) {
            result.append("/").append(getComponent2());
        }
        if (getComponent3() != null) {
            if (result.length() > 0) {
                result.append(com.prowidesoftware.swift.io.writer.FINWriterVisitor.SWIFT_EOL);
            }
            result.append(getComponent3());
        }
        return result.toString();
    }

    /**
     * Returns a localized suitable for showing to humans string of a field component.<br>
     *
     * @param component number of the component to display
     * @param locale optional locale to format date and amounts, if null, the default locale is used
     * @return formatted component value or null if component number is invalid or not present
     * @throws IllegalArgumentException if component number is invalid for the field
     * @since 7.8
     */
    @Override
    public String getValueDisplay(int component, Locale locale) {
        if (component < 1 || component > 3) {
            throw new IllegalArgumentException("invalid component number "+component+" for field 52B");
        }
        //default format (as is)
        return getComponent(component);
    }

    /**
     * Returns the field components pattern
     * @return the static value of Field52B.COMPONENTS_PATTERN
     */
    @Override
    public final String componentsPattern() {
        return COMPONENTS_PATTERN;
    }

    /**
     * Returns the field parser pattern
     * @return the static value of Field52B.PARSER_PATTERN
     */
    @Override
    public final String parserPattern() {
        return PARSER_PATTERN;
    }

    /**
     * Returns the field validator pattern
     */
    @Override
    public final String validatorPattern() {
        return "[[/<DC>][/34x]$][35x](****)";
    }

    /**
     * Given a component number it returns true if the component is optional,
     * regardless of the field being mandatory in a particular message.<br>
     * Being the field's value conformed by a composition of one or several
     * internal component values, the field may be present in a message with
     * a proper value but with some of its internal components not set.
     *
     * @param component component number, first component of a field is referenced as 1
     * @return true if the component is optional for this field, false otherwise
     */
    @Override
    public boolean isOptional(int component) {
        if (component == 1) {
            return true;
        }
        if (component == 2) {
            return true;
        }
        if (component == 3) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the field is a GENERIC FIELD as specified by the standard.
     * @return true if the field is generic, false otherwise
     */
    @Override
    public boolean isGeneric() {
        return false;
    }

    /**
     * Returns the defined amount of components.<br>
     * This is not the amount of components present in the field instance, but the total amount of components
     * that this field accepts as defined.
     * @since 7.7
     */
    @Override
    public int componentsSize() {
        return 3;
    }

    /**
     * Returns english label for components.
     * <br>
     * The index in the list is in sync with specific field component structure.
     * @see #getComponentLabel(int)
     * @since 7.8.4
     */
    @Override
    protected List<String> getComponentLabels() {
        List<String> result = new ArrayList<>();
        result.add("D/C Mark");
        result.add("Account");
        result.add("Location");
        return result;
    }

    /**
     * Returns a mapping between component numbers and their label in camel case format.
     * @since 7.10.3
     */
    @Override
    protected Map<Integer, String> getComponentMap() {
        Map<Integer, String> result = new HashMap<>();
        result.put(1, "dCMark");
        result.put(2, "account");
        result.put(3, "location");
        return result;
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Gets the component1 (D/C Mark).
     * @return the component1
     */
    public String getComponent1() {
        return getComponent(1);
    }

    /**
     * Same as getComponent(1)
     * @deprecated use {@link #getComponent(int)} instead
     */
    @Deprecated
    @ProwideDeprecated(phase4=TargetYear.SRU2020)
    public java.lang.String getComponent1AsString() {
        com.prowidesoftware.deprecation.DeprecationUtils.phase3(getClass(), "getComponent1AsString()", "Use use #getComponent(int) instead.");
        return getComponent(1);
    }

    /**
     * Gets the D/C Mark (component1).
     * @return the D/C Mark from component1
     */
    public String getDCMark() {
        return getComponent(1);
    }
    /**
     * Gets the component2 (Account).
     * @return the component2
     */
    public String getComponent2() {
        return getComponent(2);
    }

    /**
     * Same as getComponent(2)
     * @deprecated use {@link #getComponent(int)} instead
     */
    @Deprecated
    @ProwideDeprecated(phase4=TargetYear.SRU2020)
    public java.lang.String getComponent2AsString() {
        com.prowidesoftware.deprecation.DeprecationUtils.phase3(getClass(), "getComponent2AsString()", "Use use #getComponent(int) instead.");
        return getComponent(2);
    }

    /**
     * Gets the Account (component2) removing its starting slashes if any.
     * @return the Account from component2
     */
    public String getAccount() {
        String c = getComponent(2);
        if (c != null) {
            for (int i=0; i<c.length(); i++) {
                if (c.charAt(i) != '/') {
                    return c.substring(i);
                }
            }
            return "";
        }
        return null;
    }
    /**
     * Gets the component3 (Location).
     * @return the component3
     */
    public String getComponent3() {
        return getComponent(3);
    }

    /**
     * Same as getComponent(3)
     * @deprecated use {@link #getComponent(int)} instead
     */
    @Deprecated
    @ProwideDeprecated(phase4=TargetYear.SRU2020)
    public java.lang.String getComponent3AsString() {
        com.prowidesoftware.deprecation.DeprecationUtils.phase3(getClass(), "getComponent3AsString()", "Use use #getComponent(int) instead.");
        return getComponent(3);
    }

    /**
     * Gets the Location (component3).
     * @return the Location from component3
     */
    public String getLocation() {
        return getComponent(3);
    }

}