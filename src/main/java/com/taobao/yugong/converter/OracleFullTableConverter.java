package com.taobao.yugong.converter;

import com.taobao.yugong.common.db.meta.ColumnMeta;
import com.taobao.yugong.common.db.meta.Table;
import com.taobao.yugong.common.model.YuGongContext;
import com.taobao.yugong.common.utils.YuGongUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * by jgs
 *
 * @version 1.0
 */
public class OracleFullTableConverter extends AbstractTableConverter {
    public OracleFullTableConverter(YuGongContext context) {
        this.context = context;
    }

    private Table table;

    protected YuGongContext context;

    public void setTable(Table table) {
        this.table = table;
    }

    public void start() {
        super.start();
        String name = table.getName();
        String sname = table.getSchema();
        if (YuGongUtils.validateTableNameExist(context.getTargetDs(), sname, name)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(context.getTargetDs());
        // 非常有风险，但是为了解决创建表的无序性，先规避掉数据库的检查操作，避免因外键导致的创建失败
        // 可以先这样做：set FOREIGN_KEY_CHECKS=0;
//        String foreignKeySql = getForeignKeySql(table.getForeignKeyMetas());
//        boolean foreignKeyExists = StringUtils.isNotEmpty(foreignKeySql);
//        if (foreignKeyExists)
//            sb.append("SET FOREIGN_KEY_CHECKS=0;");
        sb.append("CREATE TABLE ").append(name.toUpperCase()).append("(");
        List<ColumnMeta> columns = table.getColumns();
        List<ColumnMeta> pks = table.getPrimaryKeys();
        // 默认主键为第一个数，如果存在联合主键再做另外处理，如果没有主键
        // 对于联合唯一主键，目前表中不存在除了SEQUENCE_NO以外的内容不这么处理
        for (int i = 0; i < pks.size(); i++) {
            ColumnMeta pkColumn = pks.get(i);
            String type = getMySQLType(pkColumn);
            if ("SEQUENCE_NO".equalsIgnoreCase(pkColumn.getName())) {
                sb.append("ID ").append(type + " NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT " + " '"
                        + pkColumn.getComments() + "'" + ",");
            } else {
                sb.append(pkColumn.getName() + " ").append(type + " NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT " + " '"
                        + pkColumn.getComments() + "'" + ",");
            }
        }
        for (int i = 0; i < columns.size(); i++) {
            ColumnMeta col = columns.get(i);
            String type = getMySQLType(col);
            sb.append(col.getName() + " ").append(type + " ");
            if (StringUtils.isNotEmpty(col.getDefaultValue())) {
                sb.append("DEFAULT" + " " + col.getDefaultValue() + " ");
            }
            sb.append(col.getNullValue() + " ");
            if (StringUtils.isNotEmpty(col.getComments()))
                sb.append("COMMENT" + " '" + col.getComments() + "'").append(",");
            else
                sb.append(",");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(") ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1; ");
        logger.info("创建语句：" + sb.toString());
        sb.append(getIndexSql(table.getIndexMetas(), name));
        // 执行完建表语句后，开启外键检查约束
//        if (foreignKeyExists) {
//            sb.append(foreignKeySql);
//            sb.append("set FOREIGN_KEY_CHECKS=1;");
//        }
        jdbcTemplate.execute(sb.toString());
    }
}
