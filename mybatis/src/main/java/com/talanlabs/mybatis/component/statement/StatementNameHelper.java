package com.talanlabs.mybatis.component.statement;

import com.talanlabs.component.IComponent;
import com.talanlabs.mybatis.component.helper.ComponentMyBatisHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatementNameHelper {

    public static final String FIND_ENTITY_BY_ID_NAME = "findEntityById";

    public static final String FIND_COMPONENTS_BY_NAME = "findComponentsBy";

    public static final String FIND_COMPONENTS_BY_JOIN_TABLE_NAME = "findComponentsByJoinTable";

    public static final String INSERT_NAME = "insert";

    public static final String UPDATE_NAME = "update";

    public static final String DELETE_NAME = "delete";

    private static final String DELETE_ENTITY_BY_ID_NAME = "deleteEntityById";

    private static final String DELETE_COMPONENTS_BY_NAME = "deleteComponentsBy";

    private static final String FIND_NLS_COLUMN_NAME = "findNlsColumn";

    public static final String PROPERTY = "property";

    public static final String PROPERTIES = "properties";

    public static final String SOURCE_PROPERTIES = "sourceProperties";

    public static final String TARGET_PROPERTIES = "targetProperties";

    public static final String SOURCE_COMPONENT = "sourceComponent";

    public static final String NLS_PROPERTIES = "nlsProperties";

    public static final String JOIN = "join";

    public static final String IGNORE_CANCEL = "ignoreCancel";

    public static final String ORDER_BY = "orderBy";

    public static final String PARAM = "";

    public static final String PROPERTIES_SEPARATOR = ",";

    public static final String COMPONENT_CLASS_PAT = "([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*";

    public static final String PROPERTY_PAT = "[a-zA-Z_$][a-zA-Z\\d_$]*";

    public static final String PROPERTIES_PAT = "(" + PROPERTY_PAT + PROPERTIES_SEPARATOR + ")*" + PROPERTY_PAT;

    public static final String JOIN_PAT = PROPERTY_PAT + ";" + PROPERTIES_PAT + ";" + PROPERTIES_PAT;

    public static final String JOINS_PAT = "(" + JOIN_PAT + "#)*" + JOIN_PAT;

    public static final String SORT_PAT = "(Asc|Desc)";

    public static final String ORDER_BY_PAT = PROPERTY_PAT + ";" + SORT_PAT;

    public static final String ORDERS_BY_PAT = "(" + ORDER_BY_PAT + "#)*" + ORDER_BY_PAT;

    public static final Pattern FIND_ENTITY_BY_ID_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + FIND_ENTITY_BY_ID_NAME);

    public static final Pattern FIND_COMPONENTS_BY_PATTERN = Pattern.compile(
            "(" + COMPONENT_CLASS_PAT + ")/" + FIND_COMPONENTS_BY_NAME + "\\?" + PROPERTIES + "=(" + PROPERTIES_PAT + ")(&" + ORDER_BY + "=(" + ORDERS_BY_PAT + "))?(&(" + IGNORE_CANCEL + "))?");

    public static final Pattern FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN = Pattern.compile(
            "(" + COMPONENT_CLASS_PAT + ")/" + FIND_COMPONENTS_BY_JOIN_TABLE_NAME + "\\?" + SOURCE_COMPONENT + "=(" + COMPONENT_CLASS_PAT + ")&" + SOURCE_PROPERTIES + "=(" + PROPERTIES_PAT + ")&"
                    + TARGET_PROPERTIES + "=(" + PROPERTIES_PAT + ")&" + JOIN + "=(" + JOINS_PAT + ")(&" + ORDER_BY + "=(" + ORDERS_BY_PAT + "))?(&(" + IGNORE_CANCEL + "))?");

    public static final Pattern INSERT_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + INSERT_NAME);

    public static final Pattern UPDATE_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + UPDATE_NAME + "(\\?" + NLS_PROPERTIES + "=(" + PROPERTIES_PAT + ")?)?");

    public static final Pattern DELETE_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + DELETE_NAME);

    private static final Pattern DELETE_ENTITY_BY_ID_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + DELETE_ENTITY_BY_ID_NAME);

    private static final Pattern DELETE_COMPONENTS_BY_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + DELETE_COMPONENTS_BY_NAME + "\\?" + PROPERTIES + "=(" + PROPERTIES_PAT + ")");

    private static final Pattern FIND_NLS_COLUMN_PATTERN = Pattern.compile("(" + COMPONENT_CLASS_PAT + ")/" + FIND_NLS_COLUMN_NAME + "\\?" + PROPERTY + "=(" + PROPERTY_PAT + ")");

    private StatementNameHelper() {
        super();
    }

    /**
     * Get a param
     *
     * @param i number
     * @return param + number
     */
    public static String buildParam(int i) {
        return PARAM + i;
    }

    // FindEntityById

    /**
     * Build find entity by id key
     *
     * @param componentClass component class
     * @return key
     */
    public static <E extends IComponent> String buildFindEntityByIdKey(Class<E> componentClass) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + FIND_ENTITY_BY_ID_NAME;
    }

    /**
     * Verify is find entity by id key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindEntityByIdKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_ENTITY_BY_ID_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInFindEntityByIdKey(String key) {
        if (!isFindEntityByIdKey(key)) {
            return null;
        }
        Matcher m = FIND_ENTITY_BY_ID_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    // FindComponentsBy

    /**
     * Build find components by id key
     *
     * @param componentClass component class
     * @param useCheckCancel use check cancel
     * @param propertyNames  array of property
     * @param orderBys       list of order by
     * @return key
     */
    public static <E extends IComponent> String buildFindComponentsByKey(Class<E> componentClass, boolean useCheckCancel, String[] propertyNames, List<Pair<String, String>> orderBys) {
        if (componentClass == null || propertyNames == null || propertyNames.length == 0) {
            return null;
        }
        List<String> os = orderBys != null && !orderBys.isEmpty() ? orderBys.stream().map(o -> o.getLeft() + ";" + o.getRight()).collect(Collectors.toList()) : null;
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + FIND_COMPONENTS_BY_NAME + "?" + PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, propertyNames) + (os != null ?
                "&" + ORDER_BY + "=" + String.join("#", os) :
                "") + (useCheckCancel ? "&" + IGNORE_CANCEL : "");
    }

    /**
     * Verify is find components by
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindComponentsByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_COMPONENTS_BY_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInFindComponentsByKey(String key) {
        if (!isFindComponentsByKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    /**
     * Extract properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractPropertyNamesInFindComponentsByKey(String key) {
        if (!isFindComponentsByKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract order by
     *
     * @param key key
     * @return order by
     */
    public static List<Pair<String, String>> extractOrderBiesInFindComponentsByKey(String key) {
        if (!isFindComponentsByKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        List<Pair<String, String>> res = new ArrayList<>();
        String group = m.group(6);
        if (StringUtils.isNotBlank(group)) {
            String[] os = group.split("#");
            for (String o : os) {
                String[] ss = o.split(";");
                res.add(Pair.of(ss[0], ss[1]));
            }
        }
        return res;
    }

    /**
     * Extract ignore cancel
     *
     * @param key key
     * @return true or false
     */
    public static boolean isIgnoreCancelInFindComponentsByKey(String key) {
        if (!isFindComponentsByKey(key)) {
            return false;
        }
        Matcher m = FIND_COMPONENTS_BY_PATTERN.matcher(key);
        return m.find() && IGNORE_CANCEL.equals(m.group(11));
    }

    // FindComponentsByJoinTable

    /**
     * Create a key for Find a components by join table
     *
     * @param sourceComponentClass source component class
     * @param targetComponentClass target component class
     * @param useCheckCancel       use check cancel
     * @param joins                list of join
     * @param sourceProperties     properties source
     * @param targetProperties     properties target
     * @param orderBys             list of order by
     * @return key
     */
    public static <E extends IComponent, F extends IComponent> String buildFindComponentsByJoinTableKey(Class<E> sourceComponentClass, Class<F> targetComponentClass, boolean useCheckCancel,
            List<Pair<String, Pair<String[], String[]>>> joins, String[] sourceProperties, String[] targetProperties, List<Pair<String, String>> orderBys) {
        if (sourceComponentClass == null || targetComponentClass == null || sourceProperties == null || sourceProperties.length == 0 || joins == null || joins.size() == 0 || targetProperties == null
                || targetProperties.length == 0) {
            return null;
        }
        List<String> js = joins.stream()
                .map(join -> join.getLeft() + ";" + String.join(PROPERTIES_SEPARATOR, join.getRight().getLeft()) + ";" + String.join(PROPERTIES_SEPARATOR, join.getRight().getRight()))
                .collect(Collectors.toList());
        List<String> os = orderBys != null && !orderBys.isEmpty() ? orderBys.stream().map(o -> o.getLeft() + ";" + o.getRight()).collect(Collectors.toList()) : null;
        return ComponentMyBatisHelper.componentClassToString(targetComponentClass) + "/" + FIND_COMPONENTS_BY_JOIN_TABLE_NAME + "?" + SOURCE_COMPONENT + "=" + ComponentMyBatisHelper.componentClassToString(sourceComponentClass) + "&" + SOURCE_PROPERTIES
                + "=" + String.join(PROPERTIES_SEPARATOR, sourceProperties) + "&" + TARGET_PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, targetProperties) + "&" + JOIN + "=" + String
                .join("#", js) + (os != null ? "&" + ORDER_BY + "=" + String.join("#", os) : "") + (useCheckCancel ? "&" + IGNORE_CANCEL : "");
    }

    /**
     * Verify is find components by join table key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindComponentsByJoinTableKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    /**
     * Extract source component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractSourceComponentClassInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(3));
    }

    /**
     * Extract source properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractSourcePropertiesInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(5).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract target properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractTargetPropertiesInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(7).split(PROPERTIES_SEPARATOR);
    }

    /**
     * Extract join table
     *
     * @param key key
     * @return list of join
     */
    public static List<Pair<String, Pair<String[], String[]>>> extractJoinInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String[] joins = m.group(9).split("#");
        List<Pair<String, Pair<String[], String[]>>> res = new ArrayList<>();
        for (String join : joins) {
            String[] ss = join.split(";");
            res.add(Pair.of(ss[0], Pair.of(ss[1].split(PROPERTIES_SEPARATOR), ss[2].split(PROPERTIES_SEPARATOR))));
        }
        return res;
    }

    /**
     * Extract order by
     *
     * @param key key
     * @return order by
     */
    public static List<Pair<String, String>> extractOrderBiesInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return null;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        List<Pair<String, String>> res = new ArrayList<>();
        String group = m.group(16);
        if (StringUtils.isNotBlank(group)) {
            String[] os = group.split("#");
            for (String o : os) {
                String[] ss = o.split(";");
                res.add(Pair.of(ss[0], ss[1]));
            }
        }
        return res;
    }

    /**
     * Extract ignore cancel
     *
     * @param key key
     * @return true or false
     */
    public static boolean isIgnoreCancelInFindComponentsByJoinTableKey(String key) {
        if (!isFindComponentsByJoinTableKey(key)) {
            return false;
        }
        Matcher m = FIND_COMPONENTS_BY_JOIN_TABLE_PATTERN.matcher(key);
        return m.find() && IGNORE_CANCEL.equals(m.group(21));
    }

    // Insert

    /**
     * Build a insert key
     *
     * @param componentClass component class
     * @return key
     */
    public static <E extends IComponent> String buildInsertKey(Class<E> componentClass) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + INSERT_NAME;
    }

    /**
     * Verify is insert key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isInsertKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = INSERT_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInInsertKey(String key) {
        if (!isInsertKey(key)) {
            return null;
        }
        Matcher m = INSERT_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    // Update

    /**
     * Build update key
     *
     * @param componentClass component class
     * @return key
     */
    public static <E extends IComponent> String buildUpdateKey(Class<E> componentClass, String... nlsPropertyNames) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + UPDATE_NAME + "?" + NLS_PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, nlsPropertyNames);
    }

    /**
     * Verify is update key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isUpdateKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInUpdateKey(String key) {
        if (!isUpdateKey(key)) {
            return null;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    /**
     * Extract source properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractNlsPropertiesInUpdateKey(String key) {
        if (!isUpdateKey(key)) {
            return null;
        }
        Matcher m = UPDATE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        String properties = m.group(4);
        return properties != null ? properties.split(PROPERTIES_SEPARATOR) : new String[0];
    }

    // Delete

    /**
     * Build delete key
     *
     * @param componentClass component class
     * @return key
     */
    public static <E extends IComponent> String buildDeleteKey(Class<E> componentClass) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + DELETE_NAME;
    }

    /**
     * Verify is delete key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInDeleteKey(String key) {
        if (!isDeleteKey(key)) {
            return null;
        }
        Matcher m = DELETE_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    // NlsColumn

    /**
     * Build nls key
     *
     * @param componentClass component class
     * @param property       property
     * @return key
     */
    public static <E extends IComponent> String buildFindNlsColumnKey(Class<E> componentClass, String property) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + FIND_NLS_COLUMN_NAME + "?" + PROPERTY + "=" + property;
    }

    /**
     * Verify is nls key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isFindNlsColumnKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInFindNlsColumnKey(String key) {
        if (!isFindNlsColumnKey(key)) {
            return null;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    /**
     * Extract property
     *
     * @param key key
     * @return properties
     */
    public static String extractPropertyNameInFindNlsColumnByKey(String key) {
        if (!isFindNlsColumnKey(key)) {
            return null;
        }
        Matcher m = FIND_NLS_COLUMN_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3);
    }

    // DeleteEntityById

    /**
     * Build delete entity by id key
     *
     * @param componentClass component class
     * @return key
     */
    public static <E extends IComponent> String buildDeleteEntityByIdKey(Class<E> componentClass) {
        if (componentClass == null) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + DELETE_ENTITY_BY_ID_NAME;
    }

    /**
     * Verify is delete entity by id key
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteEntityByIdKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_ENTITY_BY_ID_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInDeleteEntityByIdKey(String key) {
        if (!isDeleteEntityByIdKey(key)) {
            return null;
        }
        Matcher m = DELETE_ENTITY_BY_ID_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    // DeleteComponentsBy

    /**
     * Build delete components by
     *
     * @param componentClass component class
     * @param propertyNames  array of property
     * @return key
     */
    public static <E extends IComponent> String buildDeleteComponentsByKey(Class<E> componentClass, String... propertyNames) {
        if (componentClass == null || propertyNames == null || propertyNames.length == 0) {
            return null;
        }
        return ComponentMyBatisHelper.componentClassToString(componentClass) + "/" + DELETE_COMPONENTS_BY_NAME + "?" + PROPERTIES + "=" + String.join(PROPERTIES_SEPARATOR, propertyNames);
    }

    /**
     * Verify is delete components by
     *
     * @param key key
     * @return true or false
     */
    public static boolean isDeleteComponentsByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Matcher m = DELETE_COMPONENTS_BY_PATTERN.matcher(key);
        return m.matches();
    }

    /**
     * Extract component in the key
     *
     * @param key key
     * @return component class
     */
    public static <E extends IComponent> Class<E> extractComponentClassInDeleteComponentsByKey(String key) {
        if (!isDeleteComponentsByKey(key)) {
            return null;
        }
        Matcher m = DELETE_COMPONENTS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return ComponentMyBatisHelper.loadComponentClass(m.group(1));
    }

    /**
     * Extract properties
     *
     * @param key key
     * @return properties
     */
    public static String[] extractPropertyNamesInDeleteComponentsByKey(String key) {
        if (!isDeleteComponentsByKey(key)) {
            return null;
        }
        Matcher m = DELETE_COMPONENTS_BY_PATTERN.matcher(key);
        if (!m.find()) {
            return null;
        }
        return m.group(3).split(PROPERTIES_SEPARATOR);
    }

}
