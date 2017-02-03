package de.fsmpi.model.option;

import javafx.util.Pair;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "`option`")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Option<V> {

	public static final String DEFAULT_PRINTER = "imp_default_printer";
	public static final String IS_DUPLEX = "imp_is_duplex";
	public static final String PRINT_PASSWORD = "imp_print_password";
	public static final String COST_PER_PAGE = "imp_page_cost";
	public static final String MAIL_DOMAIN = "imp_mail_domain";

	@Transient
	public static final List<Pair<String, Class<? extends Option>>> DEFAULT_OPTIONS = Arrays.asList(
			new Pair<String, Class<? extends Option>>(Option.DEFAULT_PRINTER, PrinterOption.class),
			new Pair<String, Class<? extends Option>>(Option.IS_DUPLEX, BooleanOption.class),
			new Pair<String, Class<? extends Option>>(Option.PRINT_PASSWORD, StringOption.class),
			new Pair<String, Class<? extends Option>>(Option.COST_PER_PAGE, IntegerOption.class),
			new Pair<String, Class<? extends Option>>(Option.MAIL_DOMAIN, StringOption.class)
	);

	@Transient
	private static final Set<String> SUB_CLASSES = new HashSet<>();

	@Id
	@Column
	protected String name;

	@Column
	protected String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Transient
	public boolean hasPossibleValues() {
		return false;
	}

	@Transient
	public abstract V getValueTypeSafe();

	@Transient
	public Iterable<String> getPossibleValues() {
		return null;
	}

	@Transient
	public static void addSubclass(Class<? extends Option> clazz) {
		SUB_CLASSES.add(clazz.getName());
	}

	@Transient
	public static Set<String> getSubClasses() {
		return Collections.unmodifiableSet(SUB_CLASSES);
	}

	@Transient
	public static List<String> getDefaultOptionNames() {
		List<String> ret = new ArrayList<>();
		for (Pair<String, Class<? extends Option>> defaultOption : DEFAULT_OPTIONS) {
			ret.add(defaultOption.getKey());
		}
		return ret;
	}
}
