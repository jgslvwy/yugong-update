package com.taobao.yugong.common.db.meta;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.taobao.yugong.common.utils.YuGongToStringStyle;

/**
 * 代表一个字段的信息
 *
 * @author agapple 2013-9-3 下午2:46:32
 * @since 3.0.0
 */
public class ColumnMeta {

    private String name;
    private int type;

    private String comments;
    private int columnSize;
    private int colScale;
    private String columnType;
    private String defaultValue;
    private String nullValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getColScale() {
        return colScale;
    }

    public void setColScale(int colScale) {
        this.colScale = colScale;
    }

    public ColumnMeta(String columnName, int columnType) {
        this.name = StringUtils.upperCase(columnName);// 统一为大写
        this.type = columnType;
    }

    public ColumnMeta(String name, int type, String comments, int columnSize, int colScale, String columnType, String defaultValue, String nullValue) {
        this.name = name;
        this.type = type;
        this.comments = comments;
        this.columnSize = columnSize;
        this.colScale = colScale;
        this.columnType = columnType;
        this.defaultValue = defaultValue;
        this.nullValue = nullValue;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setName(String name) {
        this.name = StringUtils.upperCase(name);
    }

    public void setType(int type) {
        this.type = type;
    }

    public ColumnMeta clone() {
        return new ColumnMeta(this.name, this.type);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, YuGongToStringStyle.DEFAULT_STYLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + type;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ColumnMeta other = (ColumnMeta) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (type != other.type) return false;
        return true;
    }

}
