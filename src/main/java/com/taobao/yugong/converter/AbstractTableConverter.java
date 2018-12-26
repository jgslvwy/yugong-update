package com.taobao.yugong.converter;

import com.taobao.yugong.common.db.meta.ColumnMeta;
import com.taobao.yugong.common.db.meta.ForeignKeyMeta;
import com.taobao.yugong.common.db.meta.IndexMeta;
import com.taobao.yugong.common.lifecycle.AbstractYuGongLifeCycle;
import com.taobao.yugong.common.model.ExtractStatus;
import com.taobao.yugong.common.stats.ProgressTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;


public abstract class AbstractTableConverter extends AbstractYuGongLifeCycle implements TableConverter {
    private static final String CREATE_INDEX_SQL = "create {0} index {1} on {2}({3});";

    private static final String CREATE_CONSTRAINT_SQL = "alter table {0} add constraint {1} foreign key({2}) REFERENCES {3}({4});";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected volatile ExtractStatus status = ExtractStatus.NORMAL;

    protected volatile ProgressTracer tracer;

    public void setStatus(ExtractStatus status) {
        this.status = status;
    }

    public ExtractStatus status() {
        return status;
    }

    public ProgressTracer getTracer() {
        return tracer;
    }

    public void setTracer(ProgressTracer tracer) {
        this.tracer = tracer;
    }


    /**
     * 把Oracle的Type转成MySql的Type
     */
    protected String getMySQLType(ColumnMeta c) {
        if (c == null) {
            return null;
        }
        String type = c.getColumnType();
        if ("BLOB".equalsIgnoreCase(type)) {
            return "LONGTEXT";
        } else if ("DATE".equalsIgnoreCase(type)) {
            return "DATE";
        } else if ("VARCHAR2".equalsIgnoreCase(type)) {
            if (c.getColumnSize() >= 2000) {
                return "TEXT";
            } else {
                return "VARCHAR(" + c.getColumnSize() + ")";
            }
        } else if ("NUMBER".equalsIgnoreCase(type)) {
            if (c.getColScale() > 0) {
                int n = c.getColumnSize() - c.getColScale();//整数位
                int s = c.getColScale();                    //小数位
                return "DECIMAL(" + n + "," + s + ")";
            } else {
                return "INT(" + c.getColumnSize() + ")";
            }
        } else if ("INTEGER".equalsIgnoreCase(type)) {
            return "INT";
        } else if ("CLOB".equalsIgnoreCase(type)) {
            return "TEXT";
        }
        return c.getColumnType();
    }


    /**
     * 根据索引列表转换index创建语句
     *
     * @param indexMetas
     * @return
     */
    protected String getIndexSql(List<IndexMeta> indexMetas, String tableName) {
        StringBuilder sb = new StringBuilder();
        for (IndexMeta indexMeta : indexMetas) {
            if (indexMeta.isUniqueness())
                sb.append(MessageFormat.format(CREATE_INDEX_SQL, "", indexMeta.getIndexName(), tableName, indexMeta.getColumnName()));
            else {
                if (!"SEQUENCE_NO".equalsIgnoreCase(indexMeta.getColumnName())) {
                    sb.append(MessageFormat.format(CREATE_INDEX_SQL, "UNIQUE", indexMeta.getIndexName(), tableName, indexMeta.getColumnName()));
                }
            }
        }
        return sb.toString();
    }

    protected String getForeignKeySql(List<ForeignKeyMeta> foreignKeyMetas) {
        StringBuilder sb = new StringBuilder();
        for (ForeignKeyMeta fkm : foreignKeyMetas) {
            sb.append(MessageFormat.format(CREATE_CONSTRAINT_SQL, fkm.getTableName(), fkm.getConstraintName(), fkm.getConstraintColumn(), fkm.getPkTableName(), fkm.getPkConclumn()));
        }
        return sb.toString();
    }
}
